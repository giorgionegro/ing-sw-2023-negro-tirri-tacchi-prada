package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardCorners extends GoalEvaluator {
    @Override
    public String getDescription(){

        return "4 tiles of the same type in the 4 corners of the library\n";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        Tile topLeft = playerShelf[0][0];
        Tile topRight = playerShelf[0][playerShelf[0].length-1];
        Tile bottomLeft = playerShelf[playerShelf.length-1][0];
        Tile bottomRight = playerShelf[playerShelf.length-1][playerShelf[0].length-1];
        return topLeft.getColor().equals(topRight.getColor()) &&
                topLeft.getColor().equals(bottomLeft.getColor()) &&
                topLeft.getColor().equals(bottomRight.getColor()) &&
                !topLeft.getColor().equals("White");
    }
}
