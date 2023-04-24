package distibuted.socket.middleware;

import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
import distibuted.socket.middleware.socketObjects.SocketLoginInfo;
import distibuted.socket.middleware.socketObjects.SocketMessage;
import distibuted.socket.middleware.socketObjects.SocketNewGameInfo;
import distibuted.socket.middleware.socketObjects.SocketPlayerMoveInfo;
import model.abstractModel.Message;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import modelView.PlayerMoveInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerStub implements ServerInterface, AppServer {

    String ip;
    int port;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;

    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

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

            return this; //TODO controllare meglio la posizione di questo return

        } catch (IOException e) {
            throw new RemoteException("Unable to connect to the server", e);
        }
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
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }

    public synchronized void send(Object o) throws IOException {
        oos.writeObject(o);
        oos.flush();
        oos.reset();
    }

    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo move) throws RemoteException {
        try {
            send(new SocketPlayerMoveInfo(move));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(ClientInterface client, Message newMessage) throws RemoteException {
        try {
            send(new SocketMessage(newMessage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGame(ClientInterface client, LoginInfo info) {
        try {
            send(new SocketLoginInfo(info));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(ClientInterface client, NewGameInfo info) {
        try {
            send(new SocketNewGameInfo(info));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
