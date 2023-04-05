package model.goalEvaluators;

import model.Tile;

import java.util.Arrays;


// javadoc

/**
 * This a utility class for the goal evaluators containing some useful methods
 **/
public class evaluators_utils {

    /**
     * This method clones a matrix of tiles
     * @param playerShelf the matrix to be cloned
     * @return the cloned matrix
     **/

    static Tile[][] cloneTileMatrix(Tile[][] playerShelf) {
        return Arrays.stream(playerShelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }
}
