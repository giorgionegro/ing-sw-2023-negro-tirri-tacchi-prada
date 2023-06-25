package model.abstractModel;

import model.Token;
import modelView.CommonGoalInfo;
import util.Observable;

import java.util.Stack;

/**
 * This class is the abstract class for the goals,
 * <p>
 * It contains all the methods required to manage common goal information
 **/
public abstract class CommonGoal extends Observable<CommonGoal.Event> {

    /**
     * The token stack ({@link Stack} of {@link Token})
     */
    protected final Stack<Token> tokenStack;
    /**
     * The goal evaluator associated to this common goal
     */
    private final GoalEvaluator evaluator;

    /**
     * Constructor for the CommonGoal class with empty token stack, but initialized with the given Evaluator
     *
     * @param evaluator the goal evaluator
     **/
    public CommonGoal(GoalEvaluator evaluator) {
        super();
        this.tokenStack = new Stack<>();
        this.evaluator = evaluator;
    }

    /**
     * This method initializes {@link #tokenStack}
     * 
     * @param nToken number of token to add to tokenStack
     */
    protected abstract void fillStack(int nToken);

    /**
     * This method returns the goal evaluator
     *
     * @return the goal evaluator
     **/
    public GoalEvaluator getEvaluator() {
        return this.evaluator;
    }

    /**
     * This method returns the top token of {@link #tokenStack}
     *
     * @return the top token of {@link #tokenStack}
     **/
    public Token getTopToken() {
        if (this.tokenStack.size() > 0)
            return this.tokenStack.lastElement();

        return Token.TOKEN_EMPTY;
    }

    /**
     * This method pops the top token of {@link #tokenStack}
     *
     * @return the top token of {@link #tokenStack}
     */
    public Token popToken() {
        Token t = this.tokenStack.pop();
        this.setChanged();
        this.notifyObservers(Event.TOKEN_PICKED);
        return t;
    }

    /**
     * This method returns an {@link CommonGoalInfo} representing this object instance
     *
     * @return An {@link CommonGoalInfo} representing this object instance
     */
    public CommonGoalInfo getInfo() {
        return new CommonGoalInfo(this.evaluator.getId(), this.evaluator.getDescription(), this.getTopToken());
    }

    /**
     * This enumeration contains all the goal events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent whenever a token is picked from tokenStack
         */
        TOKEN_PICKED
    }
}
