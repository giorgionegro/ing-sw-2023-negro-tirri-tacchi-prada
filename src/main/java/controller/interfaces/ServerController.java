package controller.interfaces;

import distibuted.interfaces.ClientInterface;
import modelView.LoginInfo;
import modelView.NewGameInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerController extends Remote {
    void joinGame(ClientInterface client, LoginInfo info) throws RemoteException;
    void createGame(ClientInterface client, NewGameInfo info) throws RemoteException;
}
