package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard2RowsOfDifferentTiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Two lines formed each from 5 different types of tiles");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
