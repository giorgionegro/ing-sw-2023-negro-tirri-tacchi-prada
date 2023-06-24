package distibuted.socket.middleware;

import distibuted.ClientEndPoint;
import distibuted.interfaces.Binder;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.interfaces.SocketObject;
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
     * Class constructor
     * @param socket socket
     */
    public ServerSocketHandler(Socket socket) {
        super(socket);
    }

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

    @Override
    public void bind(ServerInterface server) throws RemoteException {
        try {
            this.send((SocketObject) (sender, receiver) -> {
                try {
                    ((Binder) receiver).bind(null);
                } catch (ClassCastException e) {
                    throw new RemoteException("Socket object not usable");
                }
            });
        } catch (IOException e) {
            throw new RemoteException("Unable to send the socket object");
        }

        while (true) {
            this.waitForReceive(server);
        }
    }

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
            throw new RemoteException("Unable to send the socket object");
        }
    }


}
