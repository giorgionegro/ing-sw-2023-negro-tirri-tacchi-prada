package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class is the goal evaluator for the Standard2ColumnsRowOfDifferentTiles goal.
 * It implements the GoalEvaluator abstract class.
 * It is used by the CommonGoal class.
 */
public class Standard2ColumnsRowOfDifferentTiles extends GoalEvaluator {

    /**
     * true if the goal is to have 2 columns of 6 different tiles, false if the goal is to have 2 rows of 6 different tiles
     * @see #evaluate(Tile[][])
     */
    private final boolean column;

    /**
     * This string contains the description of the commonGoal 2ColumnsOfDifferentTiles
     */
    private final String standard2ColumnsOfDifferentTilesDescription = "Two columns formed each from 6 different types of tiles";

    /**
     * This string contains the description of the commonGoal 2RowsOfDifferentTiles
     */
    private final String standard2RowsOfDifferentTilesDescription = "Two lines formed each from 5 different types of tiles";

    /**
     * This string contains the id of the commonGoal 2ColumnsOfDifferentTiles
     */
    private final String standard2ColumnsOfDifferentTilesId = "Standard2ColumnsOfDifferentTiles";

    /**
     * This string contains the id of the commonGoal 2RowsOfDifferentTiles
     */
    private final String standard2RowsOfDifferentTilesId = "Standard2RowsOfDifferentTiles";

    /**
     * Constructor for Standard2ColumnsRowOfDifferentTiles
     * @param column true if the goal is to have 2 columns of 6 different tiles, false if the goal is to have 2 rows of 6 different tiles
     */
    public Standard2ColumnsRowOfDifferentTiles(boolean column) {
        this.column = column;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standard2ColumnsOfDifferentTilesDescription} or {@link #standard2RowsOfDifferentTilesDescription} based on {@link #column} value
     */
    @Override
    public @NotNull String getDescription() {
        return (column) ? standard2ColumnsOfDifferentTilesDescription : standard2RowsOfDifferentTilesDescription;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standard2ColumnsOfDifferentTilesId} or {@link #standard2RowsOfDifferentTilesId} based on {@link #column} value
     */
    @Override
    public @NotNull String getId(){
        return (column) ? standard2ColumnsOfDifferentTilesId : standard2RowsOfDifferentTilesId;
    }

    /**
     * @param playerShelf the player's shelf to evaluate
     * @return true if the player has at least 2 columns , or 2 rows, of 6 different tiles
     */
    // returns true if the player has at least 2 columns of 6 different tiles
    @Override
    public boolean evaluate(Tile[] @NotNull [] playerShelf) {
        int counter = 0;
        int columnLength = playerShelf[0].length;
        int rowLength = playerShelf.length;
        int imax = (column) ? columnLength : rowLength;
        int jmax = (column) ? rowLength : columnLength;

        for (int i = 0; i < imax; i++) {
            //check if every line is different
            String[] colours = new String[(column) ? rowLength : columnLength];
            for (int j = 0; j < jmax; j++) {
                colours[j] = (column) ? playerShelf[j][i].getColor() : playerShelf[i][j].getColor();
            }
            //remove Empty tiles
            if (Arrays.stream(colours).filter(Objects::nonNull).filter(colour -> !colour.equals(Tile.EMPTY.getColor())).distinct().count() == colours.length) {//check if every line is different by comparing the number of distinct colours
                counter++;
            }
        }
        return counter >= 2;
    }
}
