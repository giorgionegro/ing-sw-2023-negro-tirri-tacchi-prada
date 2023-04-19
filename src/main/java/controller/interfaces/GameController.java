package controller.interfaces;

import distibuted.interfaces.ClientInterface;
import model.abstractModel.Message;
import modelView.PlayerMoveInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameController extends Remote {
    enum Event{
        NOT_JOINED,
        JOINED,
    }
    Event join(ClientInterface newClient, String playerId) throws RemoteException;
    void doPlayerMove(ClientInterface client, PlayerMoveInfo move) throws RemoteException;
    void sendMessage(ClientInterface client, Message newMessage) throws RemoteException;
}
