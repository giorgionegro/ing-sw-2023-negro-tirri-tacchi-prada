package model.abstractModel;

import model.Token;
import modelView.CommonGoalInfo;
import org.jetbrains.annotations.NotNull;
import util.Observable;

import java.util.Stack;

/**
 * This class is the abstract class for the goals,
 * <p>
 * It contains all the methods required to manage common goal information
 **/
public abstract class CommonGoal extends Observable<CommonGoal.Event>{

    /**
     * This enumeration contains all the goal events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent whenever a token is picked from tokenStack
         */
        TOKEN_PICKED
    }

    /**
     * Constructor for the CommonGoal class with empty token stack, but initialized with the given Evaluator
     * @param evaluator the goal evaluator
     **/
    public CommonGoal(GoalEvaluator evaluator){
        this.tokenStack = new Stack<>();
        this.evaluator = evaluator;
    }

    /**
     * The token stack ({@link Stack} of {@link Token})
     */
    protected final @NotNull Stack<Token> tokenStack;
    /**
     * The goal evaluator associated to this common goal
     */
    private final GoalEvaluator evaluator;

    /**
     * This method initialize {@link #tokenStack}
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
     * This method returns the top token of {@link #tokenStack}
     * @return the top token of {@link #tokenStack}
     **/
    public Token getTopToken(){
        if(tokenStack.size()>0)
            return tokenStack.lastElement();

        return Token.TOKEN_EMPTY;
    }

    /**
     * This method pops the top token of {@link #tokenStack}
     * @return the top token of {@link #tokenStack}
     */
    public Token popToken(){
        Token t = tokenStack.pop();
        setChanged();
        notifyObservers(Event.TOKEN_PICKED);
        return t;
    }

    /**
     * This method returns an {@link CommonGoalInfo} representing this object instance
     * @return An {@link CommonGoalInfo} representing this object instance
     */
    public @NotNull CommonGoalInfo getInfo(){
        return new CommonGoalInfo(evaluator.getId(), evaluator.getDescription(), getTopToken());
    }
}
