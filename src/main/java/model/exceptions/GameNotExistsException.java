package model.exceptions;

public class GameNotExistsException extends Exception{
    public GameNotExistsException(){
        super("GameId doesn't exists");
    }
}
