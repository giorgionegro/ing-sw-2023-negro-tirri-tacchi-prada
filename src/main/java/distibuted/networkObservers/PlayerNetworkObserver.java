package distibuted.networkObservers;

import model.abstractModel.Player;
import modelView.PlayerInfo;
import util.Observer;
import view.interfaces.PlayerView;

import java.net.NetworkInterface;
import java.rmi.RemoteException;

public class PlayerNetworkObserver implements Observer<Player,Player.PlayerEvent> {

    PlayerView view;
    public PlayerNetworkObserver(PlayerView view) {
        this.view = view;
    }

    @Override
    public void update(Player o, Player.PlayerEvent arg) {
        try {
            if (arg == null) {

            } else {
                switch (arg) {
                    case MALFORMED_MOVE -> view.update(new PlayerInfo("Malformed Move"), arg);
                    case NOT_ALLOWED -> view.update(new PlayerInfo("Not allowed"),arg);
                    case COMMONGOAL_ACHIEVED -> view.update(new PlayerInfo("Achieved a common goal"),arg);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
