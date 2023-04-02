package model.goalEvaluators;

import model.Tile;

public class evaluatorsutil {
    static Tile[][] cloneTile(Tile[][] playerShelf) {
        Tile[][] clone = new Tile[playerShelf.length][playerShelf[0].length];
        for(int i = 0; i < playerShelf.length; i++){
            for(int j = 0; j < playerShelf[0].length; j++){
                clone[i][j] = playerShelf[i][j];
            }
        }
        return clone;
    }
}
