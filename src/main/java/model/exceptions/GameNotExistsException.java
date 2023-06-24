package model.exceptions;

/**
 * This class represents an error occurred when trying to retrieve a Game that does not exist
 */
public class GameNotExistsException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public GameNotExistsException(){
        super("GameId doesn't exists");
    }
}
