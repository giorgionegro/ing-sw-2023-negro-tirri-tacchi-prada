package model.instances;

import model.Tile;

import java.io.Serializable;

public record StandardShelfInstance(
     Tile[][] tiles
)implements Serializable {}
