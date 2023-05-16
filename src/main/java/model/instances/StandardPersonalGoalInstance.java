package model.instances;

import model.Tile;

import java.io.Serializable;

public record StandardPersonalGoalInstance(
        Tile tile,
        int row,
        int column,
        boolean achieved
) implements Serializable {}
