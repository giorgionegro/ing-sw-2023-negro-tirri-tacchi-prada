
import distibuted.interfaces.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    ServerInterface connect() throws RemoteException;
}
