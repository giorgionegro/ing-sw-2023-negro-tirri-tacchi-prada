package model;

import model.abstractModel.Message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StandardMessage implements Message {

    private final String idSender;
    private final String idDestination;
    private final String text;
    private final String timestamp;

    public StandardMessage(String idSender, String idDestination, String text){
        this.idSender = idSender;
        this.idDestination = idDestination;
        this.text = text;
        this.timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now());
    }


    @Override
    public String getSender() {
        return idSender;
    }

    @Override
    public String getDestination() {
        return idDestination;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }
    @Override
    public String toString(){
        return "Sender: "+idSender+"\n"
                +"Receiver: "+idDestination+"\n"
                +"Text: "+text+"\n"
                +"TimeStamp: "+timestamp+"\n";
    }
}

