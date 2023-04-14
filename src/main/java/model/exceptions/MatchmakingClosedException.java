package model.exceptions;

public class MatchmakingClosedException extends Exception{
    public MatchmakingClosedException(){
        super("Game matchmaking is closed");
    }
}
