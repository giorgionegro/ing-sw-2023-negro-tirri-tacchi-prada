package model.exceptions;

public class GameNotStartedException extends Exception {
    public GameNotStartedException(){
        super("Game has not been started yet");
    }
}
