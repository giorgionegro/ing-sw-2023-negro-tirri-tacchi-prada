package distibuted.networkObservers;

import model.abstractModel.GamesManager;
import modelView.GamesManagerInfo;
import util.Observer;
import view.interfaces.GamesManagerView;

import java.rmi.RemoteException;

public class GamesManagerNetworkObserver implements Observer<GamesManager, GamesManager.GamesManagerEvent> {

    GamesManagerView view;
    public GamesManagerNetworkObserver(GamesManagerView view) {this.view = view;
    }

    @Override
    public void update(GamesManager o, GamesManager.GamesManagerEvent arg) {
        try {
            if (arg == null) {

            } else {
                switch (arg) {
                    case GAME_CREATED -> view.update(new GamesManagerInfo("", 0, 0), GamesManager.GamesManagerEvent.GAME_CREATED); //TODO
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
