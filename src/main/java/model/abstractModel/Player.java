package model.abstractModel;

import model.Tile;
import model.Token;
import util.Observable;

import java.util.List;
import java.util.Map;

/**
 * This is the abstract class for the player.
 * <p>
 * It defines all the methods required to access player components and manage player information
 */
public abstract class Player extends Observable<Player.PlayerEvent> {

    /**
     * This enumeration contains all the events that can be sent to observers
     */
    public enum PlayerEvent{
        /**
         * This event is sent whenever the player shelf has been modified
         */
        SHELF_MODIFIED,
    }

    /**
     * This method returns the player id
     * @return id of the player
     */
    public abstract String getId();

    /**
     * This method returns a copy of the current representation of the player shelf
     * @return a copy of the current representation of the player shelf
     */
    public abstract Tile[][] getShelf();

    /**
     * This method update the current representation of the player shelf
     * @param modifiedShelf the new representation of the player shelf
     */
    public abstract void setShelf(Tile[][] modifiedShelf);

    /**
     * This method returns a copy of the list of the personal goals of the player
     * @return a copy of the list of personal goals of the player
     */
    public abstract List<PersonalGoal> getPersonalGoals();

    /**
     * This method returns the current representation of the player chat
     * @return the current representation of the player chat
     */
    public abstract PlayerChat getPlayerChat();
    /**
     * This method returns a map containing all the descriptions of common goals achieved by the player, associated
     * to the token that player has won achieving that goal.
     * @return a map containing {@code achievedCommonGoalDescription -> token}
     */
    public abstract Map<String,Token> getAchievedCommonGoals();

    /**
     * This method add a new achieved common goal
     * @param description the common goal description
     * @param token the token won by achieving the common goal
     */
    public abstract void addAchievedCommonGoal(String description, Token token);
}
