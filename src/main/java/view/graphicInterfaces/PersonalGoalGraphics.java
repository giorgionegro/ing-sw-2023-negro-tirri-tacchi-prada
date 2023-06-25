package view.graphicInterfaces;
import model.Tile;

/**
 * This interface represents the graphics for a personal goal.
 * It provides a method to update the information of the personal goal.
 */
@FunctionalInterface
public interface PersonalGoalGraphics {
    /**
     * This method updates the information of the personal goal with the specified parameters:
     * @param id the id of this goal, unique among others player personal goal
     * @param hasBeenAchieved true if the goal is achieved
     * @param description     matrix representation of the goal
     */
    void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description);
}
