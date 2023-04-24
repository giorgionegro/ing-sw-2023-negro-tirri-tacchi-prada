package distibuted.socket.middleware.socketObjects;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import modelView.LoginInfo;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketLoginInfo implements SocketObject, Serializable {

    private final LoginInfo info;

    public SocketLoginInfo(LoginInfo info){
        this.info = info;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try {
            ((ServerInterface) receiver).joinGame((ClientInterface) sender, info);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
