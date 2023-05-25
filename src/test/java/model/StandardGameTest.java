package model;

import model.abstractModel.PersonalGoal;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StandardGameTest {

    @Test
    void getGameIdTest() {
        StandardGame gameTest = new StandardGame("esempio", 3);
        String actual = gameTest.getGameId();
        assertEquals("esempio", actual);
    }
    @Test
    void addPlayerTest() {
    }

    @Test
    void getPlayerTestExists() throws  PlayerNotExistsException {
        StandardGame gameTest = new StandardGame("esempio", 3);
        List<PersonalGoal> obb = new ArrayList<>();
        StandardPlayer player1 = new StandardPlayer("Rebecca", obb);
        try {
            gameTest.addPlayer(player1.getId());
        } catch (PlayerAlreadyExistsException e) {

        } catch (MatchmakingClosedException e) {

        }
       String actual = gameTest.getPlayer(player1.getId()).getId();
        assertEquals("Rebecca", actual);
    }

    @Test
    void getPlayerTestNotExists() {
        StandardGame gameTest = new StandardGame("esempio", 3);
        try {
            gameTest.getPlayer("Rebecca");
        }
        catch(Exception e){
        }
    }
    @Test
    void getCommonGoalsTest() {
        //List<CommonGoal> obb = new ArrayList<>();
        //CommonGoal Standard8TilesSameType = new CommonGoal();
    }

    @Test
    void getLivingRoomTest() {
       // LivingRoom example = new StandardLivingRoom(3);
       // StandardGame gameTest = new StandardGame("esempio", 3);

    }

    @Test
    void setLastTurnTest() {
        StandardGame gameTest = new StandardGame("esempio", 3);
        assertFalse(gameTest.isLastTurn());
        gameTest.setLastTurn();
        assertTrue(gameTest.isLastTurn());
    }

    @Test
    void isLastTurnTest() {
        StandardGame gameTest = new StandardGame("esempio", 3);
        gameTest.setLastTurn();
        assertTrue(gameTest.isLastTurn());
    }

    @Test
    void getTurnPlayerId() {
    }

    @Test
    void getGameStatus() {
    }

    @Test
    void updatePlayersTurn() {
    }
}