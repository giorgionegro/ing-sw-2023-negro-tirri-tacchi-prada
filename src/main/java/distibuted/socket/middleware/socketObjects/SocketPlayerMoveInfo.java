package distibuted.socket.middleware.socketObjects;

import controller.interfaces.GameController;
import distibuted.interfaces.ClientInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import modelView.PlayerMoveInfo;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketPlayerMoveInfo implements SocketObject, Serializable {
    private final PlayerMoveInfo info;

    public SocketPlayerMoveInfo(PlayerMoveInfo info) {
        this.info = info;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((GameController) receiver).doPlayerMove((ClientInterface) sender, info);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
