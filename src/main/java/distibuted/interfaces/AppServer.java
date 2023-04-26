package distibuted.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    ServerInterface connect(ClientInterface client) throws RemoteException;
    void disconnect(ClientInterface client) throws RemoteException;
}
