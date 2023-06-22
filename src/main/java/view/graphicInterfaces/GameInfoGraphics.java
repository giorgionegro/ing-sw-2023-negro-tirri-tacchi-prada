package view.graphicInterfaces;

import model.abstractModel.Game;

import java.util.Map;

public interface GameInfoGraphics {
    void updateGameInfoGraphics(Game.GameStatus status, String playerOnTurn, boolean isLastTurn, Map<String,Integer> pointsValues);
}
