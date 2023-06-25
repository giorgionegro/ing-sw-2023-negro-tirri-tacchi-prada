package view.interfaces;

import model.User;
import modelView.UserInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link User}
 */
@FunctionalInterface
public interface UserView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the user info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(UserInfo o, User.Event evt) throws RemoteException;
}