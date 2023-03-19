package model.abstractModel;

import java.util.Date;

public interface iMessage {
    public String getSender();
    public String getDestination();
    public String getText();
    public Date getDate();
}
