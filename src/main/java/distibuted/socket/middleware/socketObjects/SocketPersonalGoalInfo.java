package distibuted.socket.middleware.socketObjects;

import distibuted.socket.middleware.interfaces.SocketObject;
import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;
import view.interfaces.PersonalGoalView;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SocketPersonalGoalInfo implements SocketObject, Serializable {
    private final PersonalGoalInfo info;
    private final PersonalGoal.Event evt;

    public SocketPersonalGoalInfo(PersonalGoalInfo info, PersonalGoal.Event evt) {
        this.info = info;
        this.evt = evt;
    }

    @Override
    public void update(Object sender, Object receiver) throws RemoteException {
        try{
            ((PersonalGoalView) receiver).update(info, evt);
        }catch (ClassCastException e){
            throw new RemoteException("Socket object not usable");
        }
    }
}
