package model.exceptions;

/**
 * This class represents an error occurred when trying to add a duplicated Player to a Game
 */
public class PlayerAlreadyExistsException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public PlayerAlreadyExistsException(){
        super("PlayerId already exists");
    }
}
