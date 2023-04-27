package modelView;

import model.abstractModel.Message;

import java.io.Serializable;
import java.util.List;

/**
 * @param messages - list of messages
 */
public record PlayerChatInfo(List<Message> messages) implements Serializable {
}
