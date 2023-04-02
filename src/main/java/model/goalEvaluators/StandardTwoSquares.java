package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardTwoSquares extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.printf("Two separate groups of 4 tiles same type that form a 2x2 square.\nThe tiles of the two groups must be of the same type.%n");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

        for(int i =0; i < playerShelf.length-1; i++){
            for(int j = 0; j < playerShelf[0].length-1; j++){
                if(playerShelf[i][j].getColor().equals(playerShelf[i][j+1].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+1][j].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+1][j+1].getColor())){
                    return true;
                }
            }
        }
        return false;

    }
}
