package model.instances;

import model.Tile;

import java.io.Serializable;

/**
 * This record represents a {@link model.StandardShelf} instance
 * @param tiles the representation of instance's tiles
 */
public record StandardShelfInstance(
     Tile[][] tiles
)implements Serializable {}
