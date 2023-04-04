package model.goalEvaluators;

import model.Tile;





public class evaluators_utils {
    static Tile[][] cloneTileMatrix(Tile[][] playerShelf) {
        Tile[][] result = new Tile[playerShelf.length][playerShelf[0].length];
        for(int i = 0; i < playerShelf.length; i++){
            System.arraycopy(playerShelf[i], 0, result[i], 0, playerShelf[0].length);
        }
        return result;
    }
}
