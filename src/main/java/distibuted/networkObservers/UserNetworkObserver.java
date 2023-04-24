package distibuted.networkObservers;

import model.User;
import model.abstractModel.Shelf;
import modelView.UserInfo;
import util.Observer;
import view.interfaces.UserView;

import java.rmi.RemoteException;

public class UserNetworkObserver implements Observer<User, User.Event> {

    private final UserView view;

    public UserNetworkObserver(UserView view) {
        this.view = view;
    }
    @Override
    public void update(User o, User.Event arg) {
        try{
            if (arg == null) {
                view.update(new UserInfo(o), arg);
            } else if (arg.equals(Shelf.Event.SHELF_MODIFIED)) {
                view.update(new UserInfo(o), arg);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
