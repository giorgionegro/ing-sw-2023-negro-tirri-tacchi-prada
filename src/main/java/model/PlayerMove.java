package model;

import java.util.List;

public class PlayerMove {
    private List<PickedTile> pickedTiles;
    private int columnToInsert;

    public List<PickedTile> getPickedTiles(){ return pickedTiles;}

    public int getColumnToInsert() {
        return columnToInsert;
    }
}
