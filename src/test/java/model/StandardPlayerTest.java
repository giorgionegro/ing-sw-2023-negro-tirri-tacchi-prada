package model;

import controller.StandardServerController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.abstractModel.PersonalGoal;
import model.abstractModel.Player;
import model.abstractModel.Shelf;
import model.instances.StandardPersonalGoalInstance;
import model.instances.StandardPlayerChatInstance;
import model.instances.StandardPlayerInstance;
import model.instances.StandardShelfInstance;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardPlayerTest {

    /**
     * Method under test: {@link StandardPlayer#StandardPlayer(StandardPlayerInstance)}
     */
    @Test
    void testConstructor() throws RemoteException {

        ArrayList<Serializable> personalGoal = new ArrayList<>();
        HashMap<String, Token> achievedCommonGoals = new HashMap<>();
        var shelf = new StandardShelf();
        var test = new StandardPlayer(new StandardPlayerInstance("Id Player", shelf.getInstance(), personalGoal, achievedCommonGoals, new StandardPlayerChat().getInstance()));

        var test2 = new StandardPlayer("Id Player", new ArrayList<>());
        assertEquals(test.getId(), test2.getId());
        assertArrayEquals(test.getShelf().getTiles(), test2.getShelf().getTiles());


    }


    @Test
    void testPlayerCreation() {
        String playerId = getPlayerId();

        List<PersonalGoal> personalGoals = getPersonalGoals();

        Player p = new StandardPlayer(playerId, personalGoals);

        if (p.getPlayerChat() == null)
            fail("Player chat not instanced");

        if (p.getAchievedCommonGoals() == null)
            fail("Achieved common goals can't be null");

        if (p.getAchievedCommonGoals().size() != 0)
            fail("Achieved common goals must be empty on player contruction");

        if (p.getShelf() == null)
            fail("Player shelf not instanced");

        if (!p.getId().equals(playerId))
            fail("Player id instanced wrong");

        List<PersonalGoal> personalGoalListFromPlayer = p.getPersonalGoals();

        if (personalGoalListFromPlayer.size() != personalGoals.size())
            fail("Player personal goal instanced wrong");

        for (PersonalGoal personalGoal : personalGoals) {
            if (!personalGoalListFromPlayer.contains(personalGoal))
                fail("Player personal goal instanced wrong");
        }
    }

    @Test
    void testPlayerShelfModification() {
        Player p = new StandardPlayer(getPlayerId(), getPersonalGoals());

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

    @Test
    void testPlayerAchievingCommonGoal() {
        Player p = new StandardPlayer(getPlayerId(), getPersonalGoals());

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

        if (!cGoalsFromPlayer.get(desc1).equals(token1) || !cGoalsFromPlayer.get(desc2).equals(token2))
            fail("Saved wrong token");
    }


    private @NotNull String getPlayerId() {
        StringBuilder playerId = new StringBuilder();
        Random r = new Random();

        for (int j = 0; j < 5; j++)
            playerId.append((char) r.nextInt(97, 123));
        return playerId.toString();
    }

    private @NotNull List<PersonalGoal> getPersonalGoals() {
        List<PersonalGoal> r = new ArrayList<>();
        r.add(new StandardPersonalGoal(Tile.CATS_1, 0, 0));
        r.add(new StandardPersonalGoal(Tile.BOOKS_2, 1, 1));
        r.add(new StandardPersonalGoal(Tile.GAMES_1, 2, 2));
        r.add(new StandardPersonalGoal(Tile.TROPHIES_1, 3, 3));
        r.add(new StandardPersonalGoal(Tile.FRAMES_1, 4, 4));
        r.add(new StandardPersonalGoal(Tile.PLANTS_1, 5, 5));
        return r;
    }

    @Test
    void reportErrorTest() {
        StandardPlayer test = new StandardPlayer("p1", getPersonalGoals());
        test.reportError("Error test");
        assertEquals(test.getReportedError(), "Error test");
    }

    @Test
    void getInfoTest() {
        StandardPlayer test = new StandardPlayer("p1", getPersonalGoals());
        test.reportError("Error test");
        test.addAchievedCommonGoal("Test", Token.TOKEN_6_POINTS);
        assertEquals(test.getReportedError(), test.getInfo().errorMessage());
        assertEquals(test.getInfo().achievedCommonGoals(), test.getAchievedCommonGoals());
    }




    /**
     * Method under test: {@link StandardPlayer#getInstance()}
     */
    @Test
    void testGetInstance2() {
        ArrayList<PersonalGoal> personalGoal = new ArrayList<>();
        assertTrue(
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).achievedCommonGoals()
                        .isEmpty());
        Serializable shelfResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal))
                .getInstance()).shelf();
        assertTrue(shelfResult instanceof StandardShelfInstance);
        assertEquals(personalGoal,
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).personalGoal());
        assertEquals("Id Player",
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).idPlayer());
        Serializable chatResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance())
                .chat();
        assertTrue(chatResult instanceof StandardPlayerChatInstance);
        assertEquals(6, ((StandardShelfInstance) shelfResult).tiles().length);
        assertEquals(personalGoal, ((StandardPlayerChatInstance) chatResult).messages());
    }

    /**
     * Method under test: {@link StandardPlayer#getInstance()}
     */
    @Test
    void testGetInstance3() {
        ArrayList<PersonalGoal> personalGoal = new ArrayList<>();
        personalGoal.add(new StandardPersonalGoal(Tile.CATS_1, 1, 1));
        assertTrue(
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).achievedCommonGoals()
                        .isEmpty());
        Serializable shelfResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal))
                .getInstance()).shelf();
        assertTrue(shelfResult instanceof StandardShelfInstance);
        List<Serializable> personalGoalResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal))
                .getInstance()).personalGoal();
        assertEquals(1, personalGoalResult.size());
        Serializable chatResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance())
                .chat();
        assertTrue(chatResult instanceof StandardPlayerChatInstance);
        assertEquals("Id Player",
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).idPlayer());
        assertEquals(6, ((StandardShelfInstance) shelfResult).tiles().length);
        assertTrue(((StandardPlayerChatInstance) chatResult).messages().isEmpty());
        assertFalse(((StandardPersonalGoalInstance) personalGoalResult.get(0)).achieved());
        assertEquals(Tile.CATS_1, ((StandardPersonalGoalInstance) personalGoalResult.get(0)).tile());
        assertEquals(1, ((StandardPersonalGoalInstance) personalGoalResult.get(0)).row());
        assertEquals(1, ((StandardPersonalGoalInstance) personalGoalResult.get(0)).column());
    }

    /**
     * Method under test: {@link StandardPlayer#getInstance()}
     */
    @Test
    void testGetInstance4() {
        ArrayList<PersonalGoal> personalGoal = new ArrayList<>();
        personalGoal.add(new StandardPersonalGoal(Tile.CATS_1, 1, 1));
        personalGoal.add(new StandardPersonalGoal(Tile.CATS_1, 1, 1));
        assertTrue(
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).achievedCommonGoals()
                        .isEmpty());
        Serializable shelfResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal))
                .getInstance()).shelf();
        assertTrue(shelfResult instanceof StandardShelfInstance);
        List<Serializable> personalGoalResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal))
                .getInstance()).personalGoal();
        assertEquals(2, personalGoalResult.size());
        Serializable chatResult = ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance())
                .chat();
        assertTrue(chatResult instanceof StandardPlayerChatInstance);
        assertEquals("Id Player",
                ((StandardPlayerInstance) (new StandardPlayer("Id Player", personalGoal)).getInstance()).idPlayer());
        assertTrue(((StandardPlayerChatInstance) chatResult).messages().isEmpty());
        assertEquals(6, ((StandardShelfInstance) shelfResult).tiles().length);
        assertEquals(Tile.CATS_1, ((StandardPersonalGoalInstance) personalGoalResult.get(0)).tile());
        assertEquals(Tile.CATS_1, ((StandardPersonalGoalInstance) personalGoalResult.get(1)).tile());
        assertEquals(1, ((StandardPersonalGoalInstance) personalGoalResult.get(1)).row());
        assertEquals(1, ((StandardPersonalGoalInstance) personalGoalResult.get(1)).column());
        assertFalse(((StandardPersonalGoalInstance) personalGoalResult.get(1)).achieved());
        assertEquals(1, ((StandardPersonalGoalInstance) personalGoalResult.get(0)).column());
        assertFalse(((StandardPersonalGoalInstance) personalGoalResult.get(0)).achieved());
        assertEquals(1, ((StandardPersonalGoalInstance) personalGoalResult.get(0)).row());
    }


}