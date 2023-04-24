package distibuted.networkObservers;

import model.abstractModel.PlayerChat;
import modelView.PlayerChatInfo;
import util.Observer;
import view.interfaces.PlayerChatView;

import java.rmi.RemoteException;

public class PlayerChatNetworkObserver implements Observer<PlayerChat, PlayerChat.Event> {
    private final PlayerChatView view;
    public PlayerChatNetworkObserver(PlayerChatView view) {
        this.view = view;
    }

    @Override
    public void update(PlayerChat pC, PlayerChat.Event arg) {
        try {
            view.update(new PlayerChatInfo(pC),arg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
