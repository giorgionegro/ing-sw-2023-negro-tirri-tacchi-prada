package distibuted.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the interface of a service-provider server to which a client can connect and disconnect
 */
public interface AppServer extends Remote {
    /**
     * This method connects a client to the server
     * @param client The client that asked to be connected
     * @return the service provider interface this server manages
     * @throws RemoteException If an error occurred reaching the remote object
     */
    ServerInterface connect(ClientInterface client) throws RemoteException;

    /**
     * This method disconnects a client from the server
     * @param client The client that asked to be disconnected
     * @throws RemoteException If an error occurred reaching the remote object
     */
    void disconnect(ClientInterface client) throws RemoteException;
}
