package model;

import model.abstractModel.Game;
import model.abstractModel.GamesManager;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardGamesManager implements GamesManager {

    private Map<String, Game> games;

    public StandardGamesManager(){
        this.games = new HashMap<>();
    }

    @Override
    public void addGame(String gameId, Game newMatch) throws GameAlreadyExistsException {
        if(games.containsKey(gameId))
            throw new GameAlreadyExistsException();
        games.put(gameId, newMatch);
    }

    @Override
    public List<Game> getGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public Game getGame(String gameId) throws GameNotExistsException {
        if(games.containsKey(gameId))
            return games.get(gameId);
        throw new GameNotExistsException();
    }
}
