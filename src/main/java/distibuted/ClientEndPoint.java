package distibuted;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import modelView.*;
import util.TimedLock;
import view.interfaces.ViewCollection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.Consumer;

/**
 * This class is an implementation of {@link ClientInterface} and represents the client endpoint for socket and RMI connections
 */
public class ClientEndPoint extends UnicastRemoteObject implements ClientInterface {

    /**
     * The client views
     */
    private final ViewCollection views;

    /**
     * Consumer called after the client fails to receive a ping
     */
    private final Consumer<String> disconnectionCallback;

    /**
     * This class constructor creates an instance of this class, initialized with a given client views and a disconnection consumer
     * @param views collection of views
     * @param disconnectionCallback disconnection consumer
     * @throws RemoteException in case of an error occurred exporting this remote object
     */
    public ClientEndPoint(ViewCollection views, Consumer<String> disconnectionCallback) throws RemoteException {
        super();
        this.disconnectionCallback = disconnectionCallback;
        this.views = views;
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update LivingRoomInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update PersonalGoalInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update PlayerChatInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(PlayerInfo o, Player.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update PlayerInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(ShelfInfo o, Shelf.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update ShelfInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(GameInfo o, Game.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update GameInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update CommonGoalInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o
     * @param evt
     */
    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update UserInfo",e.getMessage());
        }
    }
    private final TimedLock<Boolean> pingWaiter = new TimedLock<>(false);

    /**
     * {@inheritDoc}
     * <p>
     * Starts a Thread that receives pings and calls disconnectionCallback if it stops receiving them
     * @param server a ServerInterface that represents the server the client will bind with
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void bind(ServerInterface server) throws RemoteException {
       new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true)
            {
                pingWaiter.reset();
                pingWaiter.setValue(false);

                try {
                    this.pingWaiter.lock(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!pingWaiter.hasBeenNotified()) {
                    disconnectionCallback.accept("Connection lost: ping timeout");
                    break;
                }
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     * <p>
     * It notifies the ping handler Thread that a ping has been received
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void ping() throws RemoteException {
        pingWaiter.notify(true);
    }

    /**
     * This method prints an error that has occurred
     * @param from source of the error
     * @param message the message of the error
     */
    private void printError(String from, String message){
        if(!message.isBlank())
            message = " : "+message;
        System.err.print("ClientEndpoint: exception from " + from + message);
    }


}
