package model;

import model.abstractModel.Game;
import model.abstractModel.GamesManager;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;

import java.util.HashMap;
import java.util.Map;

public class StandardGamesManager extends GamesManager{
    /**
     * Map that contains each Game and its gameId
     */
    private final Map<String, Game> games;

    /**
     * Construct a StandardGamesManager instance, initialized with an empty map of games
     */
    public StandardGamesManager(){
        this.games = new HashMap<>();
    }

    /**
     * this method adds a Game to the games Map if it is not yet contained in it, otherwise throws an exception
     * @param gameId String representing the id of a Game
     * @param newMatch Game to add to {@link #games} Map
     * @throws GameAlreadyExistsException if newMatch is already contained in the {@link #games} Map
     */
    @Override
    public void addGame(String gameId, Game newMatch) throws GameAlreadyExistsException {
        if(games.containsKey(gameId))
            throw new GameAlreadyExistsException();
        games.put(gameId, newMatch);

    }

    /**
     * this method returns a Game with id gameId if present in the {@link #games} Map
     * @param gameId String representing the id of a game
     * @return the Game which id is gameId
     * @throws GameNotExistsException if there is no Game with id gameId contained in the games Map
     */
    @Override
    public Game getGame(String gameId) throws GameNotExistsException {
        if(games.containsKey(gameId))
            return games.get(gameId);
        throw new GameNotExistsException();
    }

}
