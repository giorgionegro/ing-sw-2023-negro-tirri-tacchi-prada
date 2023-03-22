package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardXOfDifferentTiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Five tiles of the same typem forming an X.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
