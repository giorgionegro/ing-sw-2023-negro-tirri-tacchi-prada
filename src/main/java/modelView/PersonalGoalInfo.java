package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * This record contains information about the state of a {@link model.abstractModel.PersonalGoal}
 * @param achieved true if the goal is achieved
 * @param description matrix representation of the goal
 */
public record PersonalGoalInfo(boolean achieved, Tile[][] description) implements Serializable {}
