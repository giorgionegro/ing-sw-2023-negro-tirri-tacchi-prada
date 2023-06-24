package view.graphicInterfaces;

import model.Tile;
/**
 * This interface represents the graphics for living room board
 * It provides a method to update the information of the board when the users are playing the game.
 */
public interface LivingRoomGraphics {
    /** This method updates the information of the board with the specified parameter:
     * @param board board of the living room
     */
    void updateBoardGraphics(Tile[][] board);
}
