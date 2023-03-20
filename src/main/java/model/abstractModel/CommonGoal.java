package model.abstractModel;

import model.Token;

import java.util.Stack;

public abstract class CommonGoal {
    protected Stack<Token> tokenStack;
    public abstract void fillStack();
    public abstract GoalEvaluator getEvaluator();
    public abstract Token getTopToken();
    public abstract Token popToken();
}
