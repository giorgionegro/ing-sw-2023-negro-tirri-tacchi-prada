package distibuted.networkObservers;

import model.abstractModel.Shelf;
import modelView.ShelfInfo;
import util.Observer;
import view.interfaces.ShelfView;

import java.rmi.RemoteException;

public class ShelfNetworkObserver implements Observer<Shelf, Shelf.Event> {

    private final String playerId;
    private final ShelfView view;
    public ShelfNetworkObserver(ShelfView view, String playerId) {
        this.view = view;
        this.playerId = playerId;
    }

    @Override
    public void update(Shelf s, Shelf.Event arg) {
        try{
            if (arg == null) {
                view.update(new ShelfInfo(this.playerId, s.getShelf()), arg);
            } else if (arg.equals(Shelf.Event.SHELF_MODIFIED)) {
                view.update(new ShelfInfo(this.playerId, s.getShelf()), arg);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
