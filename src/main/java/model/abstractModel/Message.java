package model.abstractModel;

import java.time.LocalDate;

public interface Message {
    public String getSender();
    public String getDestination();
    public String getText();
    public String getTimestamp();
}
