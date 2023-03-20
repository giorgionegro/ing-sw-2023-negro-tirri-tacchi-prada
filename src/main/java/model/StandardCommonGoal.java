package model;

import model.abstractModel.CommonGoal;
import model.abstractModel.GoalEvaluator;

public class StandardCommonGoal extends CommonGoal {
    @Override
    public void fillStack() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public GoalEvaluator getEvaluator() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Token getTopToken() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Token popToken() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
