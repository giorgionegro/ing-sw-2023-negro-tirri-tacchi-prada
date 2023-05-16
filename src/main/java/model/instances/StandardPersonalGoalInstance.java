package model.instances;

import model.Tile;

import java.io.Serializable;

/**
 * This record represents a {@link model.StandardPersonalGoal} instance
 * @param tile the representation of instance's goal's tile
 * @param row the representation of instance's goal's row
 * @param column the representation of instance's goal's column
 * @param achieved the representation of instance's goal's achieved status
 */
public record StandardPersonalGoalInstance(
        Tile tile,
        int row,
        int column,
        boolean achieved
) implements Serializable {}
