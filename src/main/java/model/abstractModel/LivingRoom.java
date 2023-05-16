package model.abstractModel;

import model.Tile;
import modelView.LivingRoomInfo;
import util.Observable;

import java.io.Serializable;

/**
 * This is the abstract class of game living room.
 * <p>
 * It defines all the required methods needed to manage living room board.
 */
public abstract class LivingRoom extends Observable<LivingRoom.Event> {
    /**
     * This enumeration contains all the living room events that can be sent to observers
     */
    public enum Event {
        /**
         * This event is sent whenever the board has been modified
         */
        BOARD_MODIFIED
    }

    /**
     * This method return a copy of the current representation of living room board
     *
     * @return a copy of the representation of living room board
     */
    public abstract Tile[][] getBoard();

    /**
     * this method update the current representation of living room board
     *
     * @param modifiedBoard the new representation of living room board
     */
    public abstract void setBoard(Tile[][] modifiedBoard);

    /**
     * This method modify the current representation of living room board on the base of subclass implementation defined pattern
     */
    public abstract void refillBoard();

    /**
     * TODO
     * @return
     */
    public abstract LivingRoomInfo getInfo();

    public abstract Serializable getInstance();
}
