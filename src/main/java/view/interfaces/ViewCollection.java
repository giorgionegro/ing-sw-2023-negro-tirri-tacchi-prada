package view.interfaces;

import java.rmi.Remote;

/**
 * This is a collection of interfaces that contains all the necessary methods to update the application view
 */
public interface ViewCollection extends Remote, CommonGoalView, LivingRoomView, PersonalGoalView,
        PlayerChatView, PlayerView, ShelfView, GameView, UserView{}
