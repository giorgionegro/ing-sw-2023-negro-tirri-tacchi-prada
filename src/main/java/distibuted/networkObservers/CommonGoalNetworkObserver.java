package distibuted.networkObservers;

import model.abstractModel.CommonGoal;
import modelView.CommonGoalInfo;
import util.Observer;
import view.interfaces.CommonGoalView;

import java.rmi.RemoteException;

public class CommonGoalNetworkObserver implements Observer<CommonGoal, CommonGoal.CommonGoalEvent> {
    CommonGoalView view;
    public CommonGoalNetworkObserver(CommonGoalView view)
    {
        this.view = view;
    }

    @Override
    public void update(CommonGoal g, CommonGoal.CommonGoalEvent evt) {
        try{
            if (evt == null) {
                view.update(new CommonGoalInfo(g.getEvaluator().getDescription(), g.getTopToken()), evt);
            } else {
                switch (evt) {
                    case TOKEN_PICKED ->
                            view.update(new CommonGoalInfo(g.getEvaluator().getDescription(), g.getTopToken()), evt);
                    default ->
                            view.update(new CommonGoalInfo(g.getEvaluator().getDescription(), g.getTopToken()), evt);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
