package model;

import java.time.LocalDate;
public class Message {

    private String idSender;
    private String idDestination;
    private String text;
    private LocalDate time;

    public Message(String idSender, String idDestination, String text){
        this.idSender = idSender;
        this.idDestination = idDestination;
        this.text = text;
        this.time = LocalDate.now();
    }
}
