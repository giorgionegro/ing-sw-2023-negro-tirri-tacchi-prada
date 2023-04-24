package model;

import util.Observable;

public class User extends Observable<User.Event> {

    public enum Event{
        STATUS_CHANGED,
        ERROR_REPORTED
    }

    public enum Status{
        JOINED,
        NOT_JOINED,
    }
    private Status status;
    private String errorReport;

    public User(){
        this.status = Status.NOT_JOINED;
        this.errorReport = "";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        setChanged();
        notifyObservers(Event.STATUS_CHANGED);
    }

    public void reportError(String errorMessage){
        this.errorReport = errorMessage;
        setChanged();
        notifyObservers(Event.ERROR_REPORTED);
    }

    public String getReportedError(){
        return errorReport;
    }
}
