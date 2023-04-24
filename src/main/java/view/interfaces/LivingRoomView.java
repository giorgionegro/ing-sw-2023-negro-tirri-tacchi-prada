package view.interfaces;

import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LivingRoomView extends Remote {
    void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException;
}
