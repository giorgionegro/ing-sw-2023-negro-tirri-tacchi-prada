package model.exceptions;

/**
 * This class represents an error occurred when trying to create a duplicated Game
 */
public class GameAlreadyExistsException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public GameAlreadyExistsException() {
        super("GameId already exists");
    }
}
