package model.abstractModel;

import model.Tile;
import modelView.ShelfInfo;
import util.Observable;

import java.io.Serializable;

/**
 * This is the abstract class of game living room.
 * <p>
 * It defines all the required methods needed to manage living room board.
 */
public abstract class Shelf extends Observable<Shelf.Event> {
    /**
     * This enumeration contains all the living room events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent whenever the board has been modified
         */
        SHELF_MODIFIED
    }

    /**
     * This method return a copy of the current representation of living room board
     *
     * @return a copy of the representation of living room board
     */
    public abstract Tile[][] getTiles();

    /**
     * this method update the current representation of living room board
     *
     * @param modifiedShelf the new representation of living room board
     */
    public abstract void setTiles(Tile[][] modifiedShelf);

    /**
     * TODO
     * @return
     */
    public abstract ShelfInfo getInfo(String playerId);


    public abstract Serializable getInstance();
}
