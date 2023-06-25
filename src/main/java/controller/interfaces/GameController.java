package controller.interfaces;

import distibuted.interfaces.ClientInterface;
import model.abstractModel.Message;
import modelView.PlayerMoveInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the interface of a game controller
 * <p>
 * It defines all the method required to interact with a {@link model.abstractModel.Game} instance
 */
public interface GameController extends Remote {

    /**
     * This method allows client to do a game move
     * @param client The reference of the client who is doing the move
     * @param move The move info
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void doPlayerMove(ClientInterface client, PlayerMoveInfo move) throws RemoteException;

    /**
     * This method allows client to send a message
     * @param client The reference of the client who is sending the message
     * @param newMessage The message info
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void sendMessage(ClientInterface client, Message newMessage) throws RemoteException;
}
