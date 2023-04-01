package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Standard3ColumnsMax3Types extends GoalEvaluator {
    @Override
    public void getDescription(){
        System.out.println("Three columns each formed by 6 tiles of one, two or three different types." +
                "Different columns may have different combinations of tile types.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0; // counts the number of columns with at most 3 different tiles colors

for (int i = 0; i < playerShelf[0].length; i++) {
            //check if every line is different
            String[] colors = new String[6];
            for (int j = 0; j < playerShelf.length; j++) {
                colors[j] = playerShelf[j][i].getColor();
            }
            if (Arrays.stream(colors).distinct().collect(Collectors.toList()).size()<=3)
            {
                counter++;
            }
        }
        return counter >= 3;
    }
}
