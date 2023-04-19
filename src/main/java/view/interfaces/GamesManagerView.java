package view.interfaces;

import model.abstractModel.GamesManager;
import modelView.GamesManagerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GamesManagerView extends Remote {
    void update(GamesManagerInfo o, GamesManager.GamesManagerEvent evt) throws RemoteException;
}
