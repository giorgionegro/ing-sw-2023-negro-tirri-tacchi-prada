package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard4Groups4Tiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.printf("Four separate groups formed each from four adjacent tiles of the same type. The tiles of a group can be different from those of another group.\n%n");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

        int counter = 0; // counts the number of groups of 4 tiles of the same type
                //first we check the horizontal groups
        for (Tile[] tiles : playerShelf) {
            for (int j = 0; j < playerShelf[0].length - 3; j++) {
                if (tiles[j].getColor().equals(tiles[j + 1].getColor()) &&
                        tiles[j].getColor().equals(tiles[j + 2].getColor()) &&
                        tiles[j].getColor().equals(tiles[j + 3].getColor())) {
                    counter++;
                }
            }
        }
//then we check the vertical groups
        for(int i = 0; i < playerShelf.length-3; i++){
            for(int j = 0; j < playerShelf[0].length; j++){
                if(playerShelf[i][j].getColor().equals(playerShelf[i+1][j].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+2][j].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+3][j].getColor())){
                    counter++;
                }
            }
        }
        return counter >= 4;

    }
}
