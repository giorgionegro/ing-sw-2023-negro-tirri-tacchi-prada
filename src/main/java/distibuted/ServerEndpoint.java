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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerEndpoint extends UnicastRemoteObject implements ServerInterface {

    private final ServerController serverController;
    private final GameManagerController gameManagerController;

    private GameController gameController;

    public ServerEndpoint(ServerController serverController, GameManagerController gameManagerController) throws RemoteException {
        super();
        this.serverController = serverController;
        this.gameManagerController = gameManagerController;
        this.gameController = null;
    }

    @Override
    public synchronized void createGame(ClientInterface client, NewGameInfo newGameInfo) {
        try {
            this.serverController.createGame(client, newGameInfo);
        } catch (RemoteException e) {
            this.printError("CreateGame", e.getMessage());
        }
    }

    @Override
    public void joinGame(ClientInterface client, LoginInfo loginInfo) {
        try {
            this.serverController.joinGame(client, loginInfo);
            this.gameController = this.gameManagerController.getGame(loginInfo.gameId());
        } catch (RemoteException e) {
            this.printError("JoinGame", e.getMessage());
        } catch (GameNotExistsException e) {
            this.gameController = null;
        }
    }

    @Override
    public void leaveGame(ClientInterface client) {
        try {
            this.serverController.leaveGame(client);
            this.gameController = null;
        } catch (RemoteException e) {
            this.printError("LeaveGame", e.getMessage());
        }
    }

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

    private void printError(String from, String message) {
        if (!message.isBlank())
            message = " : " + message;
        System.err.print("ServerEndpoint: exception from " + from + message);
    }
}
