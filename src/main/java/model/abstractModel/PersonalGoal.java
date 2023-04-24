package model.abstractModel;

import model.Tile;
import util.Observable;

/**
 * This is the abstract class of a personal goal.
 * <p>
 * It defines all the required methods needed to access personal goal information and manage personal goal achieved status
 */
public abstract class PersonalGoal extends Observable<PersonalGoal.Event> {
    /**
     * This enumeration contains all the personal goal events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent when personal goal has been achieved
         */
        GOAL_ACHIEVED
    }

    /**
     * This method returns the description of the personal goal requirements to be achieved
     * @return the description of the personal goal requirements to be achieved
     */
    public abstract String getDescription();

    /**
     * This method returns true if the goal has been achieved
     * @return true if the goal has been achieved
     */
    public abstract boolean isAchieved();

    /**
     * This method marks the goal as achieved
     * <p>
     * If invoked then isAchieved method will then return {@code true}
     */
    public abstract void setAchieved();

    /**
     * This method test if the given shelf representation match the goal requirements
     * @param playerShelf the shelf representation that has to be tested
     * @return {@code true} if the given shelf representation match the goal requirements, {@code false} otherwise
     */
    public abstract boolean evaluate(Tile[][] playerShelf);
}
