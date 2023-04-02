package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;

public class Standard4RowsMax3Types extends GoalEvaluator {
    @Override
    public String getDescription(){
        return "Four lines formed each from 5 tiles of one, two or three types different. Different lines may have\ndifferent combinations of tile types%n";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0; // counts the number of rows with at most 3 different tiles colors

        for (Tile[] tiles : playerShelf) {
            //check if every line is different
            String[] colors = new String[playerShelf[0].length];
            for (int j = 0; j < playerShelf[0].length; j++) {
                colors[j] = tiles[j].getColor();
            }
            if (Arrays.stream(colors).filter(colour -> !colour.equals("White")).distinct().count() <= 3) {
                counter++;
            }
        }
        return counter >= 4;
    }
}
