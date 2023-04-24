package distibuted.socket.middleware.interfaces;

import java.rmi.RemoteException;

public interface SocketObject {
    void update(Object sender, Object receiver) throws RemoteException;
}
