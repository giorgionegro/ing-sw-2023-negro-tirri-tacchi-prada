package view.interfaces;

import java.rmi.Remote;

public interface ViewCollection extends Remote, CommonGoalView, LivingRoomView, PersonalGoalView,
        PlayerChatView, PlayerView, ShelfView, GameView, UserView{}
