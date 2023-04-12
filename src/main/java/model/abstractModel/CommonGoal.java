package model.abstractModel;

import model.Token;

import java.util.Stack;

/**
 * This class is the abstract class for the goals
 * It contains all the necessary methods for the common goals
 **/
public abstract class CommonGoal {

    /**
     * Constructor for the CommonGoal class
     * @param evaluator the goal evaluator
     **/
    public CommonGoal(GoalEvaluator evaluator){
        this.tokenStack = new Stack<>();
        this.evaluator = evaluator;
    }

    /**
     * The token stack, rappresenting the points achivable by the players
     */
    protected Stack<Token> tokenStack;
    /**
     * The goal evaluator, defining the goal description and the evaluation method
     */
    private final GoalEvaluator evaluator;
    /**
     * This method returns the goal evaluator
     * @return the goal evaluator
     **/

    protected abstract void fillStack(int nPlayers);
    public GoalEvaluator getEvaluator(){
        return evaluator;
    }
    /**
     * This method returns the top token of the token stack
     * @return the top token of the token stack
     **/
    public Token getTopToken(){
        return tokenStack.lastElement();
    }

    /**
     * This method pops the top token of the token stack
     * @return the top token of the token stack
     */
    public Token popToken(){
        return tokenStack.pop();
    }
}
