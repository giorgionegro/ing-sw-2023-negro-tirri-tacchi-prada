package model.goalEvaluators;

import model.Tile;
import model.abstractModel.GoalEvaluator;

import java.util.Objects;

public class StandardStairs extends GoalEvaluator {
    @Override
    public String getDescription() {
        return "Five columns of increasing height or descending: starting from the first column\nleft or right, each successive column it must be formed by an extra tile.\nTiles can be of any type.%n";
    }


    @Override
    public boolean evaluate(Tile[][] playerShelf) {
        // first we check the columns from left to right to have a descending high of tiles
        for (int i = 0; i < playerShelf[0].length - 4; i++) {
            // we check if the first column has at least 5 tiles, the second one less than the first one, and so on, colour doesn't matter
            // we check the first column from the bottom to the top, and we count the number of tiles
            int counter = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i].getColor(), Tile.EMPTY.getColor())) {
                    counter++;
                }
            }
            if (counter < 5) {
                continue;
            }
            // we check the second column from the bottom to the top, and we count the number of tiles
            int counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i + 1].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            counter = counter2;
            counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i + 2].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            counter = counter2;
            counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i + 3].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            counter = counter2;
            counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i + 4].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            return true;
        }
        // then we check the columns from right to left to have a descending high of tiles
        for (int i = playerShelf[0].length-1; i >=4; i--) {
            // we check if the first column has at least 5 tiles, the second one less than the first one, and so on, colour doesn't matter
            // we check the first column from the bottom to the top, and we count the number of tiles
            int counter = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i].getColor(), Tile.EMPTY.getColor())) {
                    counter++;
                }
            }
            if (counter < 5) {
                continue;
            }
            // we check the second column from the bottom to the top, and we count the number of tiles
            int counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i - 1].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            // we check if the second column has one tile less than the first one
            if (counter - 1 != counter2) {
                continue;
            }
            counter = counter2;
            counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i - 2].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            counter = counter2;
            counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i - 3].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            counter = counter2;
            counter2 = 0;
            for (int j = playerShelf.length - 1; j >= 0; j--) {
                if (!Objects.equals(playerShelf[j][i - 4].getColor(), Tile.EMPTY.getColor())) {
                    counter2++;
                }
            }
            if (counter - 1 != counter2) {
                continue;
            }
            return true;
        }
        return false;

    }

}
