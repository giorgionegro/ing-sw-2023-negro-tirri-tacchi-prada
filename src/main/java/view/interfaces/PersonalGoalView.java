package view.interfaces;

import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PersonalGoalView extends Remote {
    void update(PersonalGoalInfo o, PersonalGoal.PersonalGoalEvent evt) throws RemoteException;
}
