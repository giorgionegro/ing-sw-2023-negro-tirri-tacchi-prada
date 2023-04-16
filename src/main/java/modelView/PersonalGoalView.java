package modelView;

import java.io.Serializable;

public class PersonalGoalView implements Serializable {

    private final boolean achieved;
    private final String description;

    public PersonalGoalView(boolean achieved, String description) {
        this.achieved = achieved;
        this.description = description;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public String getDescription() {
        return description;
    }
}
