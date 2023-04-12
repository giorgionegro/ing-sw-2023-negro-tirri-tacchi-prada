package model.exceptions;

public class PlayerAlreadyExistsException extends Exception{
    public PlayerAlreadyExistsException(){
        super("PlayerId already exists");
    }
}
