package distibuted.socket.middleware;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import modelView.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSkeleton implements ClientInterface {

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public ClientSkeleton(Socket socket) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create output stream", e);
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create input stream", e);
        }
    }

    public void receive(ServerInterface server) throws RemoteException {
        Object o;
        try {
            o = ois.readObject();
            if(o instanceof GameInfo gI){
                server.createNewGame(this, gI);
            } else if (o instanceof LoginInfo l) {
                server.connectToGame(this, l.getPlayerId(), l.getPlayerId());
            } else if (o instanceof PlayerMove pM) {
                server.makeMove(this, pM);
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot receive choice from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize choice from client", e);
        }
    }

    @Override
    public void updateGameStatus(GameView o, Object arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }

    @Override
    public void updateCommonGoal(CommonGoalView o, Object arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }

    @Override
    public void updateShelf(ShelfView o, Object arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }

    @Override
    public void updateLivingRoom(LivingRoomView o, Object arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }

    @Override
    public void updatePlayerChat(PlayerChatView o, Object arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }

    @Override
    public void updatePersonalGoal(PersonalGoalView o, Object arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }

    @Override
    public void signalError(String error) throws RemoteException {
        try {
            oos.writeObject(error);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(new Object());
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }
}
