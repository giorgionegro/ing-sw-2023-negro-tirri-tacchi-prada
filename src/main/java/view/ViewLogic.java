package view;

import distibuted.ClientEndPoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ClientSocketHandler;
import model.StandardMessage;
import model.User;
import model.abstractModel.*;
import modelView.*;
import util.TimedLock;
import view.graphicInterfaces.GameGraphics;
import view.graphicInterfaces.AppGraphics;
import view.interfaces.ViewCollection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages the view of the application and the communication with the server.
 */
public class ViewLogic implements Remote, ViewCollection, ActionListener {

    /**
     * Exit route
     */
    public static final int EXIT = -1;
    /**
     * Connection route
     */
    public static final int ROUTE_CONNECT = 0;
    /**
     * Join route
     */
    public static final int ROUTE_JOIN = 1;
    /**
     * Create route
     */
    public static final int ROUTE_CREATE = 2;
    /**
     * Game route
     */
    public static final int ROUTE_GAME = 3;
    /**
     * Home route
     */
    public static final int ROUTE_HOME = 4;
    /**
     * Connect action
     */
    public static final int CONNECT = 5;
    /**
     * Join action
     */
    public static final int JOIN = 6;
    /**
     * Create action
     */
    public static final int CREATE = 7;
    /**
     * Send message action
     */
    public static final int SEND_MESSAGE = 8;
    /**
     * Send move action
     */
    public static final int SEND_MOVE = 9;

    /**
     * Leave game action
     */
    public static final int LEAVE_GAME = 10;

    /**
     * update Thread pool
     */
    private final ExecutorService updateService = Executors.newFixedThreadPool(1);

    /**
     * RMI action
     */
    public static final String CONNECT_RMI = "RMI";
    /**
     * Socket action
     */
    public static final String CONNECT_SOCKET = "SOCKET";

    /**
     * This action performed is used to exchange messages between the view logic and the view
     *
     * @param e the event to be processed by the view
     */
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(gameGraphics) || e.getSource().equals(appGraphics)){
            switch (e.getID()){
                case EXIT -> exit();
                case ROUTE_CONNECT -> appGraphics.showConnection(e.getActionCommand());
                case ROUTE_JOIN -> appGraphics.showJoin(e.getActionCommand());
                case ROUTE_CREATE -> appGraphics.showCreate(e.getActionCommand());
                case ROUTE_GAME -> appGraphics.showGame(e.getActionCommand());
                case ROUTE_HOME -> appGraphics.showServerInteraction(e.getActionCommand());
                case CONNECT -> {
                    if(!this.connected){
                        connect(e.getActionCommand());
                    }else{
                        appGraphics.showServerInteraction("Already connected");
                    }
                }
                case CREATE -> createGame(e.getActionCommand());
                case JOIN -> joinGame(e.getActionCommand());
                case SEND_MESSAGE -> sendMessage(e.getActionCommand());
                case SEND_MOVE -> sendMove(e.getActionCommand());
                case LEAVE_GAME -> leaveGame();
            }
        }
    }

    /**
     * Application graphics Interface
     */
    private final AppGraphics appGraphics;
    /**
     * Game graphics Interface
     */
    private final GameGraphics gameGraphics;
    /**
     * Host IP
     */
    private final String hostIp;
    /**
     * Host RMI port
     */
    private final int RMIHostPort;
    /**
     * Host Socket port
     */
    private final int SocketHostPort;
    /**
     * Server reference
     */
    private AppServer server;
    /**
     * Server endpoint reference
     */
    private ServerInterface serverEndpoint;
    /**
     * Client endpoint reference
     */
    private final ClientInterface clientEndPoint;
    /**
     * time of the current session, used to check if incoming messages are valid
     */
    private long currentSessionTime = -1;

    /**
     * field used to check if the client is connected to the server
     */
    private boolean connected;

    /**
     * Constructor of the view logic
     *
     * @param appGraphics application graphics interface
     * @param hostIp host ip
     * @param RMIHostPort host RMI port
     * @param SocketHostPort host Socket port
     * @throws RemoteException if the connection fails
     */
    public ViewLogic(AppGraphics appGraphics, String hostIp, int RMIHostPort, int SocketHostPort) throws RemoteException {
        this.hostIp = hostIp;
        this.RMIHostPort = RMIHostPort;
        this.SocketHostPort = SocketHostPort;
        this.appGraphics = appGraphics;
        this.gameGraphics = appGraphics.getGameGraphics();
        this.appGraphics.setActionListener(this);
        this.clientEndPoint = new ClientEndPoint(this,this::disconnect);
        this.connected = false;
    }

    /**
     * This method is used start the view with the connection screen
     */
    public void start(){
        this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CONNECT,""));
    }

    /**
     * This method is used to exit the application
     */
    private void exit(){
        appGraphics.exit();
    }

    /**
     * disconnects the client from the server and returns to the connection screen
     *
     * @param message the message to be sent to the view
     */
    private void disconnect(String message){
        try {
            if(server!=null)
                server.disconnect(clientEndPoint);
        } catch (RemoteException e) {
            System.err.println("Error on disconnection, continue disconnecting.... ");
        }
        updateService.submit(() -> {
            connected = false;
            currentSessionTime = -1;
            serverEndpoint = null;
            server = null;
            this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CONNECT,message));
        });
    }

    /**
     * This method is used to connect to the server using specified connection type
     *
     * @param connectionType the type of connection to be used
     */
    private void connect(String connectionType) {
        if(!(connectionType.equals(CONNECT_RMI) || connectionType.equals(CONNECT_SOCKET)))
            this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CONNECT,"Unknown Command"));
        else{
            try {
                this.serverWaiter.reset(true);

                switch (connectionType) {
                    case CONNECT_RMI -> connectRMI(clientEndPoint);
                    case CONNECT_SOCKET -> connectSocket(clientEndPoint);
                }
                //wait for the server to respond or timeout
                if (!this.serverWaiter.hasBeenUnlocked()) {
                    try {
                        this.serverWaiter.lock(6000);
                    } catch (InterruptedException e) {
                        throw new RemoteException("Timer interrupted");
                    }
                }

                if (serverWaiter.getValue()) //if the server has not responded in time the connection is considered failed and we return to the connection screen
                    this.actionPerformed(new ActionEvent(appGraphics, ROUTE_CONNECT, "Unable to connect: connection timeout"));
                else {//otherwise we are connected and we can proceed to the home screen
                    this.connected = true;
                    this.actionPerformed(new ActionEvent(appGraphics, ROUTE_HOME, "Successfully connected"));
                }
            } catch (RemoteException e) {
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CONNECT,e.getMessage()));
            }
        }

    }

    /**
     * This method is used to connect to the server using RMI
     *
     * @param client the client to be connected to the server
     * @throws RemoteException if the connection fails
     */
    private void connectRMI(ClientInterface client) throws RemoteException {
        try {
            server = (AppServer) LocateRegistry.getRegistry(hostIp, RMIHostPort).lookup("server");
            serverEndpoint = server.connect(client);
        }catch (RemoteException | NotBoundException e){
            throw new RemoteException("Unable to open rmi connection");
        }
    }

    /**
     * This method is used to connect to the server using Socket
     *
     * @param client the client to be connected to the server
     * @throws RemoteException if the connection fails
     */
    private void connectSocket(ClientInterface client) throws RemoteException {
        try {
            server = new ClientSocketHandler(hostIp, SocketHostPort);
            serverEndpoint = server.connect(client);
        }catch(RemoteException e){
            throw new RemoteException("Unable to open socket connection");
        }
    }

    /**
     * This field is used to lock the client until the server responds or the timeout expires
     */
    private final TimedLock<Boolean> serverWaiter = new TimedLock<>(false);

    /**
     *  This method is used to parse join game info and send it to the server
     *  When the server responds the client is redirected to the game screen
     *
     * @param joinInfo the info to be sent to the server
     */
    // take PLAYERID GAMEID spaced
    private void joinGame(String joinInfo) {
        //check if the join info is valid
        if(joinInfo==null)
            this.actionPerformed(new ActionEvent(appGraphics,ROUTE_JOIN,"Null info value"));
        else {
            //count the number of parts to ensure that the string is well-formed
            String[] joinInfoParts = joinInfo.split(" ");
            if (joinInfoParts.length != 2){
                this.actionPerformed(new ActionEvent(appGraphics, ROUTE_JOIN, "Wrong number of parameters: "+joinInfoParts.length+" (2 required)"));
            }else if(!(joinInfoParts[0].isBlank() || joinInfoParts[1].isBlank())){
                try {
                    gameGraphics.resetGameGraphics(joinInfoParts[0]);

                    this.currentSessionTime = System.currentTimeMillis();
                    this.serverWaiter.reset(true);

                    this.serverEndpoint.joinGame(this.clientEndPoint, new LoginInfo(joinInfoParts[0], joinInfoParts[1], currentSessionTime));
                    //wait for server response or for timeout
                    if (!this.serverWaiter.hasBeenUnlocked()) {
                        try {
                            this.serverWaiter.lock(6000);
                        } catch (InterruptedException e) {
                            throw new RemoteException("Connection timeout error");
                        }
                    }

                    if (this.serverWaiter.getValue()) //if the server didn't respond in time we return to the join screen
                        this.actionPerformed(new ActionEvent(appGraphics,ROUTE_JOIN,userMessage));
                    else //otherwise we go to the game screen
                        this.actionPerformed(new ActionEvent(appGraphics,ROUTE_GAME,""));

                } catch (RemoteException e) {
                    this.actionPerformed(new ActionEvent(appGraphics,ROUTE_JOIN,"Unable to reach server"));
                }
            }else{
                this.actionPerformed(new ActionEvent(appGraphics, ROUTE_JOIN, "One of the parameters is blank"));
            }
        }
    }

    /**
     *  This method is used to parse create game info and send it to the server
     *  When the server responds the client is redirected to the Home screen
     *
     * @param gameInfo the info to be sent to the server
     */
    //TAKE GAMETYPE GAMEID PLAYERNUMBER spaced
    private void createGame(String gameInfo){
        //if the gameInfo is null, the user is notified and the method returns
        if(gameInfo==null)
            this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CREATE,"Null info value"));
        else {
            //counting the parts to ensure that the string is well-formed
            String[] gameInfoParts = gameInfo.split(" ");
            if(gameInfoParts.length!=3)
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CREATE,"Wrong number of parameters "+gameInfoParts.length));
            else if(!(gameInfoParts[0].isBlank() || gameInfoParts[1].isBlank() || gameInfoParts[2].isBlank())){
                try {
                    int playerNumber = Integer.parseInt(gameInfoParts[gameInfoParts.length-1]);
                    this.currentSessionTime = System.currentTimeMillis();
                    this.serverWaiter.reset(true);

                    this.serverEndpoint.createGame(this.clientEndPoint, new NewGameInfo(gameInfoParts[1], gameInfoParts[0], playerNumber, this.currentSessionTime));
                    //waiting for the server to respond or the timeout to expire
                    if (!this.serverWaiter.hasBeenUnlocked()) {
                        try {
                            this.serverWaiter.lock(6000);
                        } catch (InterruptedException e) {
                            throw new RemoteException("Connection timeout error");
                        }
                    }

                    if (this.serverWaiter.getValue()) //if timeout expired, the user is notified and we return to create screen
                        this.actionPerformed(new ActionEvent(appGraphics, ROUTE_CREATE, userMessage));
                    else
                        this.actionPerformed(new ActionEvent(appGraphics, ROUTE_HOME, "Game created successfully"));

                } catch (RemoteException e) {
                    this.actionPerformed(new ActionEvent(appGraphics, ROUTE_CREATE, "Unable to reach server"));
                } catch (NumberFormatException e) {
                    this.actionPerformed(new ActionEvent(appGraphics, ROUTE_CREATE, "\"Number of player\" parameter is not a number"));
                }
            } else{
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CREATE,"One of the parameters is blank"));
            }
        }
    }

    /**
     *  This method is used to parse login info and send it to the server
     *  When the server thows an exception the client is notified
     *
     * @param messageInfo the info to be sent to the server
     */
    //take idSender idSubject content spaced
    private void sendMessage(String messageInfo){
        //if the message is null the client is notified and the method returns
        if(messageInfo==null)
            gameGraphics.updateErrorState("Null message info");
        else{
            //otherwise we count the number of parts of the message to ensure that the message is well-formed
            String[] messageInfoParts = messageInfo.split("\n");
            if(messageInfoParts.length!=3){
                //if the message is not well-formed the client is notified
                gameGraphics.updateErrorState("Message has wrong number of parts");
            }else{
                try {
                    this.serverEndpoint.sendMessage(this.clientEndPoint, new StandardMessage(
                            messageInfoParts[0], messageInfoParts[1], messageInfoParts[2]
                    ));
                }catch (RemoteException e){
                    gameGraphics.updateErrorState("Unable to reach server");
                }
            }
        }
    }

    /**
     * This method is used to parse move info and send it to the server
     * When the server thows an exception the client is notified
     *
     * @param moveInfo the info to be sent to the server
     */
    //take tiles row,col spaced and columnInShelf after \n
    private void sendMove(String moveInfo){
        //if the move is null the client is notified
        if(moveInfo==null)
            gameGraphics.updateErrorState("Null move info");
        else{
            //else we check the number of parts before sending the move to the server to ensure that the message is parsable
            String[] moveInfoParts = moveInfo.split("\n");
            if(moveInfoParts.length!=2){
                gameGraphics.updateErrorState("Move has wrong number of parts");
            }else{
                try{
                    //now we create the picked tiles list
                    List<PickedTile> tiles = new ArrayList<>();
                    String[] pickedParts = moveInfoParts[0].trim().split(" ");

                    if(pickedParts.length<1 || pickedParts.length>3)
                        throw new NumberFormatException();

                    for(String picked : pickedParts){
                        String[] tileParts = picked.split(",");
                        if(tileParts.length!=2)
                            throw new NumberFormatException();

                        int x = Integer.parseInt(tileParts[0]);
                        int y = Integer.parseInt(tileParts[1]);
                        tiles.add(new PickedTile(x,y));
                    }

                    int sC = Integer.parseInt(moveInfoParts[1]);

                    this.serverEndpoint.doPlayerMove(this.clientEndPoint,new PlayerMoveInfo(tiles,sC));
                }catch(NumberFormatException e){
                    //if any argument is not a number we notify the user still to ensure that the message is parsable
                    gameGraphics.updateErrorState("Wrong move info format");
                } catch (RemoteException e) {
                    gameGraphics.updateErrorState("Unable to reach the server");
                }
            }
        }
    }

    /**
     * This method is used to leave the game
     */
    private void leaveGame(){
        try {
            //resetting the waiter
            this.serverWaiter.reset(true);
            //we leave the game and wait for the server to respond or the timeout to expire
            this.serverEndpoint.leaveGame(this.clientEndPoint);

            if (!this.serverWaiter.hasBeenUnlocked()) {
                try {
                    this.serverWaiter.lock(6000);
                } catch (InterruptedException e) {
                    throw new RemoteException("Connection timeout error");
                }
            }
            //if the timeout has expired we notify the user
            if (this.serverWaiter.getValue())
                gameGraphics.updateErrorState("Unable to leave game");
            else //otherwise we go back to the home screen
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_HOME,""));

        } catch (RemoteException e) {
            gameGraphics.updateErrorState("Unable to reach the server");
        }
    }


    /*----------------------------------------------------------*/

    /**
     * {@inheritDoc}
     *
     * @param o   the commonGoal info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updateCommonGoalGraphics(o.id(), o.description(), o.tokenState())
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param o   the gameInfo to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {
        updateService.submit(()->
            gameGraphics.updateGameInfoGraphics(o.status(),o.firstTurnPlayer(),o.playerOnTurn(),o.lastTurn(),o.points())
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param o   the livingRoom info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updateBoardGraphics(o.board())
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param o   the personalGoal info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        updateService.submit(() ->
           gameGraphics.updatePersonalGoalGraphics(o.id(),o.achieved(),o.description())
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param o   the playerChat info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updatePlayerChatGraphics(o.messages())
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param o   the player info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
        updateService.submit(() -> {
            if (evt == null) {
                return;
            }
            switch (evt) {
                case ERROR_REPORTED -> gameGraphics.updateErrorState(o.errorMessage());
                case COMMON_GOAL_ACHIEVED -> gameGraphics.updateAchievedCommonGoals(o.achievedCommonGoals());
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param o   the shelf info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updatePlayerShelfGraphics(o.playerId(),o.shelf())
        );
    }


    /**
     * message to be shown to be returned to the view
     */
    private String userMessage = "";

    /**
     * {@inheritDoc}
     *
     * @param o   the user info to be updated
     * @param evt the event that has generated this update
     * @throws RemoteException if the connection is lost
     */
    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        updateService.submit(() ->{
            //if the session is not the current one, the update is ignored
            if (o.sessionID() != this.currentSessionTime)
                return;
            //if the event is null, we unlock the server waiter and return
            if (evt == null) {
                this.serverWaiter.unlock(false);
                return;
            }

            switch (evt) {
                case GAME_JOINED, GAME_CREATED, GAME_LEAVED -> this.serverWaiter.unlock(false); //unlock the server waiter
                //otherwise we update the user message and unlock the server waiter
                case ERROR_REPORTED -> {
                    userMessage = o.eventMessage();
                    this.serverWaiter.unlock(true);
                }
            }
        });
    }
}
