package model;

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
        STATUS_CHANGED,
        ERROR_REPORTED
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
    private String errorReport;

    /**
     * Construct a User instance with no error reported and {@link #status} = NOT_JOINED
     */
    public User(){
        this.status = Status.NOT_JOINED;
        this.errorReport = "";
    }

    /**
     * This method returns the current status of the user
     * @return {@link #status}
     * */
    public Status getStatus() {
        return status;
    }

    /**
     * This method set the status of the user
     * @param status the new status of the user
     */
    public void setStatus(Status status) {
        this.status = status;
        setChanged();
        notifyObservers(Event.STATUS_CHANGED);
    }

    /**
     * This method report a new error that user encountered during server interaction
     * @param errorMessage the encountered error description
     */
    public void reportError(String errorMessage){
        this.errorReport = errorMessage;
        setChanged();
        notifyObservers(Event.ERROR_REPORTED);
    }

    /**
     * This method returns the last error that user encountered during server interaction
     * @return {@link #errorReport}
     * */
    public String getReportedError(){
        return errorReport;
    }
}
