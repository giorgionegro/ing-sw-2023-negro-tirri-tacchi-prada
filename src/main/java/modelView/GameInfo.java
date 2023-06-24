package modelView;

import model.abstractModel.Game;
import java.io.Serializable;
import java.util.Map;

/**
 * This record contains information about the status of a {@link model.abstractModel.Game}
 * @param status status of the game
 * @param lastTurn true if the game is in the last round of turns
 * @param firstTurnPlayer id of the player that has the first turn
 * @param playerOnTurn id of the player on turn
 * @param points points amount of each player
 */
public record GameInfo(Game.GameStatus status, boolean lastTurn, String firstTurnPlayer, String playerOnTurn, Map<String,Integer> points) implements Serializable {}
