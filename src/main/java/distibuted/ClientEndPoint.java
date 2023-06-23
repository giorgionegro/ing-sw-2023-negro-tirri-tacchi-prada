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

public class ClientEndPoint extends UnicastRemoteObject implements ClientInterface {

    private final ViewCollection views;

    private final Consumer<String> disconnectionCallback;

    public ClientEndPoint(ViewCollection ui, Consumer<String> disconnectionCallback) throws RemoteException {
        super();
        this.disconnectionCallback = disconnectionCallback;
        this.views = ui;
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update LivingRoomInfo",e.getMessage());
        }
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update PersonalGoalInfo",e.getMessage());
        }
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update PlayerChatInfo",e.getMessage());
        }
    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update PlayerInfo",e.getMessage());
        }
    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update ShelfInfo",e.getMessage());
        }
    }

    @Override
    public void update(GameInfo o, Game.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update GameInfo",e.getMessage());
        }
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update CommonGoalInfo",e.getMessage());
        }
    }

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            printError("Update UserInfo",e.getMessage());
        }
    }
    private final TimedLock<Boolean> pingWaiter = new TimedLock<>(false);

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

    @Override
    public void ping() throws RemoteException {
        pingWaiter.notify(true);
    }

    private void printError(String from, String message){
        if(!message.isBlank())
            message = " : "+message;
        System.err.print("ClientEndpoint: exception from " + from + message);
    }


}
