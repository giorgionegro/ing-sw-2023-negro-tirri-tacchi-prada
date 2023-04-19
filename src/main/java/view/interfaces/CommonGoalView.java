package view.interfaces;

import model.abstractModel.CommonGoal;
import modelView.CommonGoalInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommonGoalView extends Remote {
    void update(CommonGoalInfo o, CommonGoal.CommonGoalEvent evt) throws RemoteException;
}
