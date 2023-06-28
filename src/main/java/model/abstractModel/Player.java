package model.abstractModel;

import model.Token;
import modelView.PlayerInfo;
import util.Observable;

import java.util.List;
import java.util.Map;

/**
 * This is the abstract class for the player.
 * <p>
 * It defines all the methods required to access player components and manage player information
 */
public abstract class Player extends Observable<Player.Event> {

    /**
     * This method returns a copy of the current representation of the player shelf
     * @return a copy of the current representation of the player shelf
     */
    public abstract Shelf getShelf();

    /**
     * This method returns a copy of the list of the personal goals of the player
     *
     * @return a copy of the list of personal goals of the player
     */
    public abstract List<PersonalGoal> getPersonalGoals();

    /**
     * This method returns the current representation of the player chat
     *
     * @return the current representation of the player chat
     */
    public abstract PlayerChat getPlayerChat();

    /**
     * This method returns a map containing all the descriptions of common goals achieved by the player, associated
     * to the token that player has won achieving that goal.
     *
     * @return a map containing {@code achievedCommonGoalDescription -> token}
     */
    public abstract Map<String, Token> getAchievedCommonGoals();

    /**
     * This method adds a new achieved common goal
     *
     * @param description the common goal description
     * @param token       the token won by achieving the common goal
     */
    public abstract void addAchievedCommonGoal(String description, Token token);

    /**
     * This method reports a new error that player encountered during gameplay
     *
     * @param error the encountered error description
     */
    public abstract void reportError(String error);



    /**
     * This method returns a {@link PlayerInfo} representing this object instance
     *
     * @return A {@link PlayerInfo} representing this object instance
     */
    public abstract PlayerInfo getInfo();

    /**
     * This enumeration contains all the events that can be sent to observers
     */
    public enum Event {
        /**
         * Event send when a goal is achieved
         */
        COMMON_GOAL_ACHIEVED,
        /**
         * Event send when a error is reported
         */
        ERROR_REPORTED
    }
}
