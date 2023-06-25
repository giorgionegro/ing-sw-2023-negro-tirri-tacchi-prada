package view.interfaces;

import model.abstractModel.Player;
import modelView.PlayerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface PlayerView extends Remote {
    void update(PlayerInfo o, Player.Event evt) throws RemoteException;
}
