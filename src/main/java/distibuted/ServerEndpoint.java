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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerEndpoint extends UnicastRemoteObject implements ServerInterface{

    private final ServerController serverController;
    private final GameManagerController gameManagerController;
    private @Nullable GameController gameController;

    public ServerEndpoint(ServerController serverController, GameManagerController gameManagerController) throws RemoteException {
        super();
        this.serverController = serverController;
        this.gameManagerController = gameManagerController;
        this.gameController = null;
    }

    @Override
    public synchronized void createGame(ClientInterface client, NewGameInfo newGameInfo) {
        try {
            serverController.createGame(client,newGameInfo);
        } catch (RemoteException e) {
            printError("CreateGame",e.getMessage());
        }
    }

    @Override
    public void joinGame(@NotNull ClientInterface client, @NotNull LoginInfo loginInfo){
        try {
            serverController.joinGame(client, loginInfo);
            gameController = gameManagerController.getGame(loginInfo.gameId());
        } catch (RemoteException e) {
            printError("JoinGame", e.getMessage());
        } catch (GameNotExistsException e){
            gameController = null;
        }
    }

    @Override
    public void leaveGame(ClientInterface client){
        try {
            serverController.leaveGame(client);
            gameController = null;
        } catch (RemoteException e) {
            printError("LeaveGame", e.getMessage());
        }
    }

    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo move){
        if(gameController!=null) {
            try {
                gameController.doPlayerMove(client, move);
            } catch (RemoteException e) {
                printError("DoPlayerMove", e.getMessage());
            }
        }
    }

    @Override
    public void sendMessage(ClientInterface client, Message message){
        if(gameController!=null) {
            try {
                gameController.sendMessage(client, message);
            } catch (RemoteException e) {
                printError("SendMessage", e.getMessage());
            }
        }
    }

    private void printError(String from, String message){
        if(!message.isBlank())
            message = " : "+message;
        System.err.print("ServerEndpoint: exception from " + from + message);
    }
}
