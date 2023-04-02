package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

public class StandardSixGroup2Tiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.printf("Six separate groups formed each\nfrom two adjacent tiles of the same type. The tiles of a group can\nbe different from those of another group.%n");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {

            int counter = 0; // counts the number of groups of 2 tiles of the same type
            //first we check the horizontal groups
        for (Tile[] tiles : playerShelf) {
            for (int j = 0; j < playerShelf[0].length - 1; j++) {
                if (tiles[j].getColor().equals(tiles[j + 1].getColor())) {
                    counter++;
                }
            }
        }
            //then we check the vertical groups
            for(int i = 0; i < playerShelf.length-1; i++)
                for (int j = 0; j < playerShelf[0].length; j++) {
                    if (playerShelf[i][j].getColor().equals(playerShelf[i + 1][j].getColor())) {
                        counter++;
                    }
                }
            return counter >= 6;
    }
}
