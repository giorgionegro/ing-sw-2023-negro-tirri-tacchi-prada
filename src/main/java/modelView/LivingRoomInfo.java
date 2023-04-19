package modelView;

import model.Tile;

import java.io.Serializable;
import java.util.Arrays;

public class LivingRoomInfo implements Serializable {
    private final Tile[][] board;
    public LivingRoomInfo(Tile[][] board){
        this.board = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    public Tile[][] getBoard() {
        return board;
    }
}
