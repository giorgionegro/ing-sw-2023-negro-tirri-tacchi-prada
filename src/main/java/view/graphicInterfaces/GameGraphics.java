package view.graphicInterfaces;

/**
 * This interface represents the graphics functionalities of the game
 */
public interface GameGraphics extends LivingRoomGraphics, PlayerShelfGraphics, GameInfoGraphics, PlayerChatGraphics, CommonGoalGraphics, PlayerInfoGraphics, PersonalGoalGraphics {
    /**
     * This method asks the game graphic to reset, in order to prepare for a new game
     * @param playerId the player ID used in the next game
     */
    void resetGameGraphics(String playerId);
}
