package view.graphicInterfaces;
import model.Tile;

/**
 * This interface represents the graphics for a player's shelf.
 * It provides a method to update the information of the shelf when the user inserts the tiles.
 */
public interface PlayerShelfGraphics {
    /**
     * This method updates the information of the user's shelf with the specified parameters:
     @param playerId id of the player that owns the shelf
     @param shelf shelf representation of the player
     */
    void updatePlayerShelfGraphics(String playerId, Tile[][] shelf);
}
