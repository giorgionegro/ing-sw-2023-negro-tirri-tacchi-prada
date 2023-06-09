package model.abstractModel;

import modelView.PlayerChatInfo;
import util.Observable;

import java.util.List;

/**
 * This is the abstract class for player chat.
 * <p>
 * It defines all the methods required to manage the messages sent to a specific player.
 */
public abstract class PlayerChat extends Observable<PlayerChat.Event>{

    /**
     * This enumerable contains all the chat events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent whenever a message has been received and has been added to chat
         */
        MESSAGE_RECEIVED
    }

    /**
     * This method add the new message to the chat
     * @param newMessage new message that has to be added to chat
     */
    public abstract void addMessage(Message newMessage);

    /**
     * This method returns a copy of the list of messages that are contained in the chat
     * @return a copy of the list of messages that are contained in the chat
     */
    public abstract List<Message> getMessages();

    /**
     * This method returns a {@link PlayerChatInfo} representing this object instance
     * @return A {@link PlayerChatInfo} representing this object instance
     */
    public abstract PlayerChatInfo getInfo();
}
