package controller.exceptions;

public class GameAccessDeniedException extends Exception{

    public GameAccessDeniedException(){
        this("access denied");
    }
    public GameAccessDeniedException(String message){
        super("Cannot join the game: "+message);
    }
}
