package distibuted.interfaces;

import controller.interfaces.GameController;
import model.abstractModel.Message;
import modelView.NewGameInfo;
import modelView.PlayerMoveInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote, GameController {
    enum ServerEvent{
        GAME_CREATED,
        GAME_NOT_CREATED,
        GAME_RETRIEVED,
        GAME_NOT_RETRIEVED
    }
    void register(ClientInterface client) throws RemoteException;
    ServerEvent getGame(ClientInterface client, String gameId) throws  RemoteException;
    ServerEvent createNewGame(ClientInterface client, NewGameInfo newGameInfo) throws  RemoteException;
}
