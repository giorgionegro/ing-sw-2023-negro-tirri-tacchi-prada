package model;

import model.abstractModel.*;
import model.goalEvaluators.*;
import modelView.NewGameInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * //TODO controllare
 * This class is responsible for creating a new game based on its type and initializing the various game components.
 */
public class GameBuilder {

    /**
     * //TODO controllare
     * Class constructor of GameBuilder
     */
    private GameBuilder(){}

    /**
     * //TODO controllare
     * This method creates a new Game instance based on the type of game the user wants to create.
     * @param newGameInfo record containing information of a {@link model.abstractModel.Game} creation request
     * @return a new Game instance
     * @throws IllegalArgumentException if newGameInfo type is not supported
     */
    public static Game build(NewGameInfo newGameInfo) throws IllegalArgumentException {
        //noinspection SwitchStatementWithTooFewBranches
        switch (newGameInfo.type()){
            case "STANDARD" -> {
                List<Player> avaiablePlayers = new ArrayList<>();
                List<CommonGoal> avaiableCommonGoals = setCommonGoals(newGameInfo.playerNumber());
                Stack<List<PersonalGoal>> avaiablePersonalGoals = setPersonalGoals();
                LivingRoom livingRoom = new StandardLivingRoom(newGameInfo.playerNumber());
                for (int i = 0; i < newGameInfo.playerNumber(); i++)
                    avaiablePlayers.add(new StandardPlayer(new StandardShelf(), avaiablePersonalGoals.pop(), new StandardPlayerChat()));

                return new StandardGame(avaiablePlayers, livingRoom, avaiableCommonGoals);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + newGameInfo.type());
        }
    }

    /**
     * This method initializes the common goals of a StandardGames
     * @param nPlayers number of players in the game
     * @return an ArrayList containing two common goals
     */
    private static ArrayList<CommonGoal> setCommonGoals(int nPlayers) {
        Stack<StandardCommonGoal> allGoals = new Stack<>();

        allGoals.add(new StandardCommonGoal(nPlayers, new Standard2ColumnsRowOfDifferentTiles(false)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard2ColumnsRowOfDifferentTiles(true)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard3or4ColumnsRowMax3Types(false)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard3or4ColumnsRowMax3Types(true)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard4Groups4Tiles()));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard5TileDiagonal()));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard8TilesSameType()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardCorners()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardSixGroup2Tiles()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardStairs()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardTwoSquares()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardXOfDifferentTiles()));

        Collections.shuffle(allGoals);

        ArrayList<CommonGoal> result = new ArrayList<>();

        result.add(allGoals.pop());
        result.add(allGoals.pop());

        return result;
    }

    /**
     * this method return stack containing lists of all the standard personal goals
     *
     * @return stack containing lists of all the standard personal goals
     */
    private static Stack<List<PersonalGoal>> setPersonalGoals() {
        Stack<List<PersonalGoal>> result = new Stack<>();
        result.add(createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.FRAMES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.TROPHIES_1}, new int[]{0, 0, 1, 2, 3, 5}, new int[]{0, 2, 4, 3, 1, 2}));
        result.add(createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.CATS_1, Tile.GAMES_1, Tile.BOOKS_1, Tile.TROPHIES_1, Tile.FRAMES_1}, new int[]{1, 2, 2, 3, 4, 5}, new int[]{1, 0, 2, 4, 3, 4}));
        result.add(createPersonalGoal(new Tile[]{Tile.FRAMES_1, Tile.GAMES_1, Tile.PLANTS_1, Tile.CATS_1, Tile.TROPHIES_1, Tile.BOOKS_1}, new int[]{1, 1, 2, 3, 3, 4}, new int[]{0, 3, 2, 1, 4, 0}));
        result.add(createPersonalGoal(new Tile[]{Tile.GAMES_1, Tile.TROPHIES_1, Tile.FRAMES_1, Tile.PLANTS_1, Tile.BOOKS_1, Tile.CATS_1}, new int[]{0, 2, 2, 3, 4, 4}, new int[]{4, 0, 2, 3, 1, 2}));
        result.add(createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.FRAMES_1, Tile.BOOKS_1, Tile.PLANTS_1, Tile.GAMES_1, Tile.CATS_1}, new int[]{1, 3, 3, 4, 5, 5}, new int[]{1, 1, 2, 4, 0, 3}));
        result.add(createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.FRAMES_1, Tile.PLANTS_1}, new int[]{0, 0, 2, 4, 4, 5}, new int[]{2, 4, 3, 1, 3, 0}));
        result.add(createPersonalGoal(new Tile[]{Tile.CATS_1, Tile.FRAMES_1, Tile.PLANTS_1, Tile.TROPHIES_1, Tile.GAMES_1, Tile.BOOKS_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{0, 3, 1, 0, 4, 2}));
        result.add(createPersonalGoal(new Tile[]{Tile.FRAMES_1, Tile.CATS_1, Tile.TROPHIES_1, Tile.PLANTS_1, Tile.BOOKS_1, Tile.GAMES_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{4, 1, 2, 0, 3, 3}));
        result.add(createPersonalGoal(new Tile[]{Tile.GAMES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.TROPHIES_1, Tile.PLANTS_1, Tile.FRAMES_1}, new int[]{0, 2, 3, 4, 4, 5}, new int[]{2, 2, 4, 1, 4, 0}));
        result.add(createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.GAMES_1, Tile.BOOKS_1, Tile.CATS_1, Tile.FRAMES_1, Tile.PLANTS_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{4, 1, 0, 3, 1, 3}));
        result.add(createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.FRAMES_1, Tile.CATS_1, Tile.TROPHIES_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{2, 1, 0, 2, 4, 3}));
        result.add(createPersonalGoal(new Tile[]{Tile.BOOKS_1, Tile.PLANTS_1, Tile.FRAMES_1, Tile.TROPHIES_1, Tile.GAMES_1, Tile.CATS_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{2, 1, 2, 3, 4, 0}));
        Collections.shuffle(result);
        return result;
    }

    /**
     * this method returns a list of single Tile positions representing a personal goal
     *
     * @param tiles array of Tiles of the personal goal
     * @param rows  array of row position of each Tile
     * @param cols  array of column position of each Tile
     * @return ArrayList representing a standard personal goal
     */
    private static ArrayList<PersonalGoal> createPersonalGoal(Tile[] tiles, int[] rows, int[] cols) {
        ArrayList<PersonalGoal> personalGoal = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            personalGoal.add(new StandardPersonalGoal(i, tiles[i], rows[i], cols[i]));
        }
        return personalGoal;
    }
}
