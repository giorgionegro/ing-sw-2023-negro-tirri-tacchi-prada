package model;

import model.abstractModel.LivingRoom;

import java.util.ArrayList;
import java.util.List;

public class StandardLivingRoom implements LivingRoom {
    private Tile[][] board;
    private List<Tile> bag;

    public void loadTable(){
        //TODO ?????
    }
    private List<Tile> loadBag(){
        return new ArrayList<>();//TODO aggiungere tiles e fare sorting casuale
    }

    public Tile[][] getBoard() {
        //TODO passare una copia di board
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void setBoard(Tile[][] modifiedBoard) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void refillBoard() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
