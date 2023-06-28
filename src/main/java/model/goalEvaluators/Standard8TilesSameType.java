package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is the goal evaluator for the Standard8TilesSameType goal
 */
public class Standard8TilesSameType extends GoalEvaluator {
    /**
     * This int contains the number of tiles required to achieve the goal
     */
    private final int numberOfTiles = 8;

    /**
     * This string contains the description of the commonGoal 8TilesSameType
     */
    private final String standard8TilesSameTypeDescription = "Eight tiles of the same type.";

    /**
     * This string contains the id of the commonGoal 8TilesSameType
     */
    private final String standard8TilesSameTypeId = "Standard8TilesSameType";

    /**
     * {@inheritDoc}
     *
     * @return {@link #standard8TilesSameTypeDescription}
     */
    @Override
    public String getDescription() {
        return this.standard8TilesSameTypeDescription;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #standard8TilesSameTypeId}
     */
    @Override
    public String getId() {
        return this.standard8TilesSameTypeId;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerShelf the player's shelf
     * @return boolean true if 8 tiles of the same type are in the player's shelf
     */
    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        AtomicBoolean flag = new AtomicBoolean(false);
        //get every color and check if there are 8 tiles of that color
        Arrays.stream(Tile.values()).map(Tile::getColor).filter(colour -> !colour.equals(Tile.EMPTY.getColor())).distinct().toList().forEach(color -> {
            int counter = this.numberOfTiles;
            for (Tile[] tiles : playerShelf) {
                for (int j = 0; j < playerShelf[0].length; j++) {
                    if (tiles[j].getColor().equals(color)
                    ) {
                        counter--;
                    }
                }
            }
            if (counter == 0) {
                flag.set(true);
            }
        });
        return flag.get();
    }
}
