package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.PlayerChat;
import modelView.PlayerChatInfo;
import view.interfaces.PlayerChatView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketPlayerChatInfo implements SocketObject, Serializable {

    private final PlayerChatInfo info;
    private final PlayerChat.Event evt;

    public SocketPlayerChatInfo(PlayerChatInfo info, PlayerChat.Event evt) {
        this.info = info;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((PlayerChatView) receiver).update(info, evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
