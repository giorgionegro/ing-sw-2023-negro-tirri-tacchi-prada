package model;

import model.abstractModel.Chat;
import model.abstractModel.Message;

import java.util.ArrayList;
import java.util.List;

public class StandardChat implements Chat {

    private final List<Message> messages;

    public StandardChat(){
        this.messages = new ArrayList<>();
    }

    public List<Message> getMessages(String idDest){
        return messages.stream().filter(message -> message.getDestination().equals("") || message.getDestination().equals(idDest)).toList();
    }

    @Override
    public void addMessage(model.abstractModel.Message newMessage) {
        this.messages.add(newMessage);
    }
}
