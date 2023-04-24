package model;

import util.Observable;

public class User extends Observable<User.Event> {

    public enum Event{
        STATUS_CHANGED
    }

    public enum Status{
        JOINED,
        NOT_JOINED,
    }
    private Status status;

    public User(){
        this.status = Status.NOT_JOINED;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        setChanged();
        notifyObservers(Event.STATUS_CHANGED);
    }
}
