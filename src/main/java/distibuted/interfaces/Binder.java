package distibuted.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interfaces contains the methods necessary to bind a server to a client and manage the connection
 */
public interface Binder extends Remote {
    /**
     * This method binds a client to a server
     * @param server a ServerInterface that represents the server the client will bind with
     * @throws RemoteException if the binding fails
     */
    void bind(ServerInterface server) throws RemoteException;

    /**
     * This method is used to ping a client
     * @throws RemoteException if the ping fails
     */
    void ping() throws RemoteException;
}
