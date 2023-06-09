package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * @param playerId player id
 * @param shelf   shelf of the player
 */
public record ShelfInfo(String playerId, Tile[][] shelf) implements Serializable {}
