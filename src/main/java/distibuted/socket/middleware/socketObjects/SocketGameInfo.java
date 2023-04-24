package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.Game;
import modelView.GameInfo;
import view.interfaces.GameView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketGameInfo implements SocketObject, Serializable {

    private final GameInfo o;
    private final Game.Event evt;

    public SocketGameInfo(GameInfo o, Game.Event evt){
        this.o = o;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try {
            ((GameView) receiver).update(o, evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
