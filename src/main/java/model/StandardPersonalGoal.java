package model;

import model.abstractModel.PersonalGoal;

/**
 * This class is an implementation of {@link PersonalGoal}.
 * <p>
 * In order to achieve this personal goal there must be a particular {@link #tile} in a particular position in the shelf, defined as {@link #row} and {@link #column}
 */
public  class StandardPersonalGoal extends PersonalGoal {
    /**
     * Tile type target of the goal
     */
    private final Tile tile;
    /**
     * Shelf's row target of the goal
     */
    private final int row;
    /**
     * Shelf's column target of the goal
     */
    private final int column;
    /**
     * Signal of achieved goal
     */
    private boolean achieved;

    /**
     * Constructor of StandardPersonalGoal not achieved and parameterized with given tile, row and column
     * @param tile tile type target of the goal
     * @param row row target of the goal
     * @param column column target of the goal
     */
    public StandardPersonalGoal(model.Tile tile, int row, int column) {
        this.tile = tile;
        this.row = row;
        this.column = column;
        this.achieved = false;
    }

    /**
     * {@inheritDoc}
     *
     * @return a Matrix with the description of the personal Goal
     */
    public Tile[][] getDescription(Tile[][] playerShelf) {
        int i, j;
        Tile[][] ShelfPersonalGoal = new Tile[playerShelf.length][];
        for(i=0; i < playerShelf.length; i ++){
            for(j=0; j < playerShelf[0].length; j++){
                if(this.row == i && this.column == j){
                   ShelfPersonalGoal[i][j] = Tile.valueOf(this.tile.getColor());
                }
            }
        }
        return ShelfPersonalGoal;
 }


    @Override
    public Tile[][] getDescription() {
        return new Tile[0][];
    }

    /**
     * {@inheritDoc}
     * @return {@link #achieved}
     */
    @Override
    public boolean isAchieved() {
        return achieved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAchieved() {
        achieved = true;
        setChanged();
        notifyObservers(Event.GOAL_ACHIEVED);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation tests if there is a {@link Tile} equivalent to {@link #tile} in given shelf representation at
     * the position defined by {@link #row} and {@link #column}
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
        return (this.tile.getColor()).equals(playerShelf[row][column].getColor());
    }
}
