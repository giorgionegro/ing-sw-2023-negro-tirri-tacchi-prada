package controller.exceptions;


public class GameAccessDeniedException extends Exception{

    public GameAccessDeniedException(String message){
        super("Cannot join the game: "+message);
    }
}
