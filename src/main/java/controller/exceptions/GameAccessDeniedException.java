package controller.exceptions;

/**
 * This class represent an error occurred when trying to join a Game
 */
public class GameAccessDeniedException extends Exception{

    /**
     * This constructor build an instance of this class adding the given message to the error message
     * @param message the message to be added to the error message
     */
    public GameAccessDeniedException(String message){
        super("Cannot join the game: "+message);
    }

    /**
     * This constructor build an instance of this class with a standard message
     */
    public GameAccessDeniedException(){
        super("Cannot join the game");
    }
}
