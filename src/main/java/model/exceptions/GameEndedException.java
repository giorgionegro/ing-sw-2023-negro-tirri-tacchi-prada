package model.exceptions;

/**
 * This class represents an error occurred when trying to perform an action on a Game that has ended
 */
public class GameEndedException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public GameEndedException(){
        super("Game Ended");
    }
}
