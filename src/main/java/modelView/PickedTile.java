package modelView;

import java.io.Serializable;


/**
 * @param row row of picked tile
 * @param col column of picked tile
 */
public record PickedTile(int row, int col) implements Serializable {


}

