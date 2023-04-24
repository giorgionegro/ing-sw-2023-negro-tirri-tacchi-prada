package modelView;

import model.Token;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlayerInfo implements Serializable {

    private final String errorMessage;
    private final Map<String, Token> achievedCommonGoals;
    public PlayerInfo(String errorMessage, Map<String,Token> achievedCommonGoals){
        this.errorMessage = errorMessage;
        this.achievedCommonGoals = new HashMap<>(achievedCommonGoals);
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public Map<String,Token> getAchievedCommonGoals(){
        return achievedCommonGoals;
    }
}
