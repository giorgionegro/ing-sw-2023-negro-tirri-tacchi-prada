package model;

import model.abstractModel.Message;

import java.time.LocalDate;
import java.util.Date;

public class StandardMessage implements Message {

    private String idSender;
    private String idDestination;
    private String text;
    private String timestamp;

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
}

