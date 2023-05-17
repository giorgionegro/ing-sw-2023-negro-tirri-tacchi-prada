package model;

import model.abstractModel.PersonalGoal;
import model.instances.StandardPersonalGoalInstance;
import modelView.PersonalGoalInfo;

import java.io.Serializable;
import java.util.Arrays;

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

    public StandardPersonalGoal(StandardPersonalGoalInstance instance){
        this.tile = instance.tile();
        this.row = instance.row();
        this.column = instance.column();
        this.achieved = instance.achieved();
    }

    /**
     * {@inheritDoc}
     *
     * @return a Matrix with the description of the personal Goal
     */
    @Override
    public Tile[][] getDescription() {
        //shelf dimension 6x5
        Tile[][] ShelfPersonalGoal = new Tile[6][5];
        //fill the shelf with empty tiles
        Arrays.stream(ShelfPersonalGoal).forEach(row -> Arrays.fill(row, Tile.EMPTY));
        ShelfPersonalGoal[this.row][this.column] = this.tile;
        return ShelfPersonalGoal;
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
        if(playerShelf[row][column] == Tile.EMPTY){
            return false;
        }
        return (this.tile.getColor()).equals(playerShelf[row][column].getColor());
    }

    /**
     * {@inheritDoc}
     * @return A {@link PersonalGoalInfo} representing this object instance
     */
    @Override
    public PersonalGoalInfo getInfo() {
        return new PersonalGoalInfo(achieved, getDescription());
    }

    /**
     * {@inheritDoc}
     * @return A {@link StandardPersonalGoalInstance} constructed using instance values
     */
    @Override
    public Serializable getInstance() {
        return new StandardPersonalGoalInstance(tile,row,column,achieved);
    }
}
