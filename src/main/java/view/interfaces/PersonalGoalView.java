package view.interfaces;

import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link PersonalGoal}
 */
@FunctionalInterface
public interface PersonalGoalView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the personalGoal info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException;
}
