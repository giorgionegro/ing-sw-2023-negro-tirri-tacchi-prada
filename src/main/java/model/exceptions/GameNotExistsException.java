package model.exceptions;

/**
 * This class represents an error occurred when trying to perform an action on a Game that has ended
 */
public class GameNotExistsException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public GameNotExistsException(){
        super("GameId doesn't exists");
    }
}
