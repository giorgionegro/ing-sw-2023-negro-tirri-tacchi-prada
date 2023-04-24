package view.interfaces;

import model.abstractModel.PlayerChat;
import modelView.PlayerChatInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerChatView extends Remote {
    void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException;
}
