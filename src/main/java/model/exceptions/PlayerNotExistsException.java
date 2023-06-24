package model.exceptions;

/**
 * This class represents an error occurred when trying to retrieve a Player that does not exist
 */
public class PlayerNotExistsException extends Exception {

    /**
     * This constructor builds an instance of this class
     */
    public PlayerNotExistsException(){
        super("PlayerId not exists");
    }
}
