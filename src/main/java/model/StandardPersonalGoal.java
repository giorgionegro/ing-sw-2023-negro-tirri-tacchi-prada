package model;

import model.abstractModel.PersonalGoal;

public class StandardPersonalGoal extends PersonalGoal {
    private Tile Tile;
    private int row;
    private int column;

    @Override
    public boolean evaluate(model.Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
