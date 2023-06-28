package model;

import model.abstractModel.CommonGoal;
import model.abstractModel.GoalEvaluator;

/**
 * This class is an implementation of {@link CommonGoal}
 * <p>
 * It initializes the {@link #tokenStack} with as many tokens as players.
 */
public class StandardCommonGoal extends CommonGoal {

    /**
     * Construct a {@link StandardCommonGoal} with the given evaluator
     * @param nPlayer the number of player of the game
     * @param evaluator the evaluator of the common goal
     */
    public StandardCommonGoal(int nPlayer, GoalEvaluator evaluator) {
        super(evaluator);
        this.fillStack(nPlayer);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation algorithm provides the following token value sequence: 2 -> 4 -> 6 -> 8
     * @param nToken number of token to add to tokenStack
     */
    @Override
    protected void fillStack(int nToken) {
        if(nToken<5){
            if(nToken>3)
                this.tokenStack.push(Token.TOKEN_2_POINTS);
            this.tokenStack.push(Token.TOKEN_4_POINTS);
            if(nToken>2)
                this.tokenStack.push(Token.TOKEN_6_POINTS);
            this.tokenStack.push(Token.TOKEN_8_POINTS);
        }else{
            throw new UnsupportedOperationException("The number of players is more than the maximum accepted (4max)");
        }
    }
}
