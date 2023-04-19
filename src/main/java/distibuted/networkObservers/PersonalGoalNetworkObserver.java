package distibuted.networkObservers;

import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;
import util.Observer;
import view.interfaces.PersonalGoalView;

import java.rmi.RemoteException;

public class PersonalGoalNetworkObserver implements Observer<PersonalGoal,PersonalGoal.PersonalGoalEvent> {

    PersonalGoalView view;
    public PersonalGoalNetworkObserver(PersonalGoalView view) {
        this.view = view;
    }

    @Override
    public void update(PersonalGoal pG, PersonalGoal.PersonalGoalEvent arg) {
        try {
            if (arg == null) {
                view.update(new PersonalGoalInfo(pG.isAchieved(), pG.getDescription()), arg);
            } else {
                switch (arg) {
                    case GOAL_ACHIEVED -> view.update(new PersonalGoalInfo(pG.isAchieved(), pG.getDescription()), arg);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
