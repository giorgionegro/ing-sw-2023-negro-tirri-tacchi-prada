package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This class is the goal evaluator for the StandardSixGroup2Tiles goal
 * It implements the GoalEvaluator abstract class
 * It is used by the CommonGoal class
 **/
public class StandardSixGroup2Tiles extends GoalEvaluator {

    /**
     * This string contains the description of the commonGoal SixGroup2Tiles
     */
    private final String standardSixGroup2TilesDescription = "Six separate groups formed each from two adjacent tiles of the same type.";

    /**
     * This string contains the id of the commonGoal SixGroup2Tiles
     */
    private final String standardSixGroup2TilesId = "StandardSixGroup2Tiles";

    /**
     * {@inheritDoc}
     * @return {@link #standardSixGroup2TilesDescription}
     */
    @Override
    public @NotNull String getDescription() {
        return standardSixGroup2TilesDescription;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standardSixGroup2TilesId}
     */
    @Override
    public @NotNull String getId(){
        return standardSixGroup2TilesId;
    }

    /**
     * This method evaluates the player's shelf
     *
     * @param playerShelf the player's shelf to evaluate
     * @return true if the player has at least 6 groups of 2 tiles of the same type
     */
    @Override
    public boolean evaluate(Tile[] @NotNull [] playerShelf) {

        int counter = 0; // counts the number of groups of 2 tiles of the same type
        //clone
        Tile[][] clone = Arrays.stream(playerShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        //first we check the horizontal groups
        for (Tile[] tiles : clone) {
            for (int j = 0; j < clone[0].length - 1; j++) {
                if (tiles[j].getColor().equals(tiles[j + 1].getColor()) &&
                        (tiles[j] != Tile.EMPTY)) {
                    //if we find a group of 2 tiles of the same type, we remove them from the array
                    tiles[j] = Tile.EMPTY;
                    tiles[j + 1] = Tile.EMPTY;
                    counter++;
                }
            }
        }
        //then we check the vertical groups
        for (int i = 0; i < clone.length - 1; i++)
            for (int j = 0; j < clone[0].length; j++) {
                if (clone[i][j].getColor().equals(clone[i + 1][j].getColor()) &&
                        (clone[i][j] != Tile.EMPTY)) {
                    //if we find a group of 2 tiles of the same type, we remove them from the array
                    clone[i][j] = Tile.EMPTY;
                    clone[i + 1][j] = Tile.EMPTY;
                    counter++;
                }
            }
        return counter >= 6;
    }
}
