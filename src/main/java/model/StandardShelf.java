package model;

import model.abstractModel.Shelf;

import java.util.Arrays;

public class StandardShelf extends Shelf {

    /**
     * 2d array of Tiles that represents the shelf
     */
    Tile[][] shelf;

    /**
     * Class constructor, initializes shelf with an empty 6 by 5 2d array pf Tiles
     */
    public StandardShelf(){
        this.shelf = new Tile[6][5];
    }

    /**
     * This method returns a copy of the {@link #shelf}
     * @return a 2d array representing the shelf
     */
    @Override
    public Tile[][] getShelf() {
        return Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * This method sets the shelf as a copy of modifiedShelf, calls the {@link #setChanged()} and notifies the observers that the shelf has changed
     * @param modifiedShelf the new representation of the shelf
     */
    @Override
    public void setShelf(Tile[][] modifiedShelf) {
        this.shelf = Arrays.stream(modifiedShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        setChanged();
        notifyObservers(ShelfEvent.SHELF_MODIFIED);
    }
}
