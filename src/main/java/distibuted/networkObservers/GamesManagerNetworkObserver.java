package distibuted.networkObservers;

import model.abstractModel.GamesManager;
import util.Observer;
import view.interfaces.GamesManagerView;

public class GamesManagerNetworkObserver implements Observer<GamesManager, GamesManager.Event> {

    private final GamesManagerView view;
    public GamesManagerNetworkObserver(GamesManagerView view) {this.view = view;
    }

    @Override
    public void update(GamesManager o, GamesManager.Event arg) {
//        try {
//            //
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
    }
}
