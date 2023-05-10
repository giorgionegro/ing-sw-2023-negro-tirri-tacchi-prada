package modelView;

import java.io.Serializable;
import java.util.List;


/**
 * @param pickedTiles  list of tiles picked by the player
 * @param columnToInsert column where the player wants to insert the picked tiles
 */
public record PlayerMoveInfo(List<PickedTile> pickedTiles, int columnToInsert) implements Serializable {}
