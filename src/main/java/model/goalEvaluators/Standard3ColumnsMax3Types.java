package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard3ColumnsMax3Types extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Three columns each formed by 6 tiles of one, two or three different types." +
                "Different columns may have different combinations of tile types.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
