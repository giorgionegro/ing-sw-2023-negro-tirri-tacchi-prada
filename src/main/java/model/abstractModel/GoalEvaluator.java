package model.abstractModel;

import model.Tile;

import java.io.Serializable;


/**
 * This class is the abstract class for the goal evaluators.
 * It contains the abstract methods that will be implemented by the goal evaluators.
 * It is used by the CommonGoal class
 **/
public abstract class GoalEvaluator implements Serializable{

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

    /**
     * This method returns the id of the evaluator
     * @return The id of the evaluator
     */
    public abstract String getId();
}
