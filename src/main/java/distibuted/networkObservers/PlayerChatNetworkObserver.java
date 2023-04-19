package distibuted.networkObservers;

import model.abstractModel.PlayerChat;
import modelView.PlayerChatInfo;
import util.Observer;
import view.interfaces.PlayerChatView;

import java.rmi.RemoteException;

public class PlayerChatNetworkObserver implements Observer<PlayerChat, PlayerChat.PlayerChatEvent> {
    private final PlayerChatView view;
    public PlayerChatNetworkObserver(PlayerChatView view) {
        this.view = view;
    }

    @Override
    public void update(PlayerChat pC, PlayerChat.PlayerChatEvent arg) {
        try {
            if (arg == null) {
                view.update(new PlayerChatInfo(pC), arg);
            } else {
                switch (arg) {
                    case MESSAGE_RECEIVED -> view.update(new PlayerChatInfo(pC), arg); //TODO switch with event
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
