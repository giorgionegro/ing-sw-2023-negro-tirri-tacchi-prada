package view.graphicInterfaces;

public interface GameGraphics extends LivingRoomGraphics, PlayerShelfGraphics, GameInfoGraphics, PlayerChatGraphics, CommonGoalGraphics, PlayerInfoGraphics, PersonalGoalGraphics {
    void resetGameGraphics(String playerId);
}
