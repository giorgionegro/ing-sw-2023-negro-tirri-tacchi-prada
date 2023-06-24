package view.graphicInterfaces;

/**
 * This interface
 */
public interface GameGraphics extends LivingRoomGraphics, PlayerShelfGraphics, GameInfoGraphics, PlayerChatGraphics, CommonGoalGraphics, PlayerInfoGraphics, PersonalGoalGraphics {
    /**
     * @param playerId id of the player that owns the shelf
     */
    void resetGameGraphics(String playerId);
}
