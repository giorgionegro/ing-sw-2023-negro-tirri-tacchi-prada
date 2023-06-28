package distibuted.interfaces;

import controller.interfaces.GameController;
import controller.interfaces.ServerController;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is a collection of interfaces and contains all the methods a client can call on server
 */
public interface ServerInterface extends Remote, ServerController, GameController{
    /**
     * This method is used to ping the server
     * @throws RemoteException if the ping fails
     */
    void ping() throws RemoteException;
}