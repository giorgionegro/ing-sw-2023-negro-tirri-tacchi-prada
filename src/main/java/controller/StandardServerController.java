package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.GameManagerController;
import controller.interfaces.ServerController;
import distibuted.ServerEndpoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.GameBuilder;
import model.User;
import model.abstractModel.Game;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import org.jetbrains.annotations.NotNull;
import util.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StandardServerController extends UnicastRemoteObject implements ServerController, GameManagerController, AppServer {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private final @NotNull Map<ClientInterface, User> users;
    private final @NotNull Map<User, StandardGameController> activeUsers;
    private final @NotNull Map<String, StandardGameController> gameControllers;

    public StandardServerController() throws RemoteException {
        super();
        this.gameControllers = new HashMap<>();
        this.users = new HashMap<>();
        this.activeUsers = new HashMap<>();
    }

    ///APP SERVER//////////////////////////////

    @Override
    public @NotNull ServerInterface connect(@NotNull ClientInterface client) throws RemoteException {
        User user = new User();
        user.addObserver(new Observer<User, User.Event>() {
            @Override
            public void update(@NotNull User o, User.Event arg) {
                try {
                    client.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer ->...detaching user observer");
                    o.deleteObserver(this);
                }
            }
        });

        this.users.put(client, user);

        ServerInterface server = new ServerEndpoint(this, this);

        executorService.submit(() -> { //can we test this?
            try {
                client.bind(server);
            } catch (RemoteException e) {
                try {
                    this.disconnect(client);
                } catch (RemoteException ex) {
                    System.err.println("Error while disconnecting client");
                }
            }
        });

        System.out.println("CLIENT CONNECTED");

        return server;
    }

    @Override
    public void disconnect(ClientInterface client) throws RemoteException {
        try{
            this.leaveGame(client);
        }catch(RemoteException e){
            System.err.println("ServerController: Failed to detach client from game, continuing disconnection...");
        }

        User user = this.users.remove(client);
        user.deleteObservers();

        System.err.println("CLIENT DISCONNECTED");
    }

    ///SERVER CONTROLLER//////////////////

    @Override
    public void joinGame(@NotNull ClientInterface client, @NotNull LoginInfo info) throws RemoteException {
        try {
            if (!this.gameControllers.containsKey(info.gameId()))
                throw new GameAccessDeniedException("Game does not exists");

            User user = this.users.get(client);

            if (user.getStatus() == User.Status.JOINED)
                throw new GameAccessDeniedException("User already joined");
            StandardGameController gameController = this.gameControllers.get(info.gameId());
            gameController.joinPlayer(client, user, info);

            this.activeUsers.put(user, gameController);

            System.err.println("PLAYER: "+info.playerId()+" JOINED: "+info.gameId());

        } catch (GameAccessDeniedException e) {
            if (this.users.containsKey(client) && this.users.get(client) != null)
                this.users.get(client).reportEvent(User.Status.NOT_JOINED, e.getMessage(), info.time(), User.Event.ERROR_REPORTED);
        }
    }

    @Override
    public void leaveGame(ClientInterface client) throws RemoteException {
        try {
            activeUsers.get(users.get(client)).leavePlayer(client);
        } catch (NullPointerException | GameAccessDeniedException e) {
            throw new RemoteException("Client is not connected correctly");
        }
    }

    @Override
    public void createGame(ClientInterface client, @NotNull NewGameInfo gameInfo) throws RemoteException {
        try {
            if (!this.users.containsKey(client))
                throw new GameAccessDeniedException("Client is not connected correctly");

            createGame(gameInfo);

            users.get(client).reportEvent(User.Status.NOT_JOINED, "Game created", gameInfo.time(), User.Event.GAME_CREATED);
            System.err.println("GAME CREATED: "+gameInfo.gameId());
        } catch (GameAlreadyExistsException | GameAccessDeniedException | IllegalArgumentException e) {
            if (users.containsKey(client) && users.get(client) != null)
                users.get(client).reportEvent(User.Status.NOT_JOINED, e.getMessage(), gameInfo.time(), User.Event.ERROR_REPORTED);
        }
    }

    ///GAMES MANAGER CONTROLLER/////////////////

    @Override
    public GameController getGame(String gameId) throws GameNotExistsException {
        if (!this.gameControllers.containsKey(gameId))
            throw new GameNotExistsException();

        return this.gameControllers.get(gameId);
    }


    @Override
    public void createGame(NewGameInfo newGameInfo) throws GameAlreadyExistsException, IllegalArgumentException {
        if (gameControllers.containsKey(newGameInfo.gameId()))
            throw new GameAlreadyExistsException();

        Game newGame = GameBuilder.build(newGameInfo);

        gameControllers.put(newGameInfo.gameId(), new StandardGameController(newGame));
    }
}
