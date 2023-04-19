package modelView;

import java.io.Serializable;

public class PlayerInfo implements Serializable {

    private final String message;
    public PlayerInfo(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
