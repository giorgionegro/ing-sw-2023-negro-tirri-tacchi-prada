package model.abstractModel;

import model.exceptions.*;
import util.Observable;


public abstract class GamesManager extends Observable<GamesManager.Event>{

    public enum Event {
        GAME_CREATED
    }

    public abstract void addGame(String gameId, Game newMatch) throws GameAlreadyExistsException;

    public abstract Game getGame(String gameId) throws GameNotExistsException;

}