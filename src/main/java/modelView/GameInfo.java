package modelView;

import model.abstractModel.Game;
import java.io.Serializable;

/**
 * @param status - status of the game
 * @param lastTurn - true if the game is in the last turn
 * @param playerOnTurn - player on turn's id
 */
public record GameInfo(Game.GameStatus status, boolean lastTurn, String playerOnTurn) implements Serializable {}
