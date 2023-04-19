package modelView;

import java.io.Serializable;
import java.util.List;

/**
 * Class that represents a player move
 */
public class PlayerMoveInfo implements Serializable {
    /**
     * list of picked tiles
     */
    private final List<PickedTile> pickedTiles;
    /**
     * column where the player wants to insert the tiles
     */
    private final int columnToInsert;

    /**
     * @param pickedTiles list of picked tiles
     * @param columnToInsert column where the player wants to insert the tiles
     */
    public PlayerMoveInfo(List<PickedTile> pickedTiles, int columnToInsert) {
        this.pickedTiles = pickedTiles;
        this.columnToInsert = columnToInsert;
    }

    /**
     * @return list of picked tiles
     */
    public List<PickedTile> getPickedTiles(){ return pickedTiles;}

    /**
     * @return column where the player wants to insert the tiles
     */
    public int getColumnToInsert() {
        return columnToInsert;
    }
}
