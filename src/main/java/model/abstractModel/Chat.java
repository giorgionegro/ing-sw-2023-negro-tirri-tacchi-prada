package model.abstractModel;

import java.util.List;

public interface Chat {
    public List<model.Message> getMessages(String playerId);
    public void addMessage(Message newMessage);
}
