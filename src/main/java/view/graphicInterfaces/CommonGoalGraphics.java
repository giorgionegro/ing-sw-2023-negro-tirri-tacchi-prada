package view.graphicInterfaces;

/**
 * This interface represents the graphics for a common goal.
 * It provides a method to update the information of the common goal.
 */
public interface CommonGoalGraphics {
    /**
     * This method updates the information of the common goal with the specified parameters:
     * @param id the unique id of the common goal
     * @param description the description of common goal specs
     * @param tokenState the current token value of the common goal
     */
    void updateCommonGoalGraphics(String id, String description, model.Token tokenState);
}
