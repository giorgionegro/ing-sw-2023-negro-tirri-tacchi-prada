package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Standard8TilesSameType extends GoalEvaluator {
    private final int numberOfTiles = 8;
    @Override
    public void getDescription(){
        System.out.println("Eight tiles of the same type. There are restrictions on the location of these tiles.");
    }
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
      AtomicBoolean flag = new AtomicBoolean(false);
        //get every color and check if there are 8 tiles of that color
        Arrays.stream(Tile.values()).toList().forEach(color -> {
            int counter = numberOfTiles;
            for (Tile[] tiles : playerShelf) {
                for (int j = 0; j < playerShelf[0].length; j++) {
                    if (tiles[j].getColor().equals(color.getColor())) {
                        counter--;
                    }
                }
            }
            if(counter == 0){
               flag.set(true);
            }
        });
        return flag.get();

    }
}
