package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This class is the goal evaluator for the standard game mode.
 * It checks if the player has at least 3 columns , or 4 rows, of at most 3 different tiles.
 */
public class Standard3or4ColumnsRowMax3Types extends GoalEvaluator {

    /**
     * true if the goal is to have 3 columns of at most 3 different tiles, false if the goal is to have 4 rows of at most 3 different tiles
     */
    private final boolean column;

    /**
     * This string contains the description of the commonGoal 3ColumnMax3Types
     */
    private final String standard3ColumnMax3TypesDescription = "Three columns each formed by 6 tiles of at most three different types.";

    /**
     * This string contains the description of the commonGoal 4RowsMax3Types
     */
    private final String standard4RowsMax3TypesDescription = "Four lines formed each from 5 tiles of at most three different types.";

    /**
     * This string contains the id of the commonGoal 3ColumnMax3Types
     */
    private final String standard3ColumnMax3TypesId = "Standard3ColumnMax3Types";

    /**
     * This string contains the id of the commonGoal 4RowsMax3Types
     */
    private final String standard4RowsMax3TypesId = "Standard4RowsMax3Types";

    /**
     * @param column true if the goal is to have 3 columns of at most 3 different tiles, false if the goal is to have 4 rows of at most 3 different tiles
     */
    public Standard3or4ColumnsRowMax3Types(boolean column) {
        this.column = column;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standard3ColumnMax3TypesDescription} or {@link #standard4RowsMax3TypesDescription} based on {@link #column} value
     */
    @Override
    public @NotNull String getDescription() {
        return (column) ?  standard3ColumnMax3TypesDescription : standard4RowsMax3TypesDescription;
    }

    /**
     * {@inheritDoc}
     * @return {@link #standard3ColumnMax3TypesId} or {@link #standard4RowsMax3TypesId} based on {@link #column} value
     */
    @Override
    public @NotNull String getId(){
        return (column) ? standard3ColumnMax3TypesId : standard4RowsMax3TypesId;
    }

    /**
     * @param playerShelf the player's shelf to evaluate
     * @return true if the player has at least 3 columns , or 4 rows, of at most 3 different tiles
     */
    @Override
    public boolean evaluate(Tile[] @NotNull [] playerShelf) {
        int counter = 0; // counts the number of columns with at most 3 different tiles colors
        int columnLength = playerShelf[0].length;
        int rowLength = playerShelf.length;
        int imax = (column) ? columnLength : rowLength;
        int jmax = (column) ? rowLength : columnLength;

        for (int i = 0; i < imax; i++) {
            //check if every line is different
            String[] colors = new String[(column) ? rowLength : columnLength];
            for (int j = 0; j < jmax; j++) {
                colors[j] = (column) ? playerShelf[j][i].getColor() : playerShelf[i][j].getColor();
            }
            //if colors contains EMPTY, we skip the line
            if (Arrays.stream(colors).anyMatch(colour -> colour.equals(Tile.EMPTY.getColor()))) {
                continue;
            }
            if (Arrays.stream(colors).filter(colour -> !colour.equals(Tile.EMPTY.getColor())).distinct().count() <= 3) {
                counter++;
            }
        }
        return counter >= 3;
    }
}
