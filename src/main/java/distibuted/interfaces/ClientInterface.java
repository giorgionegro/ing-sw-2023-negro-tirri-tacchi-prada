package distibuted.interfaces;

import modelView.*;

public interface ClientInterface{
    void updateGameStatus(GameView o, Object arg);
    void updateCommonGoal(CommonGoalView o, Object arg);
    void updateShelf(ShelfView o, Object arg);
    void updateLivingRoom(LivingRoomView o, Object arg);
    void updatePlayerChat(PlayerChatView o, Object arg);
    void updatePersonalGoal(PersonalGoalView o, Object arg);
}
