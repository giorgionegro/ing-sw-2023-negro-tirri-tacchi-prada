package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard4Groups4Tiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Four separate groups formed each from four adjacent tiles of the same type. " +
                        "The tiles of a group can be different from those of another group.\n");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
