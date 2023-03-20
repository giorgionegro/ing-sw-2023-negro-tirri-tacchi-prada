package model.abstractModel;

import model.Tile;

public abstract class GoalEvaluator {
    private String description;

    public abstract boolean evaluate(Tile[][] playerShelf);

    public String getDescription(){
        return description;
    }
}
