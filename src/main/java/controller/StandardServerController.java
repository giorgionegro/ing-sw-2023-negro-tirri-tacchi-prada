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

/**
 * This class is a combined implementation of a {@link ServerController} a {@link GameManagerController} and a {@link AppServer}
 */
public class StandardServerController extends UnicastRemoteObject implements ServerController, GameManagerController, AppServer {
    /**
     * The association between a client and a user on the server, it is assumed that only well authenticated clients have a user association
     */
    private final Map<ClientInterface, User> users;
    /**
     * The association between a user and the lobby it has joined
     */
    private final Map<User, LobbyController> activeUsers;
    /**
     * The association between a lobby and its ID
     */
    private final Map<String, LobbyController> lobbies;
    /**
     * The association between a lobby and the corresponding game controller
     */
    private final Map<LobbyController, GameController> gameControllers;

    /**
     * This constructor builds an instance with no lobbies, game controller, users and active users
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
    public synchronized ServerInterface connect(ClientInterface client) throws RemoteException {
        // Generate a User for the client
        User user = new User();
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

        // Generate the serverEndpoint of the connection
        ServerInterface server = new ServerEndpoint(this, this, s -> this.disconnect(client));

        // Binds serverEndpoint and clientEndpoint
        client.bind(server);

        System.out.println("CLIENT CONNECTED, currently connected users: " + this.users.size());

        return server;
    }

    /**
     * {@inheritDoc}
     *
     * @param client {@inheritDoc}
     */
    @Override
    public synchronized void disconnect(ClientInterface client) {
        /* Checks if the client has been authenticated */
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else {
            /* Tries to make client leave a game if it is connected to one*/
            this.leaveGame(client);
            /* Un-authenticate the client and remove user observers */
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
     */
    @Override
    public synchronized void joinGame(ClientInterface client, LoginInfo info) {
        /* Checks if the client has been authenticated */
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else {
            /* Get User associated with the client */
            User user = this.users.get(client);
            try {
                /* Try to find a lobby associated with the given gameID */
                if (!this.lobbies.containsKey(info.gameId()))
                    throw new GameAccessDeniedException("Game does not exists");

                /* If User is already into a lobby then discard the request.... */
                if (user.getStatus() == User.Status.JOINED)
                    throw new GameAccessDeniedException("User already joined");

                /* .... else add the Client to the requested lobby */
                LobbyController lobbyController = this.lobbies.get(info.gameId());

                lobbyController.joinPlayer(client, user, info.playerId());

                /* Associates user to the lobby it's connected to */
                this.activeUsers.put(user, lobbyController);

                /* Reports user it has joined the lobby */
                user.reportEvent(User.Status.JOINED, "Joined game, you are:" + info.playerId(), User.Event.GAME_JOINED, info.sessionID());

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
     */
    @Override
    public synchronized void leaveGame(ClientInterface client) {
        /* Check if client has been authenticated */
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else {
            /* Get user associated with client */
            User user = this.users.get(client);
            try {
                /* Remove association between user and lobby and then let user leave the lobby */
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
     */
    @Override
    public synchronized void createGame(ClientInterface client, NewGameInfo gameInfo) {
        /* Check if client has been authenticated */
        if (!this.users.containsKey(client))
            System.err.println("ServerController: Command from unauthenticated client");
        else
            try {
                /* Create a game using gameManager controller */
                LobbyController lobby = this.createGame(gameInfo);

                /* Put lobby into known list */
                this.lobbies.put(gameInfo.gameId(), lobby);

                /* Reports user it created successfully a game*/
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
        /* If a lobby with the given name exists... */
        if (!this.lobbies.containsKey(gameId))
            throw new GameNotExistsException();

        /* ... then return the gameController associated to that lobby */
        return this.gameControllers.get(this.lobbies.get(gameId));
    }

    /**
     * {@inheritDoc}
     *
     * @param newGameInfo the new-game info that needs to be used
     * @return a {@link LobbyController} associated with the same model of the created {@link GameController}
     * @throws GameAlreadyExistsException If there is already a {@link GameController} associated with the given gameID
     * @throws IllegalArgumentException   If {@link GameBuilder} couldn't build a game on given new-game info
     */
    @Override
    public LobbyController createGame(NewGameInfo newGameInfo) throws GameAlreadyExistsException, IllegalArgumentException {
        /* If a lobby with the given id does not already exists ... */
        if (this.lobbies.containsKey(newGameInfo.gameId()))
            throw new GameAlreadyExistsException();

        /* ... then build game model using Game Factory ... */
        Game newGame = GameBuilder.build(newGameInfo);

        /* ... and creates a controller that manages the game model we have just created */
        StandardGameController controller = new StandardGameController(
                newGame,
                lobbyController -> {
                    /* gameClosedCallback remove the lobby and the gameController from the known list */
                    this.lobbies.remove(newGameInfo.gameId());
                    this.gameControllers.remove(lobbyController);
                    System.out.println("GAME DELETED : " + newGameInfo.gameId());
                }
        );

        /* Associate gameController to the lobby created */
        this.gameControllers.put(controller, controller);

        return controller;
    }
}
