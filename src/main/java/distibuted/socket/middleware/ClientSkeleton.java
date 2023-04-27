package distibuted.socket.middleware;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import model.User;
import model.abstractModel.*;
import modelView.*;
import view.interfaces.*;

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
        try {
            SocketObject no = (SocketObject) ois.readObject();
            no.update(this, server);
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);//TODO
        }
    }

    public synchronized void send(SocketObject o) throws IOException {
        oos.writeObject(o);
        oos.flush();
        oos.reset();
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender, receiver) -> {
                try {
                    ((CommonGoalView) receiver).update(o, evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender, receiver) -> {
                try {
                    ((GameView) receiver).update(o, evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((GamesManagerView) receiver).update(o,evt);
                }catch(ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((LivingRoomView)receiver).update(o,evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((PersonalGoalView) receiver).update(o, evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((PlayerChatView) receiver).update(o, evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((ShelfView)receiver).update(o, evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        try{
            send((SocketObject) (sender, receiver) -> {
                try{
                    ((UserView) receiver).update(o, evt);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
    }
}
