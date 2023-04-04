package model.abstractModel;

import model.Token;

import java.util.Stack;

public abstract class CommonGoal {

    public CommonGoal(GoalEvaluator evaluator){
        this.tokenStack = new Stack<>();
        this.evaluator = evaluator;
    }
    protected Stack<Token> tokenStack;
    private final GoalEvaluator evaluator;

    protected abstract void fillStack(int nPlayers);
    public GoalEvaluator getEvaluator(){
        return evaluator;
    }
    public Token getTopToken(){
        return tokenStack.lastElement();
    }
    public Token popToken(){
        return tokenStack.pop();
    }
}
