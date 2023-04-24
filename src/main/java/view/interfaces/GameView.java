package view.interfaces;

import model.abstractModel.Game;
import modelView.GameInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameView extends Remote {
    void update(GameInfo o, Game.Event evt) throws RemoteException;
}
