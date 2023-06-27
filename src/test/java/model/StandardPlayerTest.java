package model;

import model.abstractModel.PersonalGoal;
import model.abstractModel.Player;
import model.abstractModel.PlayerChat;
import model.abstractModel.Shelf;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class StandardPlayerTest {


    /**
     * Method under test: {@link StandardPlayer#StandardPlayer(model.abstractModel.Shelf, List, model.abstractModel.PlayerChat)}
     */
    @Test
    void testPlayerCreation() {

        List<PersonalGoal> personalGoals = this.getPersonalGoals();

        Player p = new StandardPlayer(new StandardShelf(), personalGoals, new StandardPlayerChat());

        p.getPlayerChat();

        p.getAchievedCommonGoals();

        if (p.getAchievedCommonGoals().size() != 0)
            fail("Achieved common goals must be empty on player contruction");

        p.getShelf();

        List<PersonalGoal> personalGoalListFromPlayer = p.getPersonalGoals();

        if (personalGoalListFromPlayer.size() != personalGoals.size())
            fail("Player personal goal instanced wrong");

        for (PersonalGoal personalGoal : personalGoals) {
            if (!personalGoalListFromPlayer.contains(personalGoal))
                fail("Player personal goal instanced wrong");
        }
    }

    /**
     * Method under test: {@link StandardPlayer#getShelf()}
     */
    @Test
    void testPlayerShelfModification() {
        Player p = new StandardPlayer(new StandardShelf(), new ArrayList<>(), new StandardPlayerChat());

        Tile[][] shelf = new Tile[6][5];
        for (Tile[] tiles : shelf) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        shelf[0][0] = Tile.FRAMES_1;
        shelf[3][4] = Tile.CATS_1;
        shelf[2][3] = Tile.BOOKS_2;

        p.getShelf().setTiles(shelf);

        Tile[][] shelfFromPlayer = p.getShelf().getTiles();

        if (shelf.length != shelfFromPlayer.length)
            fail("Saved shelf has changed dimensions");

        if (shelf[0].length != shelfFromPlayer[0].length)
            fail("Saved shelf has changed dimensions");

        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[i].length; j++) {
                if (shelf[i][j] != shelfFromPlayer[i][j])
                    fail("Saved shelf has changed tiles");
            }
        }
    }

    /**
     * Method under test: {@link StandardPlayer#addAchievedCommonGoal(String, Token)}
     */
    @Test
    void testPlayerAchievingCommonGoal() {
        Player p = new StandardPlayer(new StandardShelf(), new ArrayList<>(), new StandardPlayerChat());

        String desc1 = "Goal 1";
        String desc2 = "Goal 2";
        Token token1 = Token.TOKEN_8_POINTS;
        Token token2 = Token.TOKEN_6_POINTS;

        p.addAchievedCommonGoal(desc1, token1);
        p.addAchievedCommonGoal(desc2, token2);

        Map<String, Token> cGoalsFromPlayer = p.getAchievedCommonGoals();
        if (cGoalsFromPlayer.size() != 2)
            fail("Not saved all achieved common goal");

        if (!cGoalsFromPlayer.containsKey(desc1) || !cGoalsFromPlayer.containsKey(desc2))
            fail("Saved wrong goal description");

        if (cGoalsFromPlayer.get(desc1) != token1 || cGoalsFromPlayer.get(desc2) != token2)
            fail("Saved wrong token");
    }


    /**
     * Utility method to get a list of personal goals
     * @return a list of personal goals
     */
    private List<PersonalGoal> getPersonalGoals() {
        List<PersonalGoal> r = new ArrayList<>();
        r.add(new StandardPersonalGoal(0, Tile.CATS_1, 0, 0));
        r.add(new StandardPersonalGoal(1, Tile.BOOKS_2, 1, 1));
        r.add(new StandardPersonalGoal(2, Tile.GAMES_1, 2, 2));
        r.add(new StandardPersonalGoal(3, Tile.TROPHIES_1, 3, 3));
        r.add(new StandardPersonalGoal(4, Tile.FRAMES_1, 4, 4));
        r.add(new StandardPersonalGoal(5, Tile.PLANTS_1, 5, 5));
        return r;
    }

    /**
     *  Method under test: {@link StandardPlayer#reportError(String)}
     */
    @Test
    void reportErrorTest() {
        Player test = new StandardPlayer(new StandardShelf(), new ArrayList<>(), new StandardPlayerChat());
        test.reportError("Error test");
       assert  test.getInfo().errorMessage().equals("Error test");
    }

    /**
     * Method under test: {@link StandardPlayer#addAchievedCommonGoal(String, Token)}
     */
    @Test
    void getInfoTest() {
        Player test = new StandardPlayer(new StandardShelf(), new ArrayList<>(), new StandardPlayerChat());
        test.reportError("Error test");
        test.addAchievedCommonGoal("Test", Token.TOKEN_6_POINTS);
        assertEquals(test.getInfo().achievedCommonGoals(), test.getAchievedCommonGoals());
    }


}