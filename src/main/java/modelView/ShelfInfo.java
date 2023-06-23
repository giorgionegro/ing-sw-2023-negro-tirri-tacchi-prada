package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * This record contains information about the state of a player {@link model.abstractModel.Shelf}
 * @param playerId id of the player that owns the shelf
 * @param shelf shelf representation of the player
 */
public record ShelfInfo(String playerId, Tile[][] shelf) implements Serializable {}
