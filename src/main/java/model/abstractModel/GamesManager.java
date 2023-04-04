package model.abstractModel;

import model.exceptions.*;

import java.util.List;


public interface GamesManager {

    public void addGame(String gameId, Game newMatch) throws GameAlreadyExistsException;

    public List<Game> getGames();

    public Game getGame(String gameId) throws GameNotExistsException;
}