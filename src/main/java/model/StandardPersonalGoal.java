package model;

import model.abstractModel.PersonalGoal;

public class StandardPersonalGoal extends PersonalGoal {
    private final Tile Tile;
    private final int row;
    private final int column;

    public StandardPersonalGoal(model.Tile tile, int row, int column) {
        Tile = tile;
        this.row = row;
        this.column = column;
    }

    @Override
    public void getDescription() {
        System.out.println("Put Tile " + this.Tile.getColor() + " in row: " + this.row + " and column " + this.column);
    }

    @Override
    public boolean evaluate(model.Tile[][] playerShelf) {
        if(playerShelf.length < this.row && playerShelf[0].length < this.column){
            throw new UnsupportedOperationException("Shelf not big enough, cannot achieved the goal");//TODO CAMBIARE IL TIPO DI ERRORE
        }
        return (this.Tile.getColor()).equals(playerShelf[row][column].getColor());
    }
}
