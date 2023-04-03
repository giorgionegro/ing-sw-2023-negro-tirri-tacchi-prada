package model.goalEvaluators;

import model.Tile;


//@ model import org.jmlspecs.lang.*;
//@ requires playerShelf != null && playerShelf.length > 0 && playerShelf[0].length > 0 && /forall int i; 0 <= i && i < playerShelf.length; /forall int j; 0 <= j && j < playerShelf[0].length; playerShelf[i] != null && playerShelf[i][j] != null;
//@ ensures \result != null && \result.length == playerShelf.length && \result[0].length == playerShelf[0].length && /forall int i; 0 <= i && i < \result.length; /forall int j; 0 <= j && j < \result[0].length; \result[i][j] != null && \result[i][j].equals(playerShelf[i][j]);
public class evaluators_utils {
    static Tile[][] cloneTileMatrix(Tile[][] playerShelf) {
        Tile[][] result = new Tile[playerShelf.length][playerShelf[0].length];
        for(int i = 0; i < playerShelf.length; i++){
            System.arraycopy(playerShelf[i], 0, result[i], 0, playerShelf[0].length);
        }
        return result;
    }
}
