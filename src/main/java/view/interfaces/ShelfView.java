package view.interfaces;

import model.abstractModel.Shelf;
import modelView.ShelfInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface ShelfView extends Remote {
    void update(ShelfInfo o, Shelf.Event evt) throws RemoteException;
}
