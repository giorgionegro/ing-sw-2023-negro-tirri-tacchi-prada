package distibuted.networkObservers;

import model.User;
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
            view.update(new UserInfo(o),arg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
