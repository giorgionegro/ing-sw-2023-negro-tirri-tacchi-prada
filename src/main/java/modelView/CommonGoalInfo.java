package modelView;

import model.Token;

import java.io.Serializable;

/**
 * This record contains information about the state of a {@link model.abstractModel.CommonGoal}
 * @param id the unique id of the common goal
 * @param description the description of common goal specs
 * @param tokenState the current token value of the common goal
 */
public record CommonGoalInfo(String id, String description, Token tokenState) implements Serializable {}
