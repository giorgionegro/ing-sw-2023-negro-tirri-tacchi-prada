package controller.interfaces;

import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.NewGameInfo;

/**
 * This is the interface of a game manager controller
 * <p>
 * It defines all the methods needed to manage game controllers
 */
public interface GameManagerController {
    /**
     * This method returns a {@link GameController} associated with the given ID, the returned {@link GameController}
     * has been built during a {@link #createGame(NewGameInfo)} call and has been associated to the given {@link NewGameInfo} gameID
     * @param gameId the ID of the game
     * @return the {@link GameController} associated with the given ID
     * @throws GameNotExistsException if there is no {@link GameController} associated with the given ID
     */
    GameController getGame(String gameId) throws GameNotExistsException;

    /**
     * This method builds a {@link GameController} on the given new-game info
     * @param newGameInfo the new-game info that needs to be used
     * @throws GameAlreadyExistsException if there already is an {@link GameController} associated with the given {@link NewGameInfo} gameID
     * @throws IllegalArgumentException if the given {@link NewGameInfo} contains illegal or unknown parameters
     */
    void createGame(NewGameInfo newGameInfo) throws GameAlreadyExistsException, IllegalArgumentException;
}
