package model.abstractModel;

/**
 * This interface define the method to access to message data
 */
public interface Message {
    /**
     * This method returns the id of the sender of the message
     * @return the id of the sender of the message
     */
    String getSender();

    /**
     * This method returns the id of the subject of the message
     * <p>
     * If {@code subject.isEmpty()==true} then the message is supposed to be sent broadcast
     * @return the MessageSubject of the message
     */
    String getSubject();

    /**
     * This method returns the text-content of the message
     * @return the text-content of the message
     */
    String getText();

    /**
     * This method returns the string representation of the timestamp when message has been created
     * @return the string representation of the timestamp when message has been created
     */
    String getTimestamp();
}
