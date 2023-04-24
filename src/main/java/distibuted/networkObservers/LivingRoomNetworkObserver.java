package distibuted.networkObservers;

import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;
import util.Observer;
import view.interfaces.LivingRoomView;

import java.rmi.RemoteException;

public class LivingRoomNetworkObserver implements Observer<LivingRoom, LivingRoom.Event> {

    private final LivingRoomView view;

    public LivingRoomNetworkObserver(LivingRoomView view) {
        this.view = view;
    }

    @Override
    public void update(LivingRoom lR, LivingRoom.Event arg) {
        try {
            if (arg == null) {
                view.update(new LivingRoomInfo(lR.getBoard()), arg);
            } else {
                switch (arg) {
                    case BOARD_MODIFIED -> view.update(new LivingRoomInfo(lR.getBoard()), arg);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
