package view.graphicInterfaces;

/**
 * This interface ?? TODO
 */
public interface GameGraphics extends LivingRoomGraphics, PlayerShelfGraphics, GameInfoGraphics, PlayerChatGraphics, CommonGoalGraphics, PlayerInfoGraphics, PersonalGoalGraphics {
    /**?? TODO
     * @param playerId id of the player that owns the shelf
     */
    void resetGameGraphics(String playerId);
}
