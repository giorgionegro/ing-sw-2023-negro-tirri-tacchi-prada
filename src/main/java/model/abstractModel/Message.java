package model.abstractModel;

import java.io.Serializable;

/**
 * This interface defines the methods to access to message data
 */
public interface Message extends Serializable {
    /**
     * This method returns the id of the sender of the message
     *
     * @return the id of the sender of the message
     */
    String getSender();

    /**
     * This method returns the id of the receiver of the message
     * <p>
     * If {@code receiver.isEmpty()==true} then the message is supposed to be sent broadcast
     *
     * @return the MessageReceiver of the message
     */
    String getReceiver();

    /**
     * This method returns the text-content of the message
     *
     * @return the text-content of the message
     */
    String getText();

    /**
     * This method returns the string representation of the timestamp when message has been created
     *
     * @return the string representation of the timestamp when message has been created
     */
    String getTimestamp();
}
