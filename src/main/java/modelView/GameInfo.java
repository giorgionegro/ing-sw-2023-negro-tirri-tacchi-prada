package modelView;

import model.abstractModel.Game;
import java.io.Serializable;
import java.util.Map;

/**
 * @param status - status of the game
 * @param lastTurn - true if the game is in the last turn
 * @param playerOnTurn - player on turn's id
 * @param points - points amount of each player
 */
public record GameInfo(Game.GameStatus status, boolean lastTurn, String playerOnTurn, Map<String,Integer> points) implements Serializable {}
