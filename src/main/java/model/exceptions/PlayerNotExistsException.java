package model.exceptions;

public class PlayerNotExistsException extends Exception {
    public PlayerNotExistsException(){
        super("PlayerId not exists");
    }
}
