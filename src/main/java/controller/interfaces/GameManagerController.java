package controller.interfaces;

import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;

public interface GameManagerController {
    GameController getGame(String gameId) throws GameNotExistsException;
    void createGame(String gameId, int playerNumber) throws GameAlreadyExistsException;
}
