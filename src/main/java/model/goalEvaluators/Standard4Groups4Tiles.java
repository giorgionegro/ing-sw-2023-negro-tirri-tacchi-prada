package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;

/**
 * This class is the goal evaluator for the Standard4Groups4Tiles goal
 * It implements the GoalEvaluator abstract class
 * It is used by the CommonGoal class
 **/
public class
Standard4Groups4Tiles extends GoalEvaluator {
    /**
     * @return the description of the goal
     */
    @Override
    public String getDescription(){
         return "Four separate groups formed each from four adjacent tiles of the same type. The tiles of a group can be different from those of another group.\n%n";
    }

    /**
     * @param playerShelf the player's shelf to evaluate
     * @return true if the player has at least 4 groups of 4 tiles of the same type
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        //clone the array
        Tile[][] clone = Arrays.stream(playerShelf).map(Tile[]::clone).toArray(Tile[][]::new);
        int counter = 0; // counts the number of groups of 4 tiles of the same type
                //first we check the horizontal groups
        for (Tile[] tiles : clone) {
            for (int j = 0; j < playerShelf[0].length - 3; j++) {
                if (tiles[j].getColor().equals(tiles[j + 1].getColor()) &&
                        tiles[j].getColor().equals(tiles[j + 2].getColor()) &&
                        tiles[j].getColor().equals(tiles[j + 3].getColor()) &&
                        (tiles[j]!=Tile.EMPTY)
                ) {
                    //if we find a group of 4 tiles of the same type, we remove them from the array
                    tiles[j]= Tile.EMPTY;
                    tiles[j+1]= Tile.EMPTY;
                    tiles[j+2]= Tile.EMPTY;
                    tiles[j+3]= Tile.EMPTY;
                    counter++;
                }
            }
        }
//then we check the vertical groups
        for(int i = 0; i < clone.length-3; i++){
            for(int j = 0; j < clone[0].length; j++){
                if(clone[i][j].getColor().equals(clone[i+1][j].getColor()) &&
                        clone[i][j].getColor().equals(clone[i+2][j].getColor()) &&
                        clone[i][j].getColor().equals(clone[i+3][j].getColor()) &&
                        clone[i][j]!=Tile.EMPTY ){
                    //if we find a group of 4 tiles of the same type, we remove them from the array
                    clone[i][j]= Tile.EMPTY;
                    clone[i+1][j]= Tile.EMPTY;
                    clone[i+2][j]= Tile.EMPTY;
                    clone[i+3][j]= Tile.EMPTY;
                    counter++;
                }
            }
        }
        return counter >= 4;

    }
}
