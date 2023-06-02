package controller.interfaces;

import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.NewGameInfo;

public interface GameManagerController {
    GameController getGame(String gameId) throws GameNotExistsException;
    void createGame(NewGameInfo newGameInfo) throws GameAlreadyExistsException, IllegalArgumentException;
}
