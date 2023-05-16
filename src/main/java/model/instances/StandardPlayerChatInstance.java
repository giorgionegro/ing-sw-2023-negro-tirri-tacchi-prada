package model.instances;

import model.abstractModel.Message;

import java.io.Serializable;
import java.util.List;

public record StandardPlayerChatInstance(
        List<Message> messages
)implements Serializable{}
