package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;
import view.interfaces.LivingRoomView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketLivingRoomInfo implements SocketObject, Serializable {

    private final LivingRoomInfo info;
    private final LivingRoom.Event evt;

    public SocketLivingRoomInfo(LivingRoomInfo info, LivingRoom.Event evt) {
        this.info = info;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((LivingRoomView)receiver).update(info,evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
