package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class is the goal evaluator for the StandardStairs goal
 * It implements the GoalEvaluator abstract class
 * It is used by the CommonGoal class
 */
public class StandardStairs extends GoalEvaluator {
    /**
     * This method returns the description of the goal
     *
     * @return String
     */
    @Override
    public @NotNull String getDescription() {
        return "Five columns of increasing height or descending height";
    }

    /**
     * This method evaluates if the player has reached the goal
     *
     * @param playerShelf the player's shelf
     * @return true if the player has at least 5 columns of increasing height or descending, false otherwise
     */
    @Override
    public boolean evaluate(Tile[] @NotNull [] playerShelf) {
        int numColumns = playerShelf[0].length;
        int numRows = playerShelf.length;
        for (int dir = 0; dir < 2; dir++) {
            // Check if the first column has at least numTiles tiles
            int counter = countTilesInColumn(playerShelf, (dir == 0) ? 0 : numColumns - 1);
            if (counter < numColumns) continue;
            int jstart = (dir == 0) ? 1 : numColumns - 2;
            int jend = (dir == 0) ? numColumns : -1;
            boolean continueOuterLoop = false;
            // Check if each subsequent column has one fewer tile than the previous column
            int prevCounter = counter;
            for (int j = jstart; j != jend; j += (dir == 0) ? 1 : -1) {
                counter = countTilesInColumn(playerShelf, j);
                if (counter != prevCounter - 1) {
                    continueOuterLoop = true;
                    break;
                }
                prevCounter = counter;
            }
            if (continueOuterLoop) continue;
            return true;
            // If all columns meet the requirements, return true
        }
        return false;
    }

    private int countTilesInColumn(Tile[] @NotNull [] playerShelf, int colIndex) {
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