package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.CommonGoal;
import modelView.CommonGoalInfo;
import view.interfaces.CommonGoalView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketCommonGoalInfo implements SocketObject, Serializable {

    private final CommonGoalInfo info;
    private final CommonGoal.Event evt;

    public SocketCommonGoalInfo(CommonGoalInfo info, CommonGoal.Event evt) {
        this.info = info;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try {
            ((CommonGoalView) receiver).update(info, evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
