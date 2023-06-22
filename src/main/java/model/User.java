package model;

import modelView.UserInfo;
import org.jetbrains.annotations.NotNull;
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
     * ID of the last user interaction with server
     */
    private long sessionID;

    /**
     * Construct a User instance with no error reported and {@link #status} = NOT_JOINED
     */
    public User(){
        this.status = Status.NOT_JOINED;
        this.eventMessage = "";
        this.sessionID = -1;
    }

    /**
     * This method returns the current status of the user
     * @return {@link #status}
     * */
    public Status getStatus() {
        return status;
    }

    /**
     * This method reports an event to the user, not modifying the sessionID
     * @param status the new status of the user
     * @param eventMessage the description of the event
     * @param eventType the type of the event
     */
    public void reportEvent(Status status, String eventMessage, User.Event eventType){
        reportEvent(status,eventMessage,eventType,sessionID);
    }

    /**
     * This method reports an event to the user, modifying the sessionID
     * @param status the new status of the user
     * @param eventMessage the description of the event
     * @param sessionID the new sessionID
     * @param eventType the type of the event
     */
    public void reportEvent(Status status, String eventMessage, User.Event eventType, long sessionID){
        this.status = status;
        this.sessionID = sessionID;
        this.eventMessage = eventMessage;
        setChanged();
        notifyObservers(eventType);
    }

    /**
     * This method returns a {@link UserInfo} representing this object instance
     * @return A {@link UserInfo} representing this object instance
     */
    public @NotNull UserInfo getInfo(){
        return new UserInfo(status,eventMessage, sessionID);
    }
}
