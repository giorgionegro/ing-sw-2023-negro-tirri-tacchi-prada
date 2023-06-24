package view.interfaces;

import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface PersonalGoalView extends Remote {
    void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException;
}
