package modelView;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private String playerId;
    private String gameId;

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
