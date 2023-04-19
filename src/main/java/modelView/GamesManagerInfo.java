package modelView;

import java.io.Serializable;

public class GamesManagerInfo implements Serializable {

    private final String gameId;
    private final int maxPlayers;
    private final int connectedPlayers;

    public GamesManagerInfo(String gameId, int maxPlayers, int connectedPlayers) {
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.connectedPlayers = connectedPlayers;
    }

    public String getGameId() {
        return gameId;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }
}
