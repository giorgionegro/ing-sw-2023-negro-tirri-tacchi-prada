package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

/**
 * This class is the goal evaluator for the StandardXOfDifferentTiles goal
 * It implements the GoalEvaluator abstract class
 * It is used by the CommonGoal class
 **/
public class StandardXOfDifferentTiles extends GoalEvaluator {

    /**
     * This string contains the description of the commonGoal XOfDifferentTiles
     */
    private final String standardXOfDifferentTilesDescription = "Five tiles of the same type forming an X.";

    /**
     * This string contains the id of the commonGoal XOfDifferentTiles
     */
    private final String standardXOfDifferentTilesId = "StandardXOfDifferentTiles";

    /**
     * {@inheritDoc}
     * @return {@link #standardXOfDifferentTilesDescription}
     */
    @Override
    public String getDescription() {
        return standardXOfDifferentTilesDescription;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standardXOfDifferentTilesId}
     */
    @Override
    public String getId(){
        return standardXOfDifferentTilesId;
    }

    /**
     * This method evaluates if the player has reached the goal
     *
     * @param playerShelf the player's shelf
     * @return true if the player has at least 5 tiles of the same type forming an X
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        /* EXAMPLE:
         X 0 X
         0 X 0
         X 0 X
         */
        for (int i = 0; i < playerShelf.length - 2; i++) {
            for (int j = 0; j < playerShelf[0].length - 2; j++) {
                if (playerShelf[i][j].getColor().equals(playerShelf[i + 2][j].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i + 1][j + 1].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i][j + 2].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i + 2][j + 2].getColor())
                        && playerShelf[i][j] != Tile.EMPTY
                ) {
                    return true;
                }
            }
        }
        return false;
    }
}
