package view.graphicInterfaces;

import model.abstractModel.Game;

import java.util.Map;
/**
 * This interface represents the graphics for the current match.
 * It provides a method to update the information of the match when the users are playing the game.
 */
public interface GameInfoGraphics {
    /**This method updates the information of the match with the specified parameters:
     @param status status of the game
     @param playerOnTurn id of the player on turn
     @param isLastTurn true if the game is in the last round of turns
     @param pointsValues points amount of each player
     */
    void updateGameInfoGraphics(Game.GameStatus status, String playerOnTurn, boolean isLastTurn, Map<String,Integer> pointsValues);
}
