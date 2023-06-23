package view.graphicInterfaces;
import model.Token;
import java.util.Map;
public interface PlayerInfoGraphics {
    void updateAchievedCommonGoals(Map<String,Token> achievedCommonGoals);
    void updateErrorState(String reportedError);
}
