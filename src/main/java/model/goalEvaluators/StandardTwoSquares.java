package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import static model.goalEvaluators.evaluators_utils.cloneTileMatrix;

public class StandardTwoSquares extends GoalEvaluator {
    @Override
    public String getDescription(){
        return "Two separate groups of 4 tiles same type that form a 2x2 square.\nThe tiles of the two groups must be of the same type.%n";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

        //clone the array
int counter = 0;
        Tile[][] clone = cloneTileMatrix(playerShelf);
        for(int i =0; i < clone.length-1; i++){
            for(int j = 0; j < clone[0].length-1; j++){
                if(clone[i][j].getColor().equals(clone[i][j+1].getColor()) &&
                        clone[i][j].getColor().equals(clone[i+1][j].getColor()) &&
                        clone[i][j].getColor().equals(clone[i+1][j+1].getColor())
                        && (clone[i][j]!=Tile.EMPTY)){
//if we find a group of 4 tiles of the same type, we remove them from the array
                    clone[i][j]= Tile.EMPTY;
                    clone[i][j+1]= Tile.EMPTY;
                    clone[i+1][j]= Tile.EMPTY;
                    clone[i+1][j+1]= Tile.EMPTY;
                    counter++;
                }
            }
        }

        return counter >= 2;

    }


}
