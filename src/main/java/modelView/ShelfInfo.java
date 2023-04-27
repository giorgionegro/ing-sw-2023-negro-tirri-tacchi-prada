package modelView;

import model.Tile;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @param playerId player id
 * @param shelf   shelf of the player
 */
public record ShelfInfo(String playerId, Tile[][] shelf) implements Serializable {
    public ShelfInfo(String playerId, Tile[][] shelf) {
        this.playerId = playerId;
        this.shelf = Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }
}
