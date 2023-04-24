package distibuted.networkObservers;

import model.abstractModel.Player;
import modelView.PlayerInfo;
import util.Observer;
import view.interfaces.PlayerView;

import java.rmi.RemoteException;

public class PlayerNetworkObserver implements Observer<Player, Player.Event> {

    private final PlayerView view;
    public PlayerNetworkObserver(PlayerView view) {
        this.view = view;
    }

    @Override
    public void update(Player o, Player.Event arg) {
        try {
            view.update(new PlayerInfo(o.getReportedError(),o.getAchievedCommonGoals()),arg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
