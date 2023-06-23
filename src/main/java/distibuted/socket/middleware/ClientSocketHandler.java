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
import java.rmi.RemoteException;

public class ClientSocketHandler extends SocketHandler<ClientInterface> implements ServerInterface, AppServer {

    private Thread receiverLoop;

    public ClientSocketHandler(String ip, int port) {
        super(ip, port);
    }

    ////APP SERVER//////////////////

    @Override
    public synchronized ServerInterface connect(ClientInterface client) throws RemoteException {
        super.open();

        this.receiverLoop = new Thread(() -> {
            try {
                while (true) {
                    this.waitForReceive(client);
                }
            } catch (RemoteException e) {
                System.err.println("Cannot receive from server: " + e.getMessage() + ".\n-> Closing this connection...");
            }
        });

        this.receiverLoop.start();

        return this;
    }

    @Override
    public synchronized void disconnect(ClientInterface client) throws RemoteException {
        try {
            this.receiverLoop.interrupt();
            super.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }


    ///SERVER INTERFACE///////////////


    @Override
    public void doPlayerMove(ClientInterface client, PlayerMoveInfo info) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((GameController) receiver).doPlayerMove((ClientInterface) sender, info);
                } catch (ClassCastException e) {
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
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((GameController) receiver).sendMessage((ClientInterface) sender, newMessage);
                } catch (ClassCastException e) {
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
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ServerInterface) receiver).joinGame((ClientInterface) sender, info);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void leaveGame(ClientInterface client) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ServerInterface) receiver).leaveGame((ClientInterface) sender);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(ClientInterface client, NewGameInfo info) {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ServerInterface) receiver).createGame((ClientInterface) sender, info);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
