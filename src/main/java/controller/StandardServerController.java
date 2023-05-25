package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.GameManagerController;
import controller.interfaces.ServerController;
import distibuted.ServerEndpoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import model.StandardGame;
import model.abstractModel.Game;
import util.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StandardServerController extends UnicastRemoteObject implements ServerController, GameManagerController, AppServer{

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<ClientInterface, User> users;
    private final Map<User,StandardGameController> activeUsers;
    private final Map<String, StandardGameController> gameControllers;

    public StandardServerController() throws RemoteException {
        super();
        this.gameControllers = new HashMap<>();
        this.users = new HashMap<>();
        this.activeUsers = new HashMap<>();
    }

    ///APP SERVER//////////////////////////////

    @Override
    public ServerInterface connect(ClientInterface client) throws RemoteException {
        User user = new User();
        user.addObserver(new Observer<User,User.Event>() {
            @Override
            public void update(User o, User.Event arg) {
                try {
                    client.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching user observer");
                    o.deleteObserver(this);
                }
            }
        });

        users.put(client,user);

        ServerInterface server = new ServerEndpoint(this,this);

        executorService.submit(() -> {
            try {
                client.bind(server);
            } catch (RemoteException e) {
                try {
                    disconnect(client);
                } catch (RemoteException ex) {
                    System.err.println("Error while client disconnection");
                }
            }
        });

        System.err.println("CLIENT CONNECTED");

        return server;
    }

    @Override
    public void disconnect(ClientInterface client) throws RemoteException {
        leaveGame(client);
        User user = users.remove(client);
        user.deleteObservers();

        System.err.println("CLIENT DISCONNECTED");
    }

    ///SERVER CONTROLLER//////////////////

    @Override
    public void joinGame(ClientInterface client, LoginInfo info) throws RemoteException {
        try{
            if(!this.gameControllers.containsKey(info.gameId()))
                throw new GameAccessDeniedException("Game does not exists");

            User user = users.get(client);

            if(user.getStatus()== User.Status.JOINED)
                throw new GameAccessDeniedException("User already joined");

            StandardGameController gameController = this.gameControllers.get(info.gameId());

            gameController.joinPlayer(client, user, info.playerId());

            activeUsers.put(user,gameController);

            System.err.println("GIOCATORE JOIN");
        } catch (GameAccessDeniedException e) {
            users.get(client).reportEvent(User.Status.NOT_JOINED,e.getMessage(),info.time(), User.Event.ERROR_REPORTED);
        }
    }

    @Override
    public void leaveGame(ClientInterface client) throws RemoteException{
        try {
            activeUsers.get(users.get(client)).leavePlayer(client);
            List<User> users = new ArrayList<>(activeUsers.keySet());
            for(User u : users)
                if(u.getStatus()== User.Status.NOT_JOINED)
                    activeUsers.remove(u);

        } catch (GameAccessDeniedException e) {
            throw new RuntimeException("Client is not connected to any match");
        } catch (NullPointerException e) {
            throw new RemoteException("Client is not connected correctly");
        }
    }

    @Override
    public void createGame(ClientInterface client, NewGameInfo gameInfo) throws RemoteException{
        try {
            createGame(gameInfo.gameId(),gameInfo.playerNumber());
            users.get(client).reportEvent(User.Status.NOT_JOINED,"Game created",gameInfo.time(),  User.Event.GAME_CREATED);
            System.err.println("GAME CREATED: "+gameInfo.gameId());
        } catch (GameAlreadyExistsException e) {
            users.get(client).reportEvent(User.Status.NOT_JOINED,e.getMessage(), gameInfo.time(), User.Event.ERROR_REPORTED);
        }
    }

    ///GAMES MANAGER CONTROLLER/////////////////

    @Override
    public GameController getGame(String gameId) throws GameNotExistsException {
        if(!gameControllers.containsKey(gameId))
            throw new GameNotExistsException();

        return gameControllers.get(gameId);
    }



    @Override
    public void createGame(String gameId, int playerNumber) throws GameAlreadyExistsException {
        if (gameControllers.containsKey(gameId))
            throw new GameAlreadyExistsException();

        Game newGame = new StandardGame(gameId, playerNumber);

        gameControllers.put(gameId, new StandardGameController(newGame));
    }
}
