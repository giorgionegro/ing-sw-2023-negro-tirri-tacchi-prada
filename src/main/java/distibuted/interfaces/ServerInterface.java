package distibuted.interfaces;

import model.abstractModel.Message;
import modelView.GameInfo;
import modelView.PlayerMove;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void register(ClientInterface client) throws RemoteException;
    void connectToGame(ClientInterface client, String playerId, String gameId) throws  RemoteException;
    void createNewGame(ClientInterface client, GameInfo newGameInfo) throws  RemoteException;
    void makeMove(ClientInterface client, PlayerMove move)throws  RemoteException;
    void sendMessage(ClientInterface client, Message message)throws  RemoteException;
}
