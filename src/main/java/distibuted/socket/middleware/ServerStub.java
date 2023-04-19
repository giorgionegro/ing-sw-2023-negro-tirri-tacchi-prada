package distibuted.socket.middleware;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.Message;
import modelView.GameInfo;
import modelView.PlayerMove;
import modelView.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerStub implements ServerInterface {

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
    public void register(ClientInterface client) throws RemoteException {
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
        } catch (IOException e) {
            throw new RemoteException("Unable to connect to the server", e);
        }
    }

    public void receive(ClientInterface client) throws RemoteException {
        Object o;
        Object arg;
        try {
            o = ois.readObject();
            arg = ois.readObject();

            if (o instanceof GameView gV) {
                client.updateGameStatus(gV, arg);
            } else if (o instanceof CommonGoalView cGV) {
                client.updateCommonGoal(cGV, arg);
            } else if (o instanceof LivingRoomView lRV) {
                client.updateLivingRoom(lRV, arg);
            } else if (o instanceof PersonalGoalView pGV) {
                client.updatePersonalGoal(pGV, arg);
            } else if (o instanceof PlayerChatView pCV) {
                client.updatePlayerChat(pCV, arg);
            } else if (o instanceof ShelfView sV) {
                client.updateShelf(sV, arg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws RemoteException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }

    @Override
    public void connectToGame(ClientInterface client, String playerId, String gameId){
        try {
            oos.writeObject(new LoginInfo(playerId,gameId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createNewGame(ClientInterface client, GameInfo newGameInfo) {
        try{
            oos.writeObject(newGameInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void makeMove(ClientInterface client, PlayerMove move) {
        try {
            oos.writeObject(move);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(ClientInterface client, Message message) {
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
