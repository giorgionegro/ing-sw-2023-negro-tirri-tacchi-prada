package modelView;

import model.Tile;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @param board - board of the living room
 */
public record LivingRoomInfo(Tile[][] board) implements Serializable {
    public LivingRoomInfo(Tile[][] board) {
        this.board = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }
}
