package model.abstractModel;

import model.Tile;
import modelView.ShelfInfo;
import util.Observable;

/**
 * This is the abstract class of game living room.
 * <p>
 * It defines all the required methods needed to manage living room board.
 */
public abstract class Shelf extends Observable<Shelf.Event>{
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
     * This method returns a copy of the current representation of living room board
     *
     * @return a copy of the representation of living room board
     */
    public abstract Tile[][] getTiles();

    /**
     * this method updates the current representation of living room board
     *
     * @param modifiedShelf the new representation of living room board
     */
    public abstract void setTiles(Tile[][] modifiedShelf);

    /**
     * This method returns a {@link ShelfInfo} representing this object instance
     * @param playerId the id of the player requesting the info
     * @return A {@link ShelfInfo} representing this object instance
     */
    public abstract ShelfInfo getInfo(String playerId);
}
