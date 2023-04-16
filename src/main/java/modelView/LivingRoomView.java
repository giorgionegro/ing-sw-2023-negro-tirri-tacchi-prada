package modelView;

import model.Tile;

import java.io.Serializable;
import java.util.Arrays;

public class LivingRoomView implements Serializable {
    private final Tile[][] board;
    public LivingRoomView(Tile[][] board){
        this.board = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    public Tile[][] getBoard() {
        return board;
    }
}
