package distibuted;

import controller.GameController;
import controller.GamesManagerController;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.Message;
import modelView.GameInfo;
import modelView.PlayerMove;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerEndpoint extends UnicastRemoteObject implements ServerInterface {

    private final GamesManagerController gamesManagerController;
    private GameController gameController;

    public ServerEndpoint(GamesManagerController gamesManagerController) throws RemoteException {
        super();
        this.gamesManagerController = gamesManagerController;
        this.gameController = null;
    }

    @Override
    public synchronized void register(ClientInterface client) throws RemoteException {
        //TODO (non ho idea di che farci qua)
    }

    @Override
    public synchronized void connectToGame(ClientInterface client, String playerId, String gameId) {
        GameController gameController = this.gamesManagerController.getGameController(client, gameId);
        if(gameController!=null){
            gameController.join(client, playerId);
            this.gameController = gameController;
        }
    }

    @Override
    public synchronized void createNewGame(ClientInterface client, GameInfo newGameInfo) {
        gamesManagerController.createGame(client,newGameInfo);
    }

    @Override
    public synchronized void makeMove(ClientInterface client, PlayerMove move) {
        if(gameController!=null)
            gameController.doPlayerMove(client,move);
    }

    @Override
    public void sendMessage(ClientInterface client, Message message) {
        gameController.sendMessage(client, message);
    }
}
