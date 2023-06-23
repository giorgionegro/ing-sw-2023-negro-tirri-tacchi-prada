package distibuted.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Binder extends Remote {
    void bind(ServerInterface server) throws RemoteException;
    void ping() throws RemoteException;
}
