package model;

import model.abstractModel.PersonalGoal;
import model.abstractModel.Player;
import model.instances.StandardPlayerInstance;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StandardPlayerTest {

    @Test
    void testPlayerCreation(){
        String playerId = getPlayerId();

        List<PersonalGoal> personalGoals = getPersonalGoals();

        Player p = new StandardPlayer(playerId,personalGoals);

        if(p.getPlayerChat()==null)
            fail("Player chat not instanced");

        if(p.getAchievedCommonGoals()==null)
            fail("Achieved common goals can't be null");

        if(p.getAchievedCommonGoals().size()!=0)
            fail("Achieved common goals must be empty on player contruction");

        if(p.getShelf()==null)
            fail("Player shelf not instanced");

        if(!p.getId().equals(playerId))
            fail("Player id instanced wrong");

        List<PersonalGoal> personalGoalListFromPlayer = p.getPersonalGoals();

        if(personalGoalListFromPlayer.size()!=personalGoals.size())
            fail("Player personal goal instanced wrong");

        for(PersonalGoal personalGoal : personalGoals){
            if(!personalGoalListFromPlayer.contains(personalGoal))
                fail("Player personal goal instanced wrong");
        }
    }

    @Test
    void testPlayerShelfModification(){
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

        if(shelf.length != shelfFromPlayer.length)
            fail("Saved shelf has changed dimensions");

        if(shelf[0].length != shelfFromPlayer[0].length)
            fail("Saved shelf has changed dimensions");

        for(int i=0; i<shelf.length; i++){
            for(int j=0; j<shelf[i].length; j++){
                if(shelf[i][j]!=shelfFromPlayer[i][j])
                    fail("Saved shelf has changed tiles");
            }
        }
    }

    @Test
    void testPlayerAchievingCommonGoal(){
        Player p = new StandardPlayer(getPlayerId(), getPersonalGoals());

        String desc1 = "Goal 1";
        String desc2 = "Goal 2";
        Token token1 = Token.TOKEN_8_POINTS;
        Token token2 = Token.TOKEN_6_POINTS;

        p.addAchievedCommonGoal(desc1,token1);
        p.addAchievedCommonGoal(desc2,token2);

        Map<String,Token> cGoalsFromPlayer = p.getAchievedCommonGoals();
        if(cGoalsFromPlayer.size()!=2)
            fail("Not saved all achieved common goal");

        if(!cGoalsFromPlayer.containsKey(desc1) || !cGoalsFromPlayer.containsKey(desc2))
            fail("Saved wrong goal description");

        if(!cGoalsFromPlayer.get(desc1).equals(token1) || !cGoalsFromPlayer.get(desc2).equals(token2))
            fail("Saved wrong token");
    }



    private String getPlayerId(){
        String playerId = "";
        Random r = new Random();

        for(int j = 0; j<5; j++)
            playerId += (char) r.nextInt(97,123);
        return playerId;
    }

    private List<PersonalGoal> getPersonalGoals(){
        List<PersonalGoal> r = new ArrayList<>();
        r.add(new StandardPersonalGoal(Tile.CATS_1,0,0));
        r.add(new StandardPersonalGoal(Tile.BOOKS_2,1,1));
        r.add(new StandardPersonalGoal(Tile.GAMES_1,2,2));
        r.add(new StandardPersonalGoal(Tile.TROPHIES_1,3,3));
        r.add(new StandardPersonalGoal(Tile.FRAMES_1,4,4));
        r.add(new StandardPersonalGoal(Tile.PLANTS_1,5,5));
        return r;
    }
    @Test
    void reportErrorTest(){
        StandardPlayer test = new StandardPlayer("p1", getPersonalGoals());
        test.reportError("Error test");
        assertEquals(test.getReportedError(), "Error test");
    }
    @Test
    void getInfoTest(){
        StandardPlayer test = new StandardPlayer("p1", getPersonalGoals());
        test.reportError("Error test");
        test.addAchievedCommonGoal("Test", Token.TOKEN_6_POINTS);
        assertEquals(test.getReportedError(), test.getInfo().errorMessage());
        assertEquals(test.getInfo().achievedCommonGoals(), test.getAchievedCommonGoals());
    }
    @Test
    void instanceTest(){
        StandardPlayer test = new StandardPlayer("p1", getPersonalGoals());
        test.reportError("Error test");
        test.addAchievedCommonGoal("Test", Token.TOKEN_6_POINTS);
        StandardPlayer instanceTest = new StandardPlayer((StandardPlayerInstance) test.getInstance());
        assertEquals(test.getInfo(), instanceTest.getInfo());
    }
}