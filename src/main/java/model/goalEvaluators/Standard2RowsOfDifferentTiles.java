package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;

public class Standard2RowsOfDifferentTiles extends GoalEvaluator {
    @Override
    public String getDescription(){

        return "Two lines formed each from 5 different types of tiles";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0; // counts the number of rows with 5 different tiles colors
        for (Tile[] tiles : playerShelf) {
            //check if every line is different
            String[] colors = new String[playerShelf[0].length];
            for (int j = 0; j < playerShelf[0].length; j++) {
                colors[j] = tiles[j].getColor();
            }
            if (Arrays.stream(colors).filter(colour -> !colour.equals("White")).distinct().count() == colors.length) {//check if every column is different by comparing the number of distinct colors
                counter++;
            }
        }
        return counter >= 2;

    }
}
