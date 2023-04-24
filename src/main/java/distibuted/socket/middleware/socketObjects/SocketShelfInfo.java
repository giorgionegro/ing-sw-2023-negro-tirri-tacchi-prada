package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.Shelf;
import modelView.ShelfInfo;
import view.interfaces.ShelfView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketShelfInfo implements SocketObject, Serializable {

    private final ShelfInfo s;
    private final Shelf.Event evt;
    public SocketShelfInfo(ShelfInfo s, Shelf.Event evt){
        this.s = s;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((ShelfView)receiver).update(s, evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
