package model;

import model.abstractModel.Chat;
import model.abstractModel.Message;

import java.util.List;

public class StandardChat implements Chat {

    private List<Message> messages;

    public List<Message> getMessages(String idDest){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void addMessage(model.abstractModel.Message newMessage) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
