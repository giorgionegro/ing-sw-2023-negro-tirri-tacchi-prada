package controller.exceptions;

public class MalformedPlayerMoveException extends Exception{
    public MalformedPlayerMoveException(String message){
        super("Malformed PlayerMove: "+message);
    }
}
