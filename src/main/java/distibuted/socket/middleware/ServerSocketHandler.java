package distibuted.socket.middleware;

import distibuted.ClientEndPoint;
import distibuted.interfaces.Binder;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import modelView.*;
import view.interfaces.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * This class manages a socket connection on server side
 */
public class ServerSocketHandler extends SocketHandler<ServerInterface> implements ClientInterface {

    /**
     * Class constructor, initialize a {@link SocketHandler} with the given socket
     * @param socket socket to manage
     */
    public ServerSocketHandler(Socket socket) {
        super(socket);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link CommonGoalView#update(CommonGoalInfo, CommonGoal.Event)} on receiver
     * @param o the commonGoal info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((CommonGoalView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link GameView#update(GameInfo, Game.Event)} on receiver
     * @param o the gameInfo
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((GameView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link LivingRoomView#update(LivingRoomInfo, LivingRoom.Event)} on receiver
     * @param o the livingRoom info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((LivingRoomView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link PersonalGoalView#update(PersonalGoalInfo, PersonalGoal.Event)} on receiver
     * @param o the personalGoal info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((PersonalGoalView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link PlayerChatView#update(PlayerChatInfo, PlayerChat.Event)} on receiver
     * @param o the playerChat info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((PlayerChatView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link ShelfView#update(ShelfInfo, Shelf.Event)} on receiver
     * @param o the shelf info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ShelfView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link UserView#update(UserInfo, User.Event)} on receiver
     * @param o the user info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((UserView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link PlayerView#update(PlayerInfo, Player.Event)} on receiver
     * @param o the player info
     * @param evt the event that has generated this update
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((PlayerView) receiver).update(o, evt);
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
     * Generates and send {@link SocketObject} that calls {@link Binder#bind(ServerInterface)} on receiver
     * <p>
     * Create and runs input-stream-handler thread
     * @param server the {@link ServerInterface} of the server that client have to bind to
     * @throws RemoteException
     */
    @Override
    public void bind(ServerInterface server) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {

                    ((Binder) receiver).bind((ServerInterface) sender);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }

        /* Create and runs input-stream-handler thread */
        new Thread(() -> {
            try {
                while (true) {
                    this.waitForReceive(server);
                }
            } catch (RemoteException e) {
                System.err.println("Cannot receive from client: " + e.getMessage() + ".\n-> Closing this connection...");
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Generates and send {@link SocketObject} that calls {@link Binder#ping()} on receiver
     * @throws RemoteException if fails to send a {@link SocketObject}
     */
    @Override
    public void ping() throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((ClientEndPoint) receiver).ping();
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            super.close();
            throw new RemoteException("Unable to send the socket object");
        }
    }


}
