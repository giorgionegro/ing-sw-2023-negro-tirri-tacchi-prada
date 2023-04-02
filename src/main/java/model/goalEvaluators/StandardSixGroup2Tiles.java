package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import static model.goalEvaluators.evaluatorsutil.cloneTile;

public class StandardSixGroup2Tiles extends GoalEvaluator {
    @Override
    public String getDescription(){
        return "Six separate groups formed each\nfrom two adjacent tiles of the same type. The tiles of a group can\nbe different from those of another group.%n";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

            int counter = 0; // counts the number of groups of 2 tiles of the same type
        //clone
        Tile[][] clone = cloneTile(playerShelf);
        //first we check the horizontal groups
        for (Tile[] tiles : clone) {
            for (int j = 0; j < clone[0].length - 1; j++) {
                if (tiles[j].getColor().equals(tiles[j + 1].getColor()) &&
                        !(tiles[j]!=Tile.EMPTY)){
                    //if we find a group of 2 tiles of the same type, we remove them from the array
                    tiles[j]= Tile.EMPTY;
                    tiles[j+1]= Tile.EMPTY;
                    counter++;
                }
            }
        }
            //then we check the vertical groups
            for(int i = 0; i < clone.length-1; i++)
                for (int j = 0; j < clone[0].length; j++) {
                    if (clone[i][j].getColor().equals(clone[i + 1][j].getColor()) &&
                            !(clone[i][j]!=Tile.EMPTY)) {
                        //if we find a group of 2 tiles of the same type, we remove them from the array
                        clone[i][j] = Tile.EMPTY;
                        clone[i + 1][j] = Tile.EMPTY;
                        counter++;
                    }
                }
            return counter >= 6;
    }
}
