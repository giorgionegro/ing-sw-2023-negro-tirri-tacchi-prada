package controller;

import model.exceptions.GameNotExistsException;
import modelView.NewGameInfo;
import model.StandardGame;
import model.abstractModel.Game;
import model.exceptions.GameAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public class GamesManagerController {

    private final Map<String, StandardGameController> gameControllers;

    public GamesManagerController() {
        this.gameControllers = new HashMap<>();
    }

    public StandardGameController getGameController(String gameId) throws GameNotExistsException{
        if(!gameControllers.containsKey(gameId))
            throw new GameNotExistsException();

        return gameControllers.get(gameId);
    }

    public void createGame(NewGameInfo gameInfo) throws GameAlreadyExistsException, IllegalArgumentException {
            if(gameControllers.containsKey(gameInfo.getGameId()))
                throw new GameAlreadyExistsException();

            Game newGame = new StandardGame(gameInfo.getGameId(), gameInfo.getPlayerNumber());

            gameControllers.put(gameInfo.getGameId(), new StandardGameController(newGame));
    }
}
