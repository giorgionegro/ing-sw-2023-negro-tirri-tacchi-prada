package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.GameManagerController;
import controller.interfaces.LobbyController;
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
import util.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is a combined implementation of a {@link ServerController} a {@link GameManagerController} and a {@link AppServer}
 */
public class StandardServerController extends UnicastRemoteObject implements ServerController, GameManagerController, AppServer {
    /**
     * The executor service that manage and run connections from socket interface
     */
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * The association between a client and a user on the server, it is assumed that only well authenticated clients has a user association
     */
    private final Map<ClientInterface, User> users;
    /**
     * The association between a user and the lobby it has joined
     */
    private final Map<User, LobbyController> activeUsers;
    /**
     * The association between a lobby and his ID
     */
    private final Map<String, LobbyController> lobbies;
    /**
     * The association between a lobby and the corresponding game controller
     */
    private final Map<LobbyController, GameController> gameControllers;

    /**
     * This constructor build an instance with no lobbies, game controller, users and active users
     *
     * @throws RemoteException If an error occurred on remote object construction or export
     */
    public StandardServerController() throws RemoteException {
        super();
        this.lobbies = new HashMap<>();
        this.gameControllers = new HashMap<>();
        this.users = new HashMap<>();
        this.activeUsers = new HashMap<>();
    }

    ///APP SERVER//////////////////////////////

    /**
     * {@inheritDoc}
     *
     * @param client {@inheritDoc}
     * @return a {@link ServerEndpoint} that represents client on the server side
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public ServerInterface connect(ClientInterface client) throws RemoteException {
        // Generate a User for the client
        User user = new User();
        // start ping thread
        new Thread(() -> {
            while (true) {
                try {
                    client.ping();
                    Thread.sleep(1000);
                } catch (InterruptedException | RemoteException e) {
                    System.err.println("ping failed");
                    try {
                        this.disconnect(client);
                    } catch (RemoteException ex) {
                        System.err.println("Error while disconnecting client");
                    }
                    break;
                }
            }
        }).start();
        user.addObserver(new Observer<User, User.Event>() {
            @Override
            public void update(User o, User.Event arg) {
                try {
                    client.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer ->...detaching user observer");
                    o.deleteObserver(this);
                }
            }
        });

        // Authorize the client
        this.users.put(client, user);

        // Generate the client on server reference
        ServerInterface server = new ServerEndpoint(this, this);

        // Bind server to client, this allows manage disconnections
        executorService.submit(() -> {
            try {
                client.bind(server);
            } catch (RemoteException e) {
                System.err.println("Client un-bound");
            }
        });

        System.out.println("CLIENT CONNECTED, connected users: " + this.users.size());

        return server;
    }

    /**
     * {@inheritDoc}
     *
     * @param client {@inheritDoc}
     * @throws RemoteException If an error occurred reaching the remote object
     */
    @Override
    public void disconnect(ClientInterface client) throws RemoteException {
        // Checks if the client has been authenticated
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else {
            try {
                this.leaveGame(client);
            } catch (RemoteException e) {
                System.err.println("ServerController: Failed to detach client from game, continuing disconnection...");
            }

            User user = this.users.remove(client);
            user.deleteObservers();

            System.out.println("CLIENT DISCONNECTED, connected user: " + this.users.size());
        }
    }

    ///SERVER CONTROLLER//////////////////

    /**
     * {@inheritDoc}
     *
     * @param client The client asking to join
     * @param info   The join-info
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void joinGame(ClientInterface client, LoginInfo info) throws RemoteException {
        // Checks if the client has been authenticated
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else {
            // Get User associated with the client
            User user = this.users.get(client);
            try {
                // Try to find a lobby associated with the given gameID
                if (!this.lobbies.containsKey(info.gameId()))
                    throw new GameAccessDeniedException("Game does not exists");

                // If User is already into a lobby then discard the request....
                if (user.getStatus() == User.Status.JOINED)
                    throw new GameAccessDeniedException("User already joined");

                // .... else add the Client to the requested lobby
                LobbyController lobbyController = this.lobbies.get(info.gameId());

                lobbyController.joinPlayer(client, user, info.playerId());

                this.activeUsers.put(user, lobbyController);

                user.reportEvent(User.Status.JOINED, "Joined game you are:" + info.playerId(), User.Event.GAME_JOINED, info.sessionID());

                System.out.println("PLAYER: " + info.playerId() + " JOINED: " + info.gameId());

            } catch (GameAccessDeniedException e) {
                user.reportEvent(User.Status.NOT_JOINED, e.getMessage(), User.Event.ERROR_REPORTED, info.sessionID());
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param client The client asking to leave
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void leaveGame(ClientInterface client) throws RemoteException {
        // Check if client has been authenticated
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else {
            User user = this.users.get(client);
            try {
                // Try lo let client leave the game is it into
                this.activeUsers.remove(user).leavePlayer(client);
            } catch (NullPointerException | GameAccessDeniedException e) {
                user.reportEvent(User.Status.NOT_JOINED, "Client is not connected to any game", User.Event.GAME_LEAVED);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param client   The client asking to create the game
     * @param gameInfo The new-game info
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void createGame(ClientInterface client, NewGameInfo gameInfo) throws RemoteException {
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else
            try {
                this.createGame(gameInfo);

                this.users.get(client).reportEvent(User.Status.NOT_JOINED, "Game created", User.Event.GAME_CREATED, gameInfo.sessionID());

                System.out.println("GAME CREATED: " + gameInfo.gameId());
            } catch (GameAlreadyExistsException | IllegalArgumentException e) {
                this.users.get(client).reportEvent(User.Status.NOT_JOINED, e.getMessage(), User.Event.ERROR_REPORTED, gameInfo.sessionID());
            }
    }

    ///GAMES MANAGER CONTROLLER/////////////////

    /**
     * {@inheritDoc}
     *
     * @param gameId the ID of the game
     * @return the {@link GameController} associated with the {@link LobbyController} associated with the given gameID
     * @throws GameNotExistsException if there is not {@link LobbyController} associated with the given gameID
     */
    @Override
    public GameController getGame(String gameId) throws GameNotExistsException {
        if (!this.lobbies.containsKey(gameId))
            throw new GameNotExistsException();

        return this.gameControllers.get(this.lobbies.get(gameId));
    }

    /**
     * {@inheritDoc}
     *
     * @param newGameInfo the new-game info that needs to be used
     * @throws GameAlreadyExistsException If there is already a {@link LobbyController} associated with the given gameID
     * @throws IllegalArgumentException   If {@link GameBuilder} couldn't build a game on given new-game info
     */
    @Override
    public void createGame(NewGameInfo newGameInfo) throws GameAlreadyExistsException, IllegalArgumentException {
        if (this.lobbies.containsKey(newGameInfo.gameId()))
            throw new GameAlreadyExistsException();

        Game newGame = GameBuilder.build(newGameInfo);

        StandardGameController controller = new StandardGameController(
                newGame,
                lobbyController -> {
                    this.lobbies.remove(newGameInfo.gameId());
                    this.gameControllers.remove(lobbyController);
                    System.out.println("GAME DELETED : " + newGameInfo.gameId());
                }
        );

        this.lobbies.put(newGameInfo.gameId(), controller);
        this.gameControllers.put(controller, controller);
    }
}
