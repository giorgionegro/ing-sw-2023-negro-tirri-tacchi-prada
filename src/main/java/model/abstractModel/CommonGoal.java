package model.abstractModel;

import model.Token;
import util.Observable;

import java.util.Stack;

/**
 * This class is the abstract class for the goals,
 * It contains all the necessary methods for the common goals
 **/
public abstract class CommonGoal extends Observable<CommonGoal.CommonGoalEvent> {

    /**
     * This enumeration contains all the goal events that can be sent to observers
     */
    public enum CommonGoalEvent{
        /**
         * This event is sent whenever a token is picked from tokenStack
         */
        TOKEN_PICKED
    }

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
     * This method initialize the tokenStack
     * @param nToken number of token to add to tokenStack
     */
    protected abstract void fillStack(int nToken);
    /**
     * This method returns the goal evaluator
     * @return the goal evaluator
     **/
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
