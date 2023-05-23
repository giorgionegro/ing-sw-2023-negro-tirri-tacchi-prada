package model;

import modelView.UserInfo;
import util.Observable;

/**
 * This class represent a user of the server
 * <p>
 * It defines all the methods required to access and manage user information
 */
public class User extends Observable<User.Event> {
    /**
     * This enumeration contains all the events that can be sent to observers
     */
    public enum Event{
        ERROR_REPORTED,
        GAME_CREATED,
        GAME_JOINED,
        GAME_LEAVED
    }

    /**
     * This enumeration contains all the statuses a user can be
     */
    public enum Status{
        JOINED,
        NOT_JOINED,
    }

    /**
     * The current status of the user
     */
    private Status status;

    /**
     * The last error user encountered during server interaction
     */
    private String eventMessage;

    /**
     * The last timeStamp of a user interaction with server
     */
    private long interactionTime;

    /**
     * Construct a User instance with no error reported and {@link #status} = NOT_JOINED
     */
    public User(){
        this.status = Status.NOT_JOINED;
        this.eventMessage = "";
        this.interactionTime = -1;
    }

    /**
     * This method returns the current status of the user
     * @return {@link #status}
     * */
    public Status getStatus() {
        return status;
    }

    /**
     * TODO
     * @param status
     * @param eventMessage
     * @param interactionTime
     * @param evt
     */
    public void reportEvent(Status status, String eventMessage, long interactionTime, User.Event evt){
        this.status = status;
        this.interactionTime = interactionTime;
        this.eventMessage = eventMessage;
        setChanged();
        notifyObservers(evt);
    }

    /**
     * This method returns the last error that user encountered during server interaction
     * @return {@link #eventMessage}
     * */
    public String getEventMessage(){
        return eventMessage;
    }

    /**
     * This method returns a {@link UserInfo} representing this object instance
     * @return A {@link UserInfo} representing this object instance
     */
    public UserInfo getInfo(){
        return new UserInfo(status,eventMessage,interactionTime);
    }
}
