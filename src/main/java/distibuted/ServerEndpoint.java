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

public class ServerEndpoint extends UnicastRemoteObject implements ServerInterface{

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
            serverController.createGame(client,newGameInfo);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGame(ClientInterface client, LoginInfo loginInfo) {
        try {
            serverController.joinGame(client, loginInfo);
            gameController = gameManagerController.getGame(loginInfo.gameId());
        } catch (RemoteException e) {
            e.printStackTrace();
         //   throw new RuntimeException(e);
        } catch (GameNotExistsException e) {
            e.printStackTrace();
          //  throw new RuntimeException(e);
        }
    }

    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo move) throws RemoteException {
        if(gameController!=null)
            gameController.doPlayerMove(client, move);
    }

    @Override
    public void sendMessage(ClientInterface client, Message message) throws RemoteException {
        if(gameController!=null)
            gameController.sendMessage(client, message);
    }
}
