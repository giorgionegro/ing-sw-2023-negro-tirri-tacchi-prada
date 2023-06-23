package model.exceptions;

/**
 * This class represents an error occurred when trying to perform an action on a Player that is not present in a Game
 */
public class PlayerNotExistsException extends Exception {

    /**
     * This constructor builds an instance of this class
     */
    public PlayerNotExistsException(){
        super("PlayerId not exists");
    }
}
