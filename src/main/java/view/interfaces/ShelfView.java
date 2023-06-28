package view.interfaces;

import model.abstractModel.Shelf;
import modelView.ShelfInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link Shelf}
 */
@FunctionalInterface
public interface ShelfView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the shelf info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(ShelfInfo o, Shelf.Event evt) throws RemoteException;
}
