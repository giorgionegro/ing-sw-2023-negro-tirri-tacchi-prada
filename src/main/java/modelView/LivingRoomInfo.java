package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * @param board - board of the living room
 */
public record LivingRoomInfo(Tile[][] board) implements Serializable {}
