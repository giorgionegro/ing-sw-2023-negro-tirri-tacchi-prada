package model.abstractModel;

import model.Tile;

public abstract class GoalEvaluator {

    public abstract boolean evaluate(Tile[][] playerShelf);

    public abstract String getDescription();
}
