package view.interfaces;

import model.abstractModel.CommonGoal;
import modelView.CommonGoalInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the view interface of a {@link CommonGoal}
 */
@FunctionalInterface
public interface CommonGoalView extends Remote {
    /**
     * This method updates the view based on given info and event
     * @param o the commonGoal info
     * @param evt the event that has generated this update
     * @throws RemoteException if an error occurred reaching the remote object
     */
    void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException;
}
