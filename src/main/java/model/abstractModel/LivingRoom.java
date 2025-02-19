package model.abstractModel;

import model.Tile;
import modelView.LivingRoomInfo;
import util.Observable;

/**
 * This is the abstract class of game living room.
 * <p>
 * It defines all the required methods needed to manage living room board.
 */
public abstract class LivingRoom extends Observable<LivingRoom.Event>{
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
     * This method returns a copy of the current representation of living room board
     *
     * @return a copy of the representation of living room board
     */
    public abstract Tile[][] getBoard();

    /**
     * this method updates the current representation of living room board
     *
     * @param modifiedBoard the new representation of living room board
     */
    public abstract void setBoard(Tile[][] modifiedBoard);

    /**
     * This method modifies the current representation of living room board on the base of subclass implementation defined pattern
     */
    public abstract void refillBoard();

    /**
     * This method returns a {@link LivingRoomInfo} representing this object instance
     * @return A {@link LivingRoomInfo} representing this object instance
     */
    public abstract LivingRoomInfo getInfo();
}
