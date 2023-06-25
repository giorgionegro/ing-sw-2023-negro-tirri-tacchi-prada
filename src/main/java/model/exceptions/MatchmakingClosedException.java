package model.exceptions;

/**
 * This class represents an error occurred when trying to join a Game that has already started
 */
public class MatchmakingClosedException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public MatchmakingClosedException(){
        super("Game matchmaking is closed");
    }
}
