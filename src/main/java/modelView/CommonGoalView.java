package modelView;

import model.Token;

import java.io.Serializable;

public class CommonGoalView implements Serializable {
    private final Token tokenState;
    private final String description;

    public CommonGoalView(String description, Token tokenState) {
        this.tokenState = tokenState;
        this.description = description;
    }

    public Token getTokenState() {
        return tokenState;
    }

    public String getDescription() {
        return description;
    }
}
