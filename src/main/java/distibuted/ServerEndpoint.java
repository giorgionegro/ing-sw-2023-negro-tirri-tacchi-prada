package distibuted;

import controller.interfaces.GameController;
import controller.interfaces.GameManagerController;
import controller.interfaces.ServerController;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.Message;
import model.exceptions.GameNotExistsException;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import modelView.PlayerMoveInfo;
import util.TimedLock;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.Consumer;

/**
 * This class is an implementation of {@link ServerInterface} and represents the server endpoint for socket and RMI connections
 */
public class ServerEndpoint extends UnicastRemoteObject implements ServerInterface {

    /**
     * The server controller this endpoint interacts with
     */
    private final ServerController serverController;

    /**
     * The game manager controller this endpoint interacts with
     */
    private final GameManagerController gameManagerController;

    /**
     * The game controller this endpoint interacts with
     */
    private GameController gameController;

    /**
     * This is the lock used to wait for the ping
     */
    private final TimedLock<Boolean> pingWaiter = new TimedLock<>(true);


    /**
     * This class constructor crates an instance of this class with a given {@link controller.interfaces.ServerController}, {@link controller.interfaces.GameManagerController} and null {@link controller.interfaces.GameController}
     * @param serverController a given server controller
     * @param gameManagerController a given game manager controller
     * @param disconnectionCallback a callback to be called when the client disconnects
     * @throws RemoteException in case of an error occurred exporting this remote object
     */
    public ServerEndpoint(ServerController serverController, GameManagerController gameManagerController, Consumer<String> disconnectionCallback) throws RemoteException {
        super();
        this.serverController = serverController;
        this.gameManagerController = gameManagerController;
        this.gameController = null;

        /* Run a thread that wait 5 seconds for a ping to be received, if not then disconnect the client */
        new Thread(()->{
            try {
                Thread.sleep(2000);
                do {
                    this.pingWaiter.reset(true);

                    this.pingWaiter.lock(5000);

                } while (this.pingWaiter.hasBeenUnlocked());

            } catch (InterruptedException e) {
                System.err.println("Error: Ping thread had been interrupted");
            } finally {
                this.pingWaiter.reset(false);
                disconnectionCallback.accept("Ping timeout");
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     * @param client      The client asking to create the game
     * @param newGameInfo The new-game info
     */
    @Override
    public synchronized void createGame(ClientInterface client, NewGameInfo newGameInfo) {
        try {
            this.serverController.createGame(client, newGameInfo);
        } catch (RemoteException e) {
            this.printError("CreateGame", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param client    The client asking to join
     * @param loginInfo The join-info
     */
    @Override
    public void joinGame(ClientInterface client, LoginInfo loginInfo) {
        try {
            /* Try to join a game */
            this.serverController.joinGame(client, loginInfo);
            /* Retrieves the gameController of the game it has joined */
            this.gameController = this.gameManagerController.getGame(loginInfo.gameId());
        } catch (RemoteException e) {
            this.printError("JoinGame", e.getMessage());
        } catch (GameNotExistsException e) {
            this.gameController = null;
        }
    }

    /**
     * {@inheritDoc}
     * @param client The client asking to leave
     */
    @Override
    public void leaveGame(ClientInterface client) {
        try {
            this.serverController.leaveGame(client);
            this.gameController = null;
        } catch (RemoteException e) {
            this.printError("LeaveGame", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param client The reference of the client who is doing the move
     * @param move   The move info
     */
    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo move) {
        if (this.gameController != null) {
            try {
                this.gameController.doPlayerMove(client, move);
            } catch (RemoteException e) {
                this.printError("DoPlayerMove", e.getMessage());
            }
        }
    }

    /**
     *{@inheritDoc}
     * @param client  The reference of the client who is sending the message
     * @param message The message info
     */
    @Override
    public void sendMessage(ClientInterface client, Message message) {
        if (this.gameController != null) {
            try {
                this.gameController.sendMessage(client, message);
            } catch (RemoteException e) {
                this.printError("SendMessage", e.getMessage());
            }
        }
    }

    /**
     * This method prints an error that has occurred
     * @param from source of the error
     * @param message the message of the error
     */
    private void printError(String from, String message) {
        if (!message.isBlank())
            message = " : " + message;
        System.err.print("ServerEndpoint: exception from " + from + message);
    }

    /**
     * {@inheritDoc}
     * <p>
     * It notifies the ping handler Thread that a ping has been received
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void ping() throws RemoteException {
        /* If client is not connected anymore then throw an exception */
        if (!this.pingWaiter.getValue())
            throw new RemoteException();
        /* Else notify the ping thread */
        this.pingWaiter.unlock(true);
    }
}
