package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;

/**
 * This class is the goal evaluator for the StandardTwoSquares goal.
 **/
public class StandardTwoSquares extends GoalEvaluator {

    /**
     * This string contains the description of the commonGoal TwoSquares
     */
    private final String standardTwoSquaresDescription = "Two separate groups of 4 tiles same type that form a 2x2 square.";

    /**
     * This string contains the id of the commonGoal TwoSquares
     */
    private final String standardTwoSquaresId = "StandardTwoSquares";

    /**
     * {@inheritDoc}
     *
     * @return {@link #standardTwoSquaresDescription}
     */
    @Override
    public String getDescription() {
        return this.standardTwoSquaresDescription;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #standardTwoSquaresId}
     */
    @Override
    public String getId() {
        return this.standardTwoSquaresId;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerShelf the player's shelf to evaluate
     * @return true if the player has at least 2 groups of 2x2 tiles of the same type
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

        //clone the array
        int counter = 0;
        Tile[][] clone = Arrays.stream(playerShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        for (int i = 0; i < clone.length - 1; i++) {
            for (int j = 0; j < clone[0].length - 1; j++) {
                if (clone[i][j].getColor().equals(clone[i][j + 1].getColor()) &&
                        clone[i][j].getColor().equals(clone[i + 1][j].getColor()) &&
                        clone[i][j].getColor().equals(clone[i + 1][j + 1].getColor())
                        && (clone[i][j] != Tile.EMPTY)) {
//if we find a group of 4 tiles of the same type, we remove them from the array
                    clone[i][j] = Tile.EMPTY;
                    clone[i][j + 1] = Tile.EMPTY;
                    clone[i + 1][j] = Tile.EMPTY;
                    clone[i + 1][j + 1] = Tile.EMPTY;
                    counter++;
                }
            }
        }

        return counter >= 2;

    }


}
