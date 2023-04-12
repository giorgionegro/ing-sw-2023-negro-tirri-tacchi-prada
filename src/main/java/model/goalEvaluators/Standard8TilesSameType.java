package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is the goal evaluator for the Standard8TilesSameType goal
 * It implements the GoalEvaluator abstract class
 * It is used by the CommonGoal class
 */
public class Standard8TilesSameType extends GoalEvaluator {
    private final int numberOfTiles = 8;
    /**
     * This method returns the description of the goal
     * @return String description of the goal
     */
    @Override
    public String getDescription(){
        return "Eight tiles of the same type. There are restrictions on the location of these tiles.";
    }
    /**
     * This method evaluates if the goal is achieved
     * @param playerShelf the player's shelf
     * @return boolean true if 8 tiles of the same type are in the player's shelf
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
      AtomicBoolean flag = new AtomicBoolean(false);
        //get every color and check if there are 8 tiles of that color
        Arrays.stream(Tile.values()).map(Tile::getColor).filter(colour -> !colour.equals(Tile.EMPTY.getColor())).distinct().toList().forEach(color -> {
            int counter = numberOfTiles;
            for (Tile[] tiles : playerShelf) {
                for (int j = 0; j < playerShelf[0].length; j++) {
                    if (tiles[j].getColor().equals(color)
                    ) {
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
