package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.User;
import modelView.UserInfo;
import view.interfaces.UserView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketUserInfo implements SocketObject, Serializable {

    private final UserInfo info;
    private final User.Event evt;

    public SocketUserInfo(UserInfo info, User.Event evt) {
        this.info = info;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((UserView) receiver).update(info, evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
