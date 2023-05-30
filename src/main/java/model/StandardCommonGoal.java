package model;

import model.abstractModel.CommonGoal;
import model.abstractModel.GoalEvaluator;
import model.instances.StandardCommonGoalInstance;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * This class is an implementation of {@link CommonGoal}
 * <p>
 * It initializes the {@link #tokenStack} with as much token as player.
 */
public class StandardCommonGoal extends CommonGoal {

    /**
     * Construct a {@link StandardCommonGoal} with the given evaluator
     * @param nPlayer the number of player of the game
     * @param evaluator the evaluator of the common goal
     */
    public StandardCommonGoal(int nPlayer, GoalEvaluator evaluator) {
        super(evaluator);
        fillStack(nPlayer);
    }

    /**
     * Construct a {@link StandardCommonGoal} using the given instance
     * @param instance the {@link StandardCommonGoal} instance
     */
    public StandardCommonGoal(@NotNull StandardCommonGoalInstance instance){
        super(instance.evaluator());
        List<Token> tokenList = new java.util.ArrayList<>(instance.tokenStack().stream().toList());
        Collections.reverse(tokenList);
        tokenList.forEach(tokenStack::push);
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
                tokenStack.push(Token.TOKEN_2_POINTS);
            tokenStack.push(Token.TOKEN_4_POINTS);
            if(nToken>2)
                tokenStack.push(Token.TOKEN_6_POINTS);
            tokenStack.push(Token.TOKEN_8_POINTS);
        }else{
            throw new UnsupportedOperationException("The number of players is more than the maximum accepted (4max)");
        }
    }

    /**
     * {@inheritDoc}
     * @return A {@link StandardCommonGoalInstance} constructed using instance values
     */
    @Override
    public @NotNull Serializable getInstance() {
        return new StandardCommonGoalInstance(getEvaluator(),tokenStack);
    }
}
