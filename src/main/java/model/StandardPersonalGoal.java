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

    /**
     * Tells the description of the Personal Goal
     * @return a string with the description of the personal Goal
     */
    @Override
    public String getDescription() {
        return "Put Tile " + this.Tile.getColor() + " in row: " + this.row + " and column " + this.column;
    }

    /**
     * This method evaluates if the player achieved the personal goal
     * @param playerShelf  matrix of Tiles representing player shelf
     * @return true if the player achieved the goal, false otherwise.
     */
    @Override
    public boolean evaluate(model.Tile[][] playerShelf) {
        if(playerShelf.length < this.row && playerShelf[0].length < this.column){
            throw new IndexOutOfBoundsException("Shelf not big enough, cannot achieved the goal");
        }
        if(playerShelf[row][column] == null){
            throw new NullPointerException("Cella vuota");
        }
        return (this.Tile.getColor()).equals(playerShelf[row][column].getColor());
    }
}
