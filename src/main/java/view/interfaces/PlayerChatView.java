package view.interfaces;

import model.abstractModel.PlayerChat;
import modelView.PlayerChatInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link PlayerChat}
 */
@FunctionalInterface
public interface PlayerChatView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the playerChat info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException;
}
