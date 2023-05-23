package model;

import model.abstractModel.PlayerChat;
import model.abstractModel.Message;
import modelView.PlayerChatInfo;

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
     * TODO
     * @return
     */
    @Override
    public PlayerChatInfo getInfo() {
        return new PlayerChatInfo(getMessages());
    }


}
