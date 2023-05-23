package view.interfaces;

import java.rmi.Remote;

public interface ViewCollection extends Remote, CommonGoalView, GamesManagerView, LivingRoomView, PersonalGoalView,
        PlayerChatView, PlayerView, ShelfView, GameView, UserView{}
