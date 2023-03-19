package model.abstractModel;

import model.Tile;

public interface GoalEvaluator {
    public boolean evaluate(Tile[][] playerShelf);
}
