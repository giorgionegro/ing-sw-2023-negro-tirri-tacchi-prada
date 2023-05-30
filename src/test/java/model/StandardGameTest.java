package model;

import model.abstractModel.Game;
import model.abstractModel.PersonalGoal;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import model.instances.StandardGameInstance;
import modelView.GameInfo;
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
    void addPlayerTest() throws MatchmakingClosedException, PlayerAlreadyExistsException {
        StandardGame game = new StandardGame("test", 2);
        game.addPlayer("1");
        game.addPlayer("2");
        game = new StandardGame((StandardGameInstance) game.getInstance());
        StandardGame finalGame = game;
        assertThrows(MatchmakingClosedException.class, () -> finalGame.addPlayer("3"));
        assertDoesNotThrow(() -> finalGame.addPlayer("1"));
    }

    @Test
    void getPlayerTestExists() throws PlayerNotExistsException {
        StandardGame gameTest = new StandardGame("esempio", 3);
        List<PersonalGoal> obb = new ArrayList<>();
        StandardPlayer player1 = new StandardPlayer("Rebecca", obb);
        try {
            gameTest.addPlayer(player1.getId());
        } catch (PlayerAlreadyExistsException | MatchmakingClosedException ignored) {

        }
        String actual = gameTest.getPlayer(player1.getId()).getId();
        assertEquals("Rebecca", actual);
    }

    @Test
    void getPlayerTestNotExists() {
        StandardGame gameTest = new StandardGame("esempio", 3);
        try {
            gameTest.getPlayer("Rebecca");
        } catch (Exception ignored) {
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
    void getTurnPlayerIdTest() {
        ArrayList<StandardPlayer> players = new ArrayList<>();
        List<PersonalGoal> obb = new ArrayList<>();
        players.add(new StandardPlayer("Rebecca", obb));
        players.add(new StandardPlayer("Enrico", obb));
        players.add(new StandardPlayer("JJ", obb));

        // Creo una coda di turni di gioco e aggiungo giocatori
        List<StandardPlayer> playerTurnQueue = new ArrayList<>(players);

        // Creo un gioco e setto la coda dei turni di gioco
        StandardGame gameTest = new StandardGame("123", 3);
        try {
            gameTest.addPlayer("Rebecca");
        } catch (PlayerAlreadyExistsException | MatchmakingClosedException e) {
            fail(e.getMessage());
        }
        //gameTest.setTurnQueue(playerTurnQueue); //TODO COME FACCIO A SETTARE IL TURNO DI UN GIOCATORE PER TESTARE?

        //Verifico che il giocatore corretto sia restituito
        assertEquals("Rebecca", gameTest.getTurnPlayerId());
    }


    /**
     * Method under test: {@link StandardGame#getInfo()}
     */
    @Test
    void testGetInfo3() throws MatchmakingClosedException, PlayerAlreadyExistsException {
        StandardGame standardGame = new StandardGame("42", 2);
        standardGame.addPlayer("Player Id");
        GameInfo actualInfo = standardGame.getInfo();
        assertFalse(actualInfo.lastTurn());
        assertEquals(Game.GameStatus.MATCHMAKING, actualInfo.status());
        assertEquals(1, actualInfo.points().size());
        assertEquals("Player Id", actualInfo.playerOnTurn());
    }

    /**
     * Method under test: {@link StandardGame#getInfo()}
     */
    @Test
    void testGetInfo4() throws MatchmakingClosedException, PlayerAlreadyExistsException {
        StandardGame standardGame = new StandardGame("42", 2);
        standardGame.addPlayer("java.util.List");
        standardGame.addPlayer("Player Id");
        GameInfo actualInfo = standardGame.getInfo();
        assertFalse(actualInfo.lastTurn());
        assertEquals(Game.GameStatus.STARTED, actualInfo.status());
        assertEquals(2, actualInfo.points().size());
    }


    @Test
    void getGameStatus() {
        StandardGame game = new StandardGame("123", 2);
        //TODO StandardGame.GameStatus expectedStatus = StandardGame.GameStatus.IN_PROGRESS;
        //game.setStatus(expectedStatus);

        // Verifichiamo che lo stato restituito dal metodo corrisponde allo stato atteso
        // assertEquals(expectedStatus, game.getGameStatus());
    }

    @Test
    void updatePlayersTurn() {
    }
}