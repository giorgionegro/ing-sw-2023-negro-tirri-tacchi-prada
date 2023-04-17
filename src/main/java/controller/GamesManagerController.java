package controller;

import distibuted.interfaces.ClientInterface;
import model.GameInfo;
import model.abstractModel.Game;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;

import java.util.HashMap;
import java.util.Map;

public class GamesManagerController {

    private final Map<String,GameController> gameControllers;

    public GamesManagerController() {
        this.gameControllers = new HashMap<>();
    }

    public GameController getGameController(ClientInterface client, String gameId) {
        try {
            if(!gameControllers.containsKey(gameId))
                throw new GameNotExistsException();

            return gameControllers.get(gameId);

        } catch (GameNotExistsException e) {
            //TODO mandare errore alla view
        }

        return null;
    }

    public void createGame(ClientInterface client, GameInfo gameInfo) {
        try {
            if(gameControllers.containsKey(gameInfo.getGameId()))
                throw new GameAlreadyExistsException();

            Game newGame = Game.gameFactory(gameInfo.getType(), gameInfo.getGameId(), gameInfo.getPlayerNumber());

            gameControllers.put(gameInfo.getGameId(), new GameController(newGame));

        } catch (GameAlreadyExistsException e) {
            //TODO inviare errore alla view
        }
    }
}
