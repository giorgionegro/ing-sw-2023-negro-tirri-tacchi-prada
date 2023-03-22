package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardTwoSquares extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Two separate groups of 4 tiles same type that form a 2x2 square.\n" +
                "The tiles of the two groups must be of the same type.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
