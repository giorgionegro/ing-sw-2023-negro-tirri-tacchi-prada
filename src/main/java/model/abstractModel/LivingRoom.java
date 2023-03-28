package model.abstractModel;

import model.Tile;

public interface LivingRoom {
    public Tile[][] getBoard();
    public void setBoard(Tile[][] modifiedBoard);
    public void refillBoard();
}
