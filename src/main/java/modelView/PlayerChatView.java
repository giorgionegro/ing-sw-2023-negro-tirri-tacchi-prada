package modelView;

import model.abstractModel.Message;
import model.abstractModel.PlayerChat;

import java.io.Serializable;
import java.util.List;

public class PlayerChatView implements Serializable {
    private final List<Message> messages;

    public PlayerChatView(PlayerChat chat){
        messages = chat.getMessages();
    }

    public List<Message> getMessages() {
        return messages;
    }
}
