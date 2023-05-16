package model;

import model.abstractModel.PlayerChat;
import model.abstractModel.Message;
import model.instances.StandardPlayerChatInstance;
import modelView.PlayerChatInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implementation of PlayerChat.
 * <p>
 * It uses a List to manage messages
 */
public class StandardPlayerChat extends PlayerChat {

    /**
     * messages sent to this player chat (list of messages)
     */
    private final List<Message> messages;

    /**
     * Constructor of an empty player chat
     */
    public StandardPlayerChat(){
        this.messages = new ArrayList<>();
    }

    public StandardPlayerChat(StandardPlayerChatInstance instance){
        this.messages = instance.messages();
    }

    /**
     * {@inheritDoc}
     * @param newMessage new message that has to be added to chat
     */
    @Override
    public void addMessage(Message newMessage) {
        this.messages.add(newMessage);
        setChanged();
        notifyObservers(Event.MESSAGE_RECEIVED);
    }

    /**
     * {@inheritDoc}
     * @return a copy of {@link #messages}
     */
    @Override
    public List<Message> getMessages() {
        return new ArrayList<>(this.messages);
    }

    /**
     * {@inheritDoc}
     * @return A {@link PlayerChatInfo} representing this object instance
     */
    @Override
    public PlayerChatInfo getInfo() {
        return new PlayerChatInfo(getMessages());
    }

    /**
     * {@inheritDoc}
     * @return A {@link model.instances.StandardPlayerInstance} constructed using instance values
     */
    @Override
    public Serializable getInstance() {
        return new StandardPlayerChatInstance(messages);
    }
}
