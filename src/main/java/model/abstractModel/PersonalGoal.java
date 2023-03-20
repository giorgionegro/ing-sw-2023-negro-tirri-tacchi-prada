package model.abstractModel;

import model.Tile;

public abstract class PersonalGoal {

    private String description;

    public String getDescription(){
        return description;
    }

    public abstract boolean evaluate(Tile[][] playerShelf);
}
