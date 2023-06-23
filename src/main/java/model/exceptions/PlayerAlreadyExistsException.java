package model.exceptions;

/**
 * This class represents an error occurred when trying to add a Player to a Game that already contains that playerID
 */
public class PlayerAlreadyExistsException extends Exception{

    /**
     * This constructor builds an instance of this class
     */
    public PlayerAlreadyExistsException(){
        super("PlayerId already exists");
    }
}
