package modelView;

import model.Tile;

import java.io.Serializable;
import java.util.Arrays;

public class ShelfView implements Serializable {

    private final Tile[][] shelf;

    private final String playerId;

    public ShelfView(String playerId, Tile[][] shelf){
        this.playerId = playerId;
        this.shelf = Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }
    public Tile[][] getShelf() {
        return shelf;
    }

    public String getPlayerId() {
        return playerId;
    }
}
