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

    /**
     * Returns chat messages of a player
     * @param idDest name of the message receiver
     * @return list of messages sent to everyone or the receiver
     */
    public List<Message> getMessages(String idDest){
        return messages.stream().filter(message -> message.getDestination().equals("") || message.getDestination().equals(idDest)).toList();
    }

    /**
     * Add a new message to the chat
     * @param newMessage  message
     */
    @Override
    public void addMessage(model.abstractModel.Message newMessage) {
        this.messages.add(newMessage);
    }
}
