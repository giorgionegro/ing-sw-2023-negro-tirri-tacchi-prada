package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardSixGroup2Tiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Six separate groups formed each\n" +
                "from two adjacent tiles of the same type. The tiles of a group can\n" +
                "be different from those of another group.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
