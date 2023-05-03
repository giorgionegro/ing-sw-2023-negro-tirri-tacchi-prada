package distibuted.socket.middleware;

import controller.interfaces.GameController;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.Message;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import modelView.PlayerMoveInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSocketHandler implements ServerInterface, AppServer {

    private final String ip;
    private final int port;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private Thread receiverLoop;

    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void waitForReceive(ClientInterface client) throws RemoteException {
        try {
            SocketObject no = (SocketObject) ois.readObject();
            no.update(this, client);
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);//TODO
        }
    }

    public synchronized void close() throws RemoteException {
        try {
            receiverLoop.interrupt();
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }

    public synchronized void send(SocketObject o) throws IOException {
        oos.writeObject(o);
        oos.flush();
        oos.reset();
    }


    ////APP SERVER//////////////////

    @Override
    public synchronized ServerInterface connect(ClientInterface client) throws RemoteException {
        try {
            this.socket = new Socket(ip, port);
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

            receiverLoop = new Thread(()->{
                while(true){
                    try {
                        waitForReceive(client);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            receiverLoop.start();

            return this; //TODO controllare meglio la posizione di questo return

        } catch (IOException e) {
            throw new RemoteException("Unable to connect to the server", e);
        }
    }

    @Override
    public void disconnect(ClientInterface client) throws RemoteException {
        //TODO Manda segnale di disconnessione
        close();
    }


    ///SERVER INTERFACE///////////////


    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo info) throws RemoteException {
        try {
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((GameController) receiver).doPlayerMove((ClientInterface) sender, info);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(ClientInterface client, Message newMessage) throws RemoteException {
        try {
            send((SocketObject) (sender,receiver) -> {
                try {
                    ((GameController) receiver).sendMessage((ClientInterface) sender, newMessage);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGame(ClientInterface client, LoginInfo info) {
        try {
            send((SocketObject) (sender,receiver) -> {
                try {
                    ((ServerInterface) receiver).joinGame((ClientInterface) sender, info);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(ClientInterface client, NewGameInfo info) {
        try {
            send((SocketObject) (sender,receiver) -> {
                try{
                    ((ServerInterface) receiver).createGame((ClientInterface) sender, info);
                }catch (ClassCastException e){
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
