package modelView;

import java.io.Serializable;

/**
 * @param achieved - true if the goal is achieved
 * @param description - description of the goal //TODO cambiare rappresentazione personal GOAL
 */
//TODO cambiare rappresentazione personal GOAL
public record PersonalGoalInfo(boolean achieved, String description) implements Serializable {
}
