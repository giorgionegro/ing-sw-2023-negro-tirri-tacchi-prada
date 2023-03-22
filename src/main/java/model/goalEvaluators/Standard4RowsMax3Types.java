package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard4RowsMax3Types extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Four lines formed each from 5 tiles of one, two or three types different. Different lines may have\n" +
                "different combinations of tile types");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
