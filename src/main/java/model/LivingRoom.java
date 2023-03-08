package model;

import java.util.ArrayList;
import java.util.List;

public class LivingRoom {
    private Tile[][] board;
    private List<Tile> bag;

    public LivingRoom(){
        this.board = new Tile[9][9];
        this.bag = new ArrayList<>();
    }

    public Tile[][] getBoard() {
        //TODO passare una copia di board
        throw new UnsupportedOperationException("not implemented yet");
    }
}
