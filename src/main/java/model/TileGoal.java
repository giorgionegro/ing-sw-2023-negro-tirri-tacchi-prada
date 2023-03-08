package model;

public class TileGoal {
    private Tile Tile;

    private int row;
    private int column;

    public TileGoal(Tile Tile, int row, int column){
        this.Tile = Tile;
        this.row = row;
        this.column = column;
    }

    public Tile getTile() {
        return Tile;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
