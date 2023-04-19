package distibuted;

import controller.StandardGameController;
import controller.GamesManagerController;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.Message;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.NewGameInfo;
import modelView.PlayerMoveInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerEndpoint extends UnicastRemoteObject implements ServerInterface {

    private final GamesManagerController gamesManagerController;
    private StandardGameController gameController;

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
    public synchronized ServerEvent getGame(ClientInterface client, String gameId) {
        try{
            this.gameController = this.gamesManagerController.getGameController(gameId);
            return ServerEvent.GAME_RETRIEVED;
        } catch (GameNotExistsException e) {
            return ServerEvent.GAME_NOT_RETRIEVED;
        }
    }

    @Override
    public synchronized ServerEvent createNewGame(ClientInterface client, NewGameInfo newGameInfo) {
        try {
            gamesManagerController.createGame(newGameInfo);
            return ServerEvent.GAME_CREATED;
        } catch (GameAlreadyExistsException | IllegalArgumentException e) {
            return ServerEvent.GAME_NOT_CREATED;
        }
    }

    @Override
    public Event join(ClientInterface newClient, String playerId) {
        if(gameController!=null)
            return gameController.join(newClient, playerId);
        return Event.NOT_JOINED;
    }

    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo move) {
        if(gameController!=null)
            gameController.doPlayerMove(client, move);
    }

    @Override
    public void sendMessage(ClientInterface client, Message message) {
        if(gameController!=null)
            gameController.sendMessage(client, message);
    }
}
