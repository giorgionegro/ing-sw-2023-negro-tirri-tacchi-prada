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

        int counter = 0; // counts the number of groups of 4 tiles of the same type
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
