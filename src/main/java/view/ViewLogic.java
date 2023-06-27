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

public class ViewLogic implements Remote, ViewCollection, ActionListener {

    public static final int EXIT = -1;
    public static final int ROUTE_CONNECT = 0;
    public static final int ROUTE_JOIN = 1;
    public static final int ROUTE_CREATE = 2;
    public static final int ROUTE_GAME = 3;
    public static final int ROUTE_HOME = 4;
    public static final int CONNECT = 5;
    public static final int JOIN = 6;
    public static final int CREATE = 7;
    public static final int SEND_MESSAGE = 8;
    public static final int SEND_MOVE = 9;

    public static final int LEAVE_GAME = 10;

    private final ExecutorService updateService = Executors.newFixedThreadPool(1);

    public static final String CONNECT_RMI = "RMI";
    public static final String CONNECT_SOCKET = "SOCKET";

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

    private final AppGraphics appGraphics;
    private final GameGraphics gameGraphics;
    private final String hostIp;
    private final int RMIHostPort;
    private final int SocketHostPort;
    private AppServer server;
    private ServerInterface serverEndpoint;
    private final ClientInterface clientEndPoint;
    private long currentSessionTime = -1;

    private boolean connected;
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

    public void start(){
        this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CONNECT,""));
    }

    private void exit(){
        appGraphics.exit();
    }

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

                if (!this.serverWaiter.hasBeenUnlocked()) {
                    try {
                        this.serverWaiter.lock(6000);
                    } catch (InterruptedException e) {
                        throw new RemoteException("Timer interrupted");
                    }
                }

                if (serverWaiter.getValue())
                    this.actionPerformed(new ActionEvent(appGraphics, ROUTE_CONNECT, "Unable to connect: connection timeout"));
                else {
                    this.connected = true;
                    this.actionPerformed(new ActionEvent(appGraphics, ROUTE_HOME, "Successfully connected"));
                }
            } catch (RemoteException e) {
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CONNECT,e.getMessage()));
            }
        }

    }

    private void connectRMI(ClientInterface client) throws RemoteException {
        try {
            server = (AppServer) LocateRegistry.getRegistry(hostIp, RMIHostPort).lookup("server");
            serverEndpoint = server.connect(client);
        }catch (RemoteException | NotBoundException e){
            throw new RemoteException("Unable to open rmi connection");
        }
    }

    private void connectSocket(ClientInterface client) throws RemoteException {
        try {
            server = new ClientSocketHandler(hostIp, SocketHostPort);
            serverEndpoint = server.connect(client);
        }catch(RemoteException e){
            throw new RemoteException("Unable to open socket connection");
        }
    }
    private final TimedLock<Boolean> serverWaiter = new TimedLock<>(false);

    // take PLAYERID GAMEID spaced
    public void joinGame(String joinInfo) {
        if(joinInfo==null)
            this.actionPerformed(new ActionEvent(appGraphics,ROUTE_JOIN,"Null info value"));
        else {
            String[] joinInfoParts = joinInfo.split(" ");
            if (joinInfoParts.length != 2){
                this.actionPerformed(new ActionEvent(appGraphics, ROUTE_JOIN, "Wrong number of parameters: "+joinInfoParts.length+" (2 required)"));
            }else if(!(joinInfoParts[0].isBlank() || joinInfoParts[1].isBlank())){
                try {
                    gameGraphics.resetGameGraphics(joinInfoParts[0]);

                    this.currentSessionTime = System.currentTimeMillis();
                    this.serverWaiter.reset(true);

                    this.serverEndpoint.joinGame(this.clientEndPoint, new LoginInfo(joinInfoParts[0], joinInfoParts[1], currentSessionTime));

                    if (!this.serverWaiter.hasBeenUnlocked()) {
                        try {
                            this.serverWaiter.lock(6000);
                        } catch (InterruptedException e) {
                            throw new RemoteException("Connection timeout error");
                        }
                    }

                    if (this.serverWaiter.getValue())
                        this.actionPerformed(new ActionEvent(appGraphics,ROUTE_JOIN,userMessage));
                    else
                        this.actionPerformed(new ActionEvent(appGraphics,ROUTE_GAME,""));

                } catch (RemoteException e) {
                    this.actionPerformed(new ActionEvent(appGraphics,ROUTE_JOIN,"Unable to reach server"));
                }
            }else{
                this.actionPerformed(new ActionEvent(appGraphics, ROUTE_JOIN, "One of the parameters is blank"));
            }
        }
    }

    //TAKE GAMETYPE GAMEID PLAYERNUMBER spaced
    private void createGame(String gameInfo){
        if(gameInfo==null)
            this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CREATE,"Null info value"));
        else {
            String[] gameInfoParts = gameInfo.split(" ");
            if(gameInfoParts.length!=3)
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_CREATE,"Wrong number of parameters "+gameInfoParts.length));
            else if(!(gameInfoParts[0].isBlank() || gameInfoParts[1].isBlank() || gameInfoParts[2].isBlank())){
                try {
                    int playerNumber = Integer.parseInt(gameInfoParts[gameInfoParts.length-1]);
                    this.currentSessionTime = System.currentTimeMillis();
                    this.serverWaiter.reset(true);

                    this.serverEndpoint.createGame(this.clientEndPoint, new NewGameInfo(gameInfoParts[1], gameInfoParts[0], playerNumber, this.currentSessionTime));

                    if (!this.serverWaiter.hasBeenUnlocked()) {
                        try {
                            this.serverWaiter.lock(6000);
                        } catch (InterruptedException e) {
                            throw new RemoteException("Connection timeout error");
                        }
                    }

                    if (this.serverWaiter.getValue())
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

    //take idSender idSubject content spaced
    private void sendMessage(String messageInfo){
        if(messageInfo==null)
            gameGraphics.updateErrorState("Null message info");
        else{
            String[] messageInfoParts = messageInfo.split("\n");
            if(messageInfoParts.length!=3){
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
    //take tiles row,col spaced and columnInShelf after \n
    private void sendMove(String moveInfo){
        if(moveInfo==null)
            gameGraphics.updateErrorState("Null move info");
        else{
            String[] moveInfoParts = moveInfo.split("\n");
            if(moveInfoParts.length!=2){
                gameGraphics.updateErrorState("Move has wrong number of parts");
            }else{
                try{
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
                    gameGraphics.updateErrorState("Wrong move info format");
                } catch (RemoteException e) {
                    gameGraphics.updateErrorState("Unable to reach the server");
                }
            }
        }
    }

    private void leaveGame(){
        try {
            this.serverWaiter.reset(true);

            this.serverEndpoint.leaveGame(this.clientEndPoint);

            if (!this.serverWaiter.hasBeenUnlocked()) {
                try {
                    this.serverWaiter.lock(6000);
                } catch (InterruptedException e) {
                    throw new RemoteException("Connection timeout error");
                }
            }

            if (this.serverWaiter.getValue())
                gameGraphics.updateErrorState("Unable to leave game");
            else
                this.actionPerformed(new ActionEvent(appGraphics,ROUTE_HOME,""));

        } catch (RemoteException e) {
            gameGraphics.updateErrorState("Unable to reach the server");
        }
    }


    /*----------------------------------------------------------*/

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updateCommonGoalGraphics(o.id(), o.description(), o.tokenState())
        );
    }

    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {
        updateService.submit(()->
            gameGraphics.updateGameInfoGraphics(o.status(),o.firstTurnPlayer(),o.playerOnTurn(),o.lastTurn(),o.points())
        );
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updateBoardGraphics(o.board())
        );
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        updateService.submit(() ->
           gameGraphics.updatePersonalGoalGraphics(o.id(),o.achieved(),o.description())
        );
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updatePlayerChatGraphics(o.messages())
        );
    }

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

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
        updateService.submit(() ->
            gameGraphics.updatePlayerShelfGraphics(o.playerId(),o.shelf())
        );
    }


    private String userMessage = "";

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        updateService.submit(() ->{
            if (o.sessionID() != this.currentSessionTime)
                return;

            if (evt == null) {
                this.serverWaiter.unlock(false);
                return;
            }

            switch (evt) {
                case GAME_JOINED, GAME_CREATED, GAME_LEAVED -> this.serverWaiter.unlock(false);
                case ERROR_REPORTED -> {
                    userMessage = o.eventMessage();
                    this.serverWaiter.unlock(true);
                }
            }
        });
    }
}
