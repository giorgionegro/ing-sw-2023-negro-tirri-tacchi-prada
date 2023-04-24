package distibuted.socket.middleware.socketObjects;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import modelView.NewGameInfo;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketNewGameInfo implements SocketObject, Serializable {
    private final NewGameInfo o;

    public SocketNewGameInfo(NewGameInfo o){
        this.o = o;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((ServerInterface) receiver).createGame((ClientInterface) sender, o);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
