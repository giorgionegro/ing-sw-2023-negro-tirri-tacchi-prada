package distibuted.socket.middleware.interfaces;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * TODO
 */
public interface SocketObject extends Serializable {
    /**
     * TODO
     * @param sender
     * @param receiver
     * @throws RemoteException
     */
    void update(Object sender, Object receiver) throws RemoteException;
}
