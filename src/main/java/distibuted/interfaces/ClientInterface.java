package distibuted.interfaces;

import view.interfaces.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is a collection of interfaces and contains all the methods a server can call on client
 */
public interface ClientInterface
        extends Remote, ViewCollection{
    /**
     * This method binds a client to a server
     * @param server the {@link ServerInterface} of the server that client have to bind to
     * @throws RemoteException if the binding fails
     */
    void bind(ServerInterface server) throws RemoteException;
}
