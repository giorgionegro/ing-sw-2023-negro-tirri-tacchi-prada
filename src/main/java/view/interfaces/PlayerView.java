package view.interfaces;

import model.abstractModel.Player;
import modelView.PlayerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link Player}
 */
@FunctionalInterface
public interface PlayerView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the player info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(PlayerInfo o, Player.Event evt) throws RemoteException;
}
