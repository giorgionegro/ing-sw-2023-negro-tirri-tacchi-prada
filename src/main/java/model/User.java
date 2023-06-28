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
    public User() {
        super();
        this.status = Status.NOT_JOINED;
        this.eventMessage = "";
        this.sessionID = -1;
    }

    /**
     * This method returns the current status of the user
     *
     * @return {@link #status}
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * This method reports an event to the user, not modifying the sessionID
     *
     * @param status       the new status of the user
     * @param eventMessage the description of the event
     * @param eventType    the type of the event
     */
    public void reportEvent(Status status, String eventMessage, User.Event eventType) {
        this.reportEvent(status, eventMessage, eventType, this.sessionID);
    }

    /**
     * This method reports an event to the user, modifying the sessionID
     *
     * @param status       the new status of the user
     * @param eventMessage the description of the event
     * @param sessionID    the new sessionID
     * @param eventType    the type of the event
     */
    public void reportEvent(Status status, String eventMessage, User.Event eventType, long sessionID) {
        this.status = status;
        this.sessionID = sessionID;
        this.eventMessage = eventMessage;
        this.setChanged();
        this.notifyObservers(eventType);
    }

    /**
     * This method returns a {@link UserInfo} representing this object instance
     *
     * @return A {@link UserInfo} representing this object instance
     */
    public UserInfo getInfo() {
        return new UserInfo(this.status, this.eventMessage, this.sessionID);
    }

    /**
     * This enumeration contains all the events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent when an error is reported
         */
        ERROR_REPORTED,
        /**
         * This event is sent when a game is created
         */
        GAME_CREATED,
        /**
         * This event is sent when a game is joined
         */
        GAME_JOINED,
        /**
         * This event is sent when a game is leaved
         */
        GAME_LEAVED
    }

    /**
     * This enumeration contains all the statuses a user can be
     */
    public enum Status {
        /**
         * This status is set when the user is in a game
         */
        JOINED,
        /**
         * This status is set when the user is not in a game
         */
        NOT_JOINED,
    }
}
