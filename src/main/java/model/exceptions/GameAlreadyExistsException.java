package model.exceptions;

public class GameAlreadyExistsException extends Exception{
    public GameAlreadyExistsException() {
        super("GameId already exists");
    }
}
