package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardStairs extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Five columns of increasing height or descending: starting from the first column\n" +
                "left or right, each successive column it must be formed by an extra tile.\n" +
                "Tiles can be of any type.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
