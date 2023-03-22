package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard8TilesSameType extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Eight tiles of the same type. There are restrictions on the location of these tiles.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
