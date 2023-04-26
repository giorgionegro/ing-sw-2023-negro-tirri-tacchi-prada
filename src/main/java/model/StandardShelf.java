package model;

import model.abstractModel.Shelf;

import java.util.Arrays;

public class StandardShelf extends Shelf {

    private Tile[][] tiles;

    public StandardShelf(){
        this.tiles = new Tile[6][5];
    }

    @Override
    public Tile[][] getTiles() {
        return Arrays.stream(tiles).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    @Override
    public void setTiles(Tile[][] modifiedShelf) {
        this.tiles = Arrays.stream(modifiedShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        setChanged();
        notifyObservers(Event.SHELF_MODIFIED);
    }
}
