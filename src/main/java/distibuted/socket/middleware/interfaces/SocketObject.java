package distibuted.socket.middleware.interfaces;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface SocketObject extends Serializable {

    void update(Object sender, Object receiver) throws RemoteException;
}
