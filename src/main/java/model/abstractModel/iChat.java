package model.abstractModel;

import model.Message;

import java.util.List;

public interface iChat {
    public List<Message> getMessages(String playerId);
    public void addMessage(iMessage newMessage);
}
