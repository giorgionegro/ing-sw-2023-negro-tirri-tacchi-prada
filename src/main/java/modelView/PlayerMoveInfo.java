package modelView;

import java.io.Serializable;
import java.util.List;


/**
 * This record contains information about a move made by a player
 * @param pickedTiles list of tiles picked by the player
 * @param columnToInsert column of the shelf where the player wants to insert the picked tiles
 */
public record PlayerMoveInfo(List<PickedTile> pickedTiles, int columnToInsert) implements Serializable {}
