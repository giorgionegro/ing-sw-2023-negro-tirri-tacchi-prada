package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardXOfDifferentTiles extends GoalEvaluator {
    @Override
    public String getDescription(){

       return "Five tiles of the same typem forming an X.";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        /* EXAMPLE:
         X 0 X
         0 X 0
         X 0 X
         */
        for(int i = 0; i < playerShelf.length-2; i++){
            for(int j = 1; j < playerShelf[0].length-2; j++){
                if(playerShelf[i][j].getColor().equals(playerShelf[i+2][j].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+1][j+1].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i][j+2].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+2][j+2].getColor())
                        && !playerShelf[i][j].getColor().equals("White")
                ){
                    return true;
                }
            }

        }
        return false;
    }
}
