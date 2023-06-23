package model;

import model.abstractModel.Message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class is an implementation of {@link Message}
 */
public class StandardMessage implements Message {

    /**
     * The id of the message sender
     */
    private final String idSender;
    /**
     * The message receiver
     */
    private final String receiver;
    /**
     * The message text content
     */
    private final String text;
    /**
     * The {@link String} representation of the timestamp of instance creation
     */
    private final String timestamp;

    /**
     * Construct an instance with given sender id, receiver and text, timestamp is set on localtime at instance creation
     *
     * @param idSender the id of the sender
     * @param receiver the receiver of the message
     * @param text     an {@link String} representation of message text content
     */
    public StandardMessage(String idSender, String receiver, String text) {
        super();
        this.idSender = idSender;
        this.receiver = receiver;
        this.text = text;
        this.timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now());
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #idSender}
     */
    @Override
    public String getSender() {
        return this.idSender;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #receiver}
     */
    @Override
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #text}
     */
    public String getText() {
        return this.text;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #timestamp}
     */
    @Override
    public String getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "Sender: " + this.idSender + "\n"
                + "Receiver: " + this.receiver + "\n"
                + "Text: " + this.text + "\n"
                + "TimeStamp: " + this.timestamp + "\n";
    }
}

