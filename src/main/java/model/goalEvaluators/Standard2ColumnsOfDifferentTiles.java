package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;

public class Standard2ColumnsOfDifferentTiles extends GoalEvaluator {
    @Override
    public String getDescription(){
        return "Two columns formed each from 6 different types of tiles";
    }

    // returns true if the player has at least 2 columns of 6 different tiles
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0; // counts the number of columns with 6 different tiles colors
        for (int i = 0; i < playerShelf[0].length; i++) {
            //check if every line is different
            String[] colours = new String[playerShelf.length];
            for (int j = 0; j < playerShelf.length; j++) {
                colours[j] = playerShelf[j][i].getColor();
            }
            //remove Empty tiles
            if (Arrays.stream(colours).filter(colour -> !colour.equals(Tile.EMPTY.getColor())).distinct().count()==colours.length) {//check if every line is different by comparing the number of distinct colours
                counter++;
            }
        }
        return counter >= 2;    }
}
