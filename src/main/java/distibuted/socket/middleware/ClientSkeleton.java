package distibuted.socket.middleware;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.*;
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

    public void waitForReceive(ServerInterface server) throws RemoteException {
        Object o;
        try {
            o = ois.readObject();
            //TODO server.send(o,null);


        } catch (IOException e) {
            throw new RemoteException("Cannot receive choice from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize choice from client", e);
        }
    }

    public void update(GameInfo o, Object arg) throws RemoteException {
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


    public void update(CommonGoalInfo o, Object arg) throws RemoteException {
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

    public void update(ShelfInfo o, Object arg) throws RemoteException {
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


    public void update(LivingRoomInfo o, Object arg) throws RemoteException {
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


    public void update(PlayerChatInfo o, Object arg) throws RemoteException {
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

    public void update(PersonalGoalInfo o, Object arg) throws RemoteException {
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
    public void update(CommonGoalInfo info, CommonGoal.CommonGoalEvent evt) {

    }

    @Override
    public void update(GamesManagerInfo o, GamesManager.GamesManagerEvent evt) {

    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.LivingRoomEvent evt) {

    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.PersonalGoalEvent evt) {

    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.PlayerChatEvent evt) {

    }

    @Override
    public void update(PlayerInfo o, Player.PlayerEvent evt) {

    }

    @Override
    public void update(ShelfInfo o, Shelf.ShelfEvent evt) {

    }

    @Override
    public void update(GameInfo o, Game.GameEvent evt) {

    }
}
