package model.exceptions;

/**
 * This class represents an error occurred when trying to create a Game with an ID that already exists
 */
public class GameAlreadyExistsException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public GameAlreadyExistsException() {
        super("GameId already exists");
    }
}
