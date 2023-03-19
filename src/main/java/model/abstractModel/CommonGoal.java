package model.abstractModel;

import model.Token;

import java.util.Stack;

public abstract class CommonGoal {
    protected Stack<Token> tokenStack;
    public abstract iGoalEvaluator getEvaluator();
    public abstract Token getToken();
}
