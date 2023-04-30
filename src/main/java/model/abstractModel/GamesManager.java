package model.abstractModel;

import model.exceptions.*;
import util.Observable;


public abstract class GamesManager extends Observable<GamesManager.GamesManagerEvent>{

    /**
     * This enumeration contains all the games manager events that can be sent to observers
     */
    public enum GamesManagerEvent{
        GAME_CREATED
    }

    /**
     * This method adds a game to GamesManager if it is not yet contained in it, otherwise thros an exception
     * @param gameId String representing the id of a Game
     * @param newMatch Game to add to GamesManager
     * @throws GameAlreadyExistsException if the gameId is already contained in GamesManager
     */
    public abstract void addGame(String gameId, Game newMatch) throws GameAlreadyExistsException;

    /**
     * This method returns the Game with id gameId if present
     * @param gameId String representing the id of a game
     * @return the Game which id is gameId
     * @throws GameNotExistsException if there is no Game with id gameId is contained in the games manager
     */
    public abstract Game getGame(String gameId) throws GameNotExistsException;

}