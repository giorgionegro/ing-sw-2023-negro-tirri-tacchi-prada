package modelView;

import java.io.Serializable;


/**
 * This record contains information about a {@link model.Tile} picked from a {@link model.abstractModel.LivingRoom}
 * @param row row of picked tile
 * @param col column of picked tile
 */
public record PickedTile(int row, int col) implements Serializable {}

