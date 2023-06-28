package distibuted;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import modelView.*;
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
    private final Consumer<? super String> disconnectionCallback;

    /**
     * This class constructor creates an instance of this class, initialized with a given client views and a disconnection consumer
     * @param views collection of views
     * @param disconnectionCallback disconnection consumer
     * @throws RemoteException in case of an error occurred exporting this remote object
     */
    public ClientEndPoint(ViewCollection views, Consumer<? super String> disconnectionCallback) throws RemoteException {
        super();
        this.disconnectionCallback = disconnectionCallback;
        this.views = views;
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update LivingRoomInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update PersonalGoalInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update PlayerChatInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(PlayerInfo o, Player.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update PlayerInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(ShelfInfo o, Shelf.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update ShelfInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(GameInfo o, Game.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update GameInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update CommonGoalInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @param evt {@inheritDoc}
     */
    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        try {
            this.views.update(o,evt);
        } catch (RemoteException e) {
            this.printError("Update UserInfo",e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Starts a Thread that receives pings and calls disconnectionCallback if it stops receiving them
     * @param server a ServerInterface that represents the server the client will bind with
     * @throws RemoteException {@inheritDoc}
     */
    @Override
    public void bind(ServerInterface server) throws RemoteException {
        // start ping thread
        new Thread(() -> {
            /* This thread pings client every 1s */
            try {
                while (true) {
                    server.ping();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException | RemoteException e) {
                /* If an error occurred during a ping, then disconnect the client */
                this.disconnectionCallback.accept("Ping timeout");
            }
        }).start();
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
