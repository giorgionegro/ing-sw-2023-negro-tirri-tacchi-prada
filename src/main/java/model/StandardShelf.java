package model;

import model.abstractModel.Shelf;

import java.util.Arrays;

public class StandardShelf extends Shelf {

    Tile[][] shelf;

    public StandardShelf(){
        this.shelf = new Tile[6][5];
    }

    @Override
    public Tile[][] getShelf() {
        return Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    @Override
    public void setShelf(Tile[][] modifiedShelf) {
        this.shelf = Arrays.stream(modifiedShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        setChanged();
        notifyObservers(Event.SHELF_MODIFIED);
    }
}
