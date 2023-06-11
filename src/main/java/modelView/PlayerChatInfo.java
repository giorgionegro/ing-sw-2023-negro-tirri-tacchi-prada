package modelView;

import model.abstractModel.Message;

import java.io.Serializable;
import java.util.List;

/**
 * This record contains information about the state of a {@link model.abstractModel.PlayerChat}
 * @param messages list of messages sent to a player
 */
public record PlayerChatInfo(List<Message> messages) implements Serializable {}
