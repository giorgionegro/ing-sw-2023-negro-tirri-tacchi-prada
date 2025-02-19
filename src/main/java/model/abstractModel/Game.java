package model.abstractModel;

import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import modelView.GameInfo;
import util.Observable;

import java.util.List;

/**
 * This is the abstract class for games.
 * <p>
 * It defines all the required methods needed to access game components and status and to manage game's turn sequence.
 */
public abstract class Game extends Observable<Game.Event>{
    /**
     * This enumeration contains all the game events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent whenever a player joins the game
         */
        PLAYER_JOINED,
        /**
         * This event is sent when the game has just been started
         */
        GAME_STARTED,
        /**
         * This event is sent whenever game routine has been temporary suspended
         */
        GAME_SUSPENDED,
        /**
         * This event is sent whenever game routine has been resumed
         */
        GAME_RESUMED,
        /**
         * This event is sent whenever a player takes turn to the next player
         */
        NEXT_TURN,
        /**
         * This event is sent when a player complete his shelf, this event signal this would be the last round of turns
         */
        LAST_TURN,
        /**
         * This event is sent when the game is ended
         */
        GAME_ENDED

    }

    /**
     * This enumeration represents all the statuses of the game
     */
    public enum GameStatus{
        /**
         * This status is set when the game is in matchmaking phase
         */
        MATCHMAKING,
        /**
         * This status is set when the game is started
         */
        STARTED,
        /**
         * This status is set when the game is suspended
         */
        SUSPENDED,
        /**
         * This status is set when the game is ended
         */
        ENDED
    }

    /**
     * This method returns the current game status
     * @return the current game status
     */
    public abstract GameStatus getGameStatus();

    /**
     * This method signal the game to end and close
     */
    public abstract void close();

    /**
     * This method marks the current turn sequence to be the last one.
     * <p>
     * If invoked then isLastTurn method will return {@code true}
     */
    public abstract void setLastTurn();

    /**
     * This method returns {@code true} if the current turn sequence is the last one
     * @return {@code true} if the current turn sequence is the last one
     */
    public abstract boolean isLastTurn();

    /**
     * This method returns the playerId of the player that has currently the turn.
     * @return {@code if(getGameStatus().equals(GameStatus.STARTED)} the playerId of the player that has currently the turn, empty otherwise
     */
    public abstract String getTurnPlayerId();

    /**
     * This method gives the turn to the next player
     * @throws GameEndedException if turn can't be given to the next player because game is ended
     */
    public abstract void updatePlayersTurn() throws GameEndedException;

    /**
     * This method adds a new Player to game, the new Player is referenced by playerId. After this method has been
     * invoked with success the same Player can be obtained via {@code getPlayer(playerId)}
     * @param playerId the new id that has to be associated to the new Player
     * @throws PlayerAlreadyExistsException when there is already a Player associated to {@code playerId}
     * @throws MatchmakingClosedException when not {@code getGameStatus().equals(GameStatus.MATCHMAKING)}
     */
    public abstract void addPlayer(String playerId) throws PlayerAlreadyExistsException, MatchmakingClosedException;

    /**
     * This method removes a Player from the game, the Player is referenced by playerId. After this method has been
     * invoked with success then {@code getPlayer(playerId)} will throw {@link PlayerNotExistsException}
     * @param playerId the id of the player that needs to be removed
     * @throws PlayerNotExistsException if there is no Player associated to {@code playerId}
     */
    public abstract void removePlayer(String playerId) throws PlayerNotExistsException;

    /**
     * This method returns the Player associated to the playerId
     * @param playerId the id of the Player requested
     * @return the Player associated to the playerId
     * @throws PlayerNotExistsException if there is no Player associated to playerId
     */
    public abstract Player getPlayer(String playerId) throws PlayerNotExistsException;

    /**
     * This method returns a copy of the list of common goals of the game
     * @return a copy of the list of common goals of the game
     */
    public abstract List<CommonGoal> getCommonGoals();

    /**
     * This method returns the living room of the game
     * @return the living room of the game
     */
    public abstract LivingRoom getLivingRoom();

    /**
     * This method returns a {@link GameInfo} representing this object instance
     * @return A {@link GameInfo} representing this object instance
     */
    public abstract GameInfo getInfo();
}