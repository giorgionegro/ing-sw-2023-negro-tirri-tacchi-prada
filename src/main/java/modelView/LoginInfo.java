package modelView;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private final String playerId;
    private final String gameId;

    public LoginInfo(String playerId, String gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getGameId() {
        return gameId;
    }
}
