package model;

import java.util.List;

/**
 * Class that represents a player move000
 */
public class PlayerMove {
    /**
     * list of picked tiles
     */
    private List<PickedTile> pickedTiles;
    /**
     * column where the player wants to insert the tiles
     */
    private int columnToInsert;

    /**
     * @param pickedTiles list of picked tiles
     * @param columnToInsert column where the player wants to insert the tiles
     */
    public PlayerMove(List<PickedTile> pickedTiles, int columnToInsert) {
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
