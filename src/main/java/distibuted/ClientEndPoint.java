package distibuted;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import modelView.*;
import view.interfaces.ViewCollection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientEndPoint extends UnicastRemoteObject implements ClientInterface {

    ViewCollection views;
    public ClientEndPoint(ViewCollection ui) throws RemoteException {
        super();
        this.views = ui;
    }

    @Override
    public void update(GamesManagerInfo o, GamesManager.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(GameInfo o, Game.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        try {
            views.update(o,evt);
        } catch (RemoteException e) {
            //TODO
        }
    }

    @Override
    public void bind(ServerInterface server) throws RemoteException {
        Thread binder = new Thread(() -> {
            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    //TODO
                }
            }
        });

        binder.start();

        try {
            binder.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);//TODO
        }
    }
}
