package distibuted.socket.middleware;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * TODO
 * This interface represents an operation between two objects
 */
@FunctionalInterface
public interface SocketObject extends Serializable {
    /**
     * This method contains the interaction between a sender and a receiver
     * @param sender the sender of the interaction
     * @param receiver the receiver of the interaction
     * @throws RemoteException If an error occurred during the interaction
     */
    void interact(Object sender, Object receiver) throws RemoteException;
}
