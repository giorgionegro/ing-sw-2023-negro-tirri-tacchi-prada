package model;

import java.util.ArrayList;
import java.util.List;

public class CommonGoal {
    private CommonGoalType goalType;
    private List<Token> tokenStack;

    public CommonGoal(CommonGoalType goalType, int players){
        this.goalType = goalType;
        this.tokenStack = chargeStack(players);
    }

    private List<Token> chargeStack(int players){
        List<Token> temp = new ArrayList<>();

        temp.add(Token.TOKEN_8_POINTS);
        temp.add(Token.TOKEN_6_POINTS);
        if(players>2) temp.add(Token.TOKEN_4_POINTS);
        if(players>3) temp.add(Token.TOKEN_2_POINTS);

        return temp;
    }
}
