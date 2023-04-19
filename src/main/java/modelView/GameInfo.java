package modelView;

import model.abstractModel.Game;
import java.io.Serializable;

public class GameInfo implements Serializable {
    private final Game.GameStatus status;
    private final boolean lastTurn;
    private final String playerOnTurn;

    public GameInfo(Game.GameStatus status, boolean lastTurn, String playerOnTurn) {
        this.status = status;
        this.lastTurn = lastTurn;
        this.playerOnTurn = playerOnTurn;
    }

    public Game.GameStatus getStatus() {
        return status;
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    public String getPlayerOnTurn() {
        return playerOnTurn;
    }
}
