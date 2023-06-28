package view.interfaces;

import model.abstractModel.Game;
import modelView.GameInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link Game}
 */
@FunctionalInterface
public interface GameView extends Remote {
    /**
     * This method updates the view based on vien info and event
     * @param o the gameInfo
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(GameInfo o, Game.Event evt) throws RemoteException;
}
