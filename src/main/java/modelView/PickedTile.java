package modelView;

import java.io.Serializable;

/**
 * Class that represents a picked tile
 */
public class PickedTile implements Serializable{


    /**
     * row of the tile
     */

    private final int row;


    /**
     * column of the tile
     */

    private final int col;

    /**
     * Constructor
     *
     * @param row row of the tile
     * @param col column of the tile
     */
    public PickedTile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return row of the tile
     */
    public int getRow() {
        return row;
    }

    /**
     * @return column of the tile
     */

    public int getCol() {
        return col;
    }
}

