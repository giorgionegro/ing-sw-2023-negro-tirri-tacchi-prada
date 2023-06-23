package view.graphicInterfaces;
import model.Token;
import java.util.Map;


/**
 * This interface represents the graphics for a common goal completed and an error reported.
 * It provides two methods to update the information of the player's info during the game.
 */
public interface PlayerInfoGraphics {
    /**This method updates the information of the player achieving a common goal with the specified parameter:
     * @param achievedCommonGoals map of achieved common goals with earned token
     */
    void updateAchievedCommonGoals(Map<String,Token> achievedCommonGoals);

    /**This method updates the information of an error reported during the match with the specified parameter:
     * @param reportedError message of en error encountered during gameplay
     */
    void updateErrorState(String reportedError);
}
