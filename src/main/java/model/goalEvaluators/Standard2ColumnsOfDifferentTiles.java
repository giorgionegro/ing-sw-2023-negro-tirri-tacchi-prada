package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard2ColumnsOfDifferentTiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Two columns formed each from 6 different types of tiles");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
