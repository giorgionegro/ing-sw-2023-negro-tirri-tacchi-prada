package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard5TileDiagonal extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Five tiles of the same type that form a diagonal.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
