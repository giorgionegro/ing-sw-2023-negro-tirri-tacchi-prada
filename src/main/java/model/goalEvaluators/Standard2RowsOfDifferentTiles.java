package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Standard2RowsOfDifferentTiles extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Two lines formed each from 5 different types of tiles");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0; // counts the number of rows with 5 different tiles colors
        for (int i = 0; i < playerShelf.length; i++) {
            //check if every line is different
            String[] colors = new String[5];
            for (int j = 0; j < playerShelf[0].length; j++) {
                colors[j] = playerShelf[i][j].getColor();
            }
            if (Arrays.stream(colors).distinct().collect(Collectors.toList()).size()==colors.length) {//check if every column is different by comparing the number of distinct colors
                counter++;
            }
        }
        return counter >= 2;

    }
}
