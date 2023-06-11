package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * This record contains information about the state of a {@link model.abstractModel.LivingRoom}
 * @param board board of the living room
 */
public record LivingRoomInfo(Tile[][] board) implements Serializable {}
