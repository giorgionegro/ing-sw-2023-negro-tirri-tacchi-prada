package model.instances;

import model.Tile;

import java.io.Serializable;
import java.util.Stack;

/**
 * This record represents a {@link model.StandardLivingRoom} instance
 * @param board the representation of instance's living room board
 * @param bag the representation of instance's living room bag
 * @param numberOfPlayers the representation of instance's number of game's players
 */
public record StandardLivingRoomInstance (
        Tile[][] board,
        Stack<Tile> bag,
        int numberOfPlayers
)implements Serializable {}
