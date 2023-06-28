package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Objects;

/**
 * This class is the goal evaluator for the StandardStairs goal.
 */
public class StandardStairs extends GoalEvaluator {

    /**
     * This string contains the description of the commonGoal Stairs
     */
    private final String standardStairsDescription = "Five columns of increasing height or descending height.";

    /**
     * This string contains the id of the commonGoal Stairs
     */
    private final String standardStairsId = "StandardStairs";

    /**
     * {@inheritDoc}
     *
     * @return {@link #standardStairsDescription}
     */
    @Override
    public String getDescription() {
        return this.standardStairsDescription;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #standardStairsId}
     */
    @Override
    public String getId() {
        return this.standardStairsId;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerShelf the player's shelf
     * @return true if the player has at least 5 columns of increasing height or descending, false otherwise
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int numColumns = playerShelf[0].length;
        for (int dir = 0; dir < 2; dir++) {// dir = 0 -> left to right, dir = 1 -> right to left
            // Check if the first column has at least numTiles tiles
            int counter = this.countTilesInColumn(playerShelf, (dir == 0) ? 0 : numColumns - 1);
            if (counter < numColumns) continue;
            int jstart = (dir == 0) ? 1 : numColumns - 2;
            int jend = (dir == 0) ? numColumns : -1;
            // Check if each subsequent column has one fewer tile than the previous column
            if (this.shouldContinue(playerShelf, dir, jstart, jend, counter)) continue;
            return true;
            // If all columns meet the requirements, return true
        }
        return false;
    }

    /**
     * This method checks if the evaluation should continue
     * @param playerShelf the player's shelf
     * @param dir direction
     * @param jstart start index
     * @param jend end index
     * @param prevCounter previous counter
     * @return true if the evaluation should continue, false otherwise
     */
    private boolean shouldContinue(Tile[][] playerShelf, int dir, int jstart, int jend, int prevCounter) {
        int counter;
        boolean continueOuterLoop = false;
        for (int j = jstart; j != jend; j += (dir == 0) ? 1 : -1) {
            counter = this.countTilesInColumn(playerShelf, j);
            if (counter != prevCounter - 1) {
                continueOuterLoop = true;
                break;
            }
            prevCounter = counter;
        }
        return continueOuterLoop;
    }

    /**
     * This method counts the tiles in a column
     * @param playerShelf the player's shelf
     * @param colIndex the column index
     * @return the number of tiles in the column
     */
    private int countTilesInColumn(Tile[][] playerShelf, int colIndex) {
        int numRows = playerShelf.length;
        int counter = 0;
        for (int i = numRows - 1; i >= 0; i--) {
            if (!Objects.equals(playerShelf[i][colIndex].getColor(), Tile.EMPTY.getColor())) {
                counter++;
            }
        }
        return counter;
    }
}