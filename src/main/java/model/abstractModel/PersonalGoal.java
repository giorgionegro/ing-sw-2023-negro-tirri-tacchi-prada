package model.abstractModel;

import model.Tile;

public abstract class PersonalGoal {
    public abstract String getDescription();
    public abstract boolean evaluate(Tile[][] playerShelf);
}
