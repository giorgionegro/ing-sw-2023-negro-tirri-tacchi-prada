package modelView;

import model.Tile;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @param board - board of the living room
 */
public record LivingRoomInfo(Tile[][] board) implements Serializable {
    public LivingRoomInfo(Tile[] @NotNull [] board) {
        this.board = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }
}
