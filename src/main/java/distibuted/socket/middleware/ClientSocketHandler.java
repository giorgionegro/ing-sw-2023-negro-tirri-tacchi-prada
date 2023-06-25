package distibuted.socket.middleware;

import controller.interfaces.GameController;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.Message;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import modelView.PlayerMoveInfo;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * This class manages a socket connection on client side
 */
public class ClientSocketHandler extends SocketHandler<ClientInterface> implements ServerInterface, AppServer {

    /**
     * Thread responsible for reading the input stream
     */
    private Thread receiverLoop;

    /**
     * Class constructor, initialize a {@link SocketHandler} with the given ip and port
     * @param ip Ip address of the socket
     * @param port Port of the socket
     */
    public ClientSocketHandler(String ip, int port) {
        super(ip, port);
    }

    ////APP SERVER//////////////////

    /**
     * {@inheritDoc}
     * @param client The client that asked to be connected
     * @return this object as a {@link ServerInterface}
     * @throws RemoteException if socket connection fails to open
     */
    @Override
    public synchronized ServerInterface connect(ClientInterface client) throws RemoteException {
        /* Calls SocketHandler to open the socket connection */
        super.open();
        
        /* Create and runs input stream handler thread */
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

        /* Returns this object as a serverInterface*/
        return this;
    }

    /**
     * {@inheritDoc}
     * @param client The client that asked to be disconnected
     * @throws RemoteException if socket connection fails to close
     */
    @Override
    public synchronized void disconnect(ClientInterface client) throws RemoteException {
        try {
            /* If a receiverLoop is running then stop it */
            if(receiverLoop!=null)
                this.receiverLoop.interrupt();
            
            /* Calls socketHandler to close socket connection */
            super.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }

    ///SERVER INTERFACE///////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link GameController#doPlayerMove(ClientInterface, PlayerMoveInfo)} on receiver
     * @param client The reference of the client who is doing the move
     * @param info   The move info
     * @throws RemoteException if fails to send the {@link SocketObject}
     */
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
            throw new RemoteException("Unable to send the socket object");
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link GameController#sendMessage(ClientInterface, Message)} on receiver
     * @param client     The reference of the client who is sending the message
     * @param newMessage The message info
     * @throws RemoteException if fails to send the {@link SocketObject}
     */
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
            throw new RemoteException("Unable to send the socket object");
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link ServerInterface#joinGame(ClientInterface, LoginInfo)} on receiver
     * @param client The client asking to join
     * @param info   The join-info
     * @throws RemoteException if fails to send the {@link SocketObject}
     */
    @Override
    public void joinGame(ClientInterface client, LoginInfo info) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ServerInterface) receiver).joinGame((ClientInterface) sender, info);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link ServerInterface#leaveGame(ClientInterface)} on receiver
     * @param client The client asking to leave
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
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
            throw new RemoteException("Unable to send the socket object");
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link ServerInterface#createGame(ClientInterface, NewGameInfo)} on receiver
     * @param client The client asking to create the game
     * @param info   The new-game info
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void createGame(ClientInterface client, NewGameInfo info) throws RemoteException{
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ServerInterface) receiver).createGame((ClientInterface) sender, info);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }
    }
}
