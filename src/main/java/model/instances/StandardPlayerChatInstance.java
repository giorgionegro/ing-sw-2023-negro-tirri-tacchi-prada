package model.instances;

import model.abstractModel.Message;

import java.io.Serializable;
import java.util.List;

/**
 * This record represents a {@link model.StandardPlayerChat} instance
 * @param messages the representation of instance's set of {@link Message}
 */
public record StandardPlayerChatInstance(
        List<Message> messages
)implements Serializable{}
