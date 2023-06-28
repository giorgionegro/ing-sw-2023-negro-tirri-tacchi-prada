package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class is the goal evaluator for the Standard2ColumnsOfDifferentTiles or Standard2RowOfDifferentTiles goal.
 */
public class Standard2ColumnsRowOfDifferentTiles extends GoalEvaluator {

    /**
     * true if the goal is to have 2 columns of 6 different tiles, false if the goal is to have 2 rows of 6 different tiles
     *
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
     *
     * @param column true if the goal is to have 2 columns of 6 different tiles, false if the goal is to have 2 rows of 6 different tiles
     */
    public Standard2ColumnsRowOfDifferentTiles(boolean column) {
        super();
        this.column = column;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #standard2ColumnsOfDifferentTilesDescription} or {@link #standard2RowsOfDifferentTilesDescription} based on {@link #column} value
     */
    @Override
    public String getDescription() {
        return (this.column) ? this.standard2ColumnsOfDifferentTilesDescription : this.standard2RowsOfDifferentTilesDescription;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #standard2ColumnsOfDifferentTilesId} or {@link #standard2RowsOfDifferentTilesId} based on {@link #column} value
     */
    @Override
    public String getId() {
        return (this.column) ? this.standard2ColumnsOfDifferentTilesId : this.standard2RowsOfDifferentTilesId;
    }

    /**
     * {@inheritDoc}
     * @param playerShelf the player's shelf to evaluate
     * @return true if the player has at least 2 columns , or 2 rows, of 6 different tiles
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0;
        int columnLength = playerShelf[0].length;
        int rowLength = playerShelf.length;
        int imax = (this.column) ? columnLength : rowLength;
        int jmax = (this.column) ? rowLength : columnLength;

        for (int i = 0; i < imax; i++) {
            //check if every line is different
            String[] colours = new String[(this.column) ? rowLength : columnLength];
            for (int j = 0; j < jmax; j++) {
                colours[j] = (this.column) ? playerShelf[j][i].getColor() : playerShelf[i][j].getColor();
            }
            //remove Empty tiles
            if (Arrays.stream(colours).filter(Objects::nonNull).filter(colour -> !colour.equals(Tile.EMPTY.getColor())).distinct().count() == colours.length) {//check if every line is different by comparing the number of distinct colours
                counter++;
            }
        }
        return counter >= 2;
    }
}
