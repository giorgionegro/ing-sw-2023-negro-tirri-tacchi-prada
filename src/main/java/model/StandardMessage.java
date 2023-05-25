package model;

import model.abstractModel.Message;
import org.jetbrains.annotations.NotNull;

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
     * The message subject
     */
    private final String subject;
    /**
     * The message text content
     */
    private final String text;
    /**
     * The {@link String} representation of the timestamp of instance creation
     */
    private final @NotNull String timestamp;

    /**
     * Construct an instance with given sender id, subject and text, timestamp is set on localtime at instance creation
     * @param idSender the id of the sender
     * @param subject the subject of the message
     * @param text an {@link String} representation of message text content
     */
    public StandardMessage(String idSender, String subject, String text){
        this.idSender = idSender;
        this.subject = subject;
        this.text = text;
        this.timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now());
    }

    /**
     * {@inheritDoc}
     * @return {@link #idSender}
     */
    @Override
    public String getSender() {
        return idSender;
    }

    /**
     * {@inheritDoc}
     * @return {@link #subject}
     */
    @Override
    public String getSubject() {
        return subject;
    }

    /**
     * {@inheritDoc}
     * @return {@link #text}
     */
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     * @return {@link #timestamp}
     */
    @Override
    public @NotNull String getTimestamp() {
        return timestamp;
    }

    @Override
    public @NotNull String toString(){
        return "Sender: "+idSender+"\n"
                +"Receiver: "+subject+"\n"
                +"Text: "+text+"\n"
                +"TimeStamp: "+timestamp+"\n";
    }
}

