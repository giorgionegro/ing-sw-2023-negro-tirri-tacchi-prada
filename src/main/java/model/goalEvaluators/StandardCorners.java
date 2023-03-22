package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardCorners extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("4 tiles of the same type in the 4 corners of the library\n");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
