package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.GamesManager;
import modelView.GamesManagerInfo;
import view.interfaces.GamesManagerView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketGamesManagerInfo implements SocketObject, Serializable {
    private final GamesManagerInfo o;
    private final GamesManager.Event evt;

    public SocketGamesManagerInfo(GamesManagerInfo o, GamesManager.Event evt) {
        this.o = o;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((GamesManagerView) receiver).update(o,evt);
        }catch(ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
