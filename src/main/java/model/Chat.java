package model;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private List<Message> messages;

    public Chat(){
        this.messages = new ArrayList<>();
    }

    public List<Message> getMessages(String idDest){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void addMessage(Message message){
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
