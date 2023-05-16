package model.instances;

import model.Tile;

import java.io.Serializable;
import java.util.Stack;

public record StandardLivingRoomInstance (
        Tile[][] board,
        Stack<Tile> bag,
        int numberOfPlayers
)implements Serializable {}
