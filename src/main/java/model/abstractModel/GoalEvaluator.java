package model.abstractModel;

import model.Tile;
import org.jetbrains.annotations.NotNull;


/**
 * This class is the abstract class for the goal evaluators
 * It contains the abstract methods that will be implemented by the goal evaluators
 * It is used by the CommonGoal class
 **/
public abstract class GoalEvaluator {

    /**
     * This method evaluates the player's shelf
     * @param playerShelf the player's shelf
     * @return true if the player has completed the goal
     **/
    public abstract boolean evaluate(Tile[][] playerShelf);


    /**
     * This method returns the description of the goal
     * @return the description of the goal
     **/
    public abstract String getDescription();
}
