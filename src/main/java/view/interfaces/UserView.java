package view.interfaces;

import model.User;
import modelView.UserInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface UserView extends Remote {
    void update(UserInfo o, User.Event evt) throws RemoteException;
}