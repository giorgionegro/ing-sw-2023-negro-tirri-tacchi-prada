package modelView;

import java.io.Serializable;

public class PersonalGoalInfo implements Serializable {

    private final boolean achieved;
    private final String description;

    public PersonalGoalInfo(boolean achieved, String description) {
        this.achieved = achieved;
        this.description = description;//TODO cambiare rappresentazione personal GOAL
    }

    public boolean isAchieved() {
        return achieved;
    }

    public String getDescription() {
        return description;
    }
}
