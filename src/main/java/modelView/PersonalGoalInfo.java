package modelView;

import model.Tile;

import java.io.Serializable;

/**
 * This record contains information about the state of a {@link model.abstractModel.PersonalGoal}
 * @param id the id of this goal, unique among others player personal goal
 * @param achieved true if the goal is achieved
 * @param description matrix representation of the goal
 */
public record PersonalGoalInfo(int id, boolean achieved, Tile[][] description) implements Serializable {}
