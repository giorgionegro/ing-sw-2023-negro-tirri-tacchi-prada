package distibuted.socket.middleware.socketObjects;

import controller.interfaces.GameController;
import distibuted.interfaces.ClientInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.Message;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketMessage implements SocketObject, Serializable {
    private final Message message;

    public SocketMessage(Message message) {
        this.message = message;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try {
            ((GameController) receiver).sendMessage((ClientInterface) sender, message);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
