package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;

public class Standard3ColumnsMax3Types extends GoalEvaluator {
    @Override
    public String getDescription(){
        return "Three columns each formed by 6 tiles of one, two or three different types.Different columns may have different combinations of tile types.%n";
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        int counter = 0; // counts the number of columns with at most 3 different tiles colors

for (int i = 0; i < playerShelf[0].length; i++) {
            //check if every line is different
            String[] colors = new String[playerShelf.length];
            for (int j = 0; j < playerShelf.length; j++) {
                colors[j] = playerShelf[j][i].getColor();
            }
            if (Arrays.stream(colors).filter(colour -> !colour.equals("White")).distinct().count() <=3)
            {
                counter++;
            }
        }
        return counter >= 3;
    }
}
