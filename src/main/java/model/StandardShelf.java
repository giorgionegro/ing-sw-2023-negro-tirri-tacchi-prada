package model;

import model.abstractModel.Shelf;
import model.instances.StandardShelfInstance;
import modelView.ShelfInfo;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

public class StandardShelf extends Shelf {

    /**
     * 2d array of Tiles that represents the shelf
     */
    private Tile[][] tiles;
    /**
     * Class constructor, initializes shelf with an empty 6 by 5 2d array pf Tiles
     */
    public StandardShelf(){
        this.tiles = new Tile[6][5];
        for(Tile[] a : tiles)
            Arrays.fill(a,Tile.EMPTY);
    }

    public StandardShelf(@NotNull StandardShelfInstance instance){
        this.tiles = instance.tiles();
    }

    /**
     * This method returns a copy of {@link #tiles}
     * @return a 2d array representing the shelf
     */
    @Override
    public Tile[] @NotNull [] getTiles() {
        return Arrays.stream(tiles).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * This method sets the shelf as a copy of modifiedShelf, calls the {@link #setChanged()} and notifies the observers that the shelf has changed
     * @param modifiedShelf the new representation of the shelf
     */
    @Override
    public void setTiles(Tile[] @NotNull [] modifiedShelf) {
        this.tiles = Arrays.stream(modifiedShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        setChanged();
        notifyObservers(Event.SHELF_MODIFIED);
    }

    /**
     * {@inheritDoc}
     * @param playerId the player id associated with this {@link StandardShelf} instance
     * @return A {@link ShelfInfo} representing this object instance
     */
    @Override
    public @NotNull ShelfInfo getInfo(String playerId) {
        return new ShelfInfo(playerId, getTiles());
    }

    /**
     * {@inheritDoc}
     * @return A {@link StandardShelfInstance} constructed using instance values
     */
    @Override
    public @NotNull Serializable getInstance(){
        return new StandardShelfInstance(tiles);
    }
}
