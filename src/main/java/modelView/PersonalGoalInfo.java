package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * @param achieved - true if the goal is achieved
 * @param description - matrix rappresentation of the goal
 */
public record PersonalGoalInfo(boolean achieved, Tile[][] description) implements Serializable {
}
