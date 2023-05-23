package modelView;

import model.Token;

import java.io.Serializable;

/**
 * @param description - description of the goal
 * @param tokenState - token state of the goal
 */
public record CommonGoalInfo(String id, String description, Token tokenState) implements Serializable {}
