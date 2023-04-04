package model;

import model.abstractModel.CommonGoal;
import model.abstractModel.GoalEvaluator;

public class StandardCommonGoal extends CommonGoal {
    public StandardCommonGoal(int nPlayer, GoalEvaluator evaluator) {
        super(evaluator);
        fillStack(nPlayer);
    }

    protected void fillStack(int nPlayers) {
        if(nPlayers<5){
            tokenStack.push(Token.TOKEN_2_POINTS);
            tokenStack.push(Token.TOKEN_4_POINTS);
            if(nPlayers>2){
                tokenStack.push(Token.TOKEN_6_POINTS);
                if(nPlayers>3){
                    tokenStack.push(Token.TOKEN_8_POINTS);
                }
            }
        }else{
            throw new UnsupportedOperationException("The number of players is more than the maximum accepted (4max)"); //TODO change error type
        }
    }
}
