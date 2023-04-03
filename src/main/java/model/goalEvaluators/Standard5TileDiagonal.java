package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class Standard5TileDiagonal extends GoalEvaluator {

    @Override
    public String getDescription(){

        return "Five tiles of the same type that form a diagonal.";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

        // first we check the diagonal from top left to bottom righ
        for(int i = 0; i < playerShelf.length-4; i++){
            for(int j = 0; j < playerShelf[0].length-4; j++){
                if(playerShelf[i][j].getColor().equals(playerShelf[i+1][j+1].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+2][j+2].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+3][j+3].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+4][j+4].getColor())&&
                        (playerShelf[i][j]!=Tile.EMPTY)
                ){
                    return true;
                }
            }
        }
        // then we check the diagonal from top right to bottom left
        for(int i = 0; i < playerShelf.length-4; i++){
            for(int j = 4; j < playerShelf[0].length; j++){
                if(playerShelf[i][j].getColor().equals(playerShelf[i+1][j-1].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+2][j-2].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+3][j-3].getColor()) &&
                        playerShelf[i][j].getColor().equals(playerShelf[i+4][j-4].getColor()) &&
                        (playerShelf[i][j]!=Tile.EMPTY)){
                    return true;
                }
            }
        }

        return false;

    }
}
