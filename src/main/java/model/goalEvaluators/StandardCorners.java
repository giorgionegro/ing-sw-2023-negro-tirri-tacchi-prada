package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

/**
 * This class is the goal evaluator for the StandardCorners goal
 * It implements the GoalEvaluator abstract class
 * It is used by the CommonGoal class
 **/
public class StandardCorners extends GoalEvaluator {

    /**
     * This string contains the description of the commonGoal Corners
     */
    private final String standardCornersDescription = "4 tiles of the same type in the 4 corners of the library.";

    /**
     * This string contains the id of the commonGoal Corners
     */
    private final String standardCornersId = "StandardCorners";

    /**
     * {@inheritDoc}
     * @return {@link #standardCornersDescription}
     */
    @Override
    public String getDescription() {
        return standardCornersDescription;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standardCornersId}
     */
    @Override
    public String getId(){
        return standardCornersId;
    }

    /**
     * This method evaluates if the player has reached the goal
     *
     * @param playerShelf the player's shelf
     * @return true if the 4 corners of the library are of the same type
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        Tile topLeft = playerShelf[0][0];
        Tile topRight = playerShelf[0][playerShelf[0].length - 1];
        Tile bottomLeft = playerShelf[playerShelf.length - 1][0];
        Tile bottomRight = playerShelf[playerShelf.length - 1][playerShelf[0].length - 1];
        return topLeft.getColor().equals(topRight.getColor()) &&
                topLeft.getColor().equals(bottomLeft.getColor()) &&
                topLeft.getColor().equals(bottomRight.getColor()) &&
                topLeft != Tile.EMPTY;
    }
}
