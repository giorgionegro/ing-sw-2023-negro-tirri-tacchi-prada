package view.interfaces;

import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link LivingRoom}
 */
@FunctionalInterface
public interface LivingRoomView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the livingRoom info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException;
}
