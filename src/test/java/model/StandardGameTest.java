package model;

import model.abstractModel.Game;
import model.abstractModel.LivingRoom;
import model.abstractModel.Player;
import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import modelView.GameInfo;
import modelView.NewGameInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class StandardGameTest {

    /**
     * Method under test: {@link StandardGame#StandardGame(List, LivingRoom, List)}
     */
    @Test
    void testConstructor2() throws IllegalArgumentException {
        ArrayList<Player> availablePlayers = new ArrayList<>();
        StandardLivingRoom livingRoom = new StandardLivingRoom(2);
        StandardGame actualStandardGame = new StandardGame(availablePlayers, livingRoom, new ArrayList<>());
        assertFalse(actualStandardGame.hasChanged());
        assertSame(livingRoom, actualStandardGame.getLivingRoom());
        assertEquals(Game.GameStatus.MATCHMAKING, actualStandardGame.getGameStatus());
    }


    /**
     * Method under test: {@link StandardGame#addPlayer(String)}
     */
    @Test
    void testAddPlayer() throws IllegalArgumentException, PlayerNotExistsException {

        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        assertDoesNotThrow(() -> game.addPlayer("42"));
        assertThrows(PlayerAlreadyExistsException.class, () -> game.addPlayer("42"));
        assertDoesNotThrow(() -> game.addPlayer("43"));
        assertThrows(MatchmakingClosedException.class, () -> game.addPlayer("44"));


        //test for reconnections
        game.removePlayer("42");
        assertDoesNotThrow(() -> game.addPlayer("42"));
        assertThrows(PlayerAlreadyExistsException.class, () -> game.addPlayer("42"));
    }


    /**
     * Method under test: {@link StandardGame#removePlayer(String)}
     */
    @Test
    void testRemovePlayer() throws IllegalArgumentException, MatchmakingClosedException, PlayerAlreadyExistsException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));

        assertThrows(PlayerNotExistsException.class,
                () -> game.removePlayer("42"));
        game.addPlayer("42");
        game.addPlayer("43");
        assertDoesNotThrow(() -> game.removePlayer("43"));
        assertDoesNotThrow(() -> game.removePlayer("42"));
    }
    /**
     * Method under test: {@link StandardGame#removePlayer(String)}
     */
    @Test
    void testRemovePlayer2() throws IllegalArgumentException, MatchmakingClosedException, PlayerAlreadyExistsException {
        Game game2 = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        game2.addPlayer("42");
        game2.addPlayer("43");
        assertDoesNotThrow(() -> game2.removePlayer(game2.getTurnPlayerId()));
        assertDoesNotThrow(() -> game2.removePlayer(game2.getTurnPlayerId()));
    }

    /**
     * Method under test: {@link StandardGame#getPlayer(String)}
     */
    @Test
    void testGetPlayer() throws IllegalArgumentException {

        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        assertThrows(PlayerNotExistsException.class,
                () -> game.getPlayer("42"));
        assertDoesNotThrow(() -> game.addPlayer("42"));
        final Player[] player = new Player[1];
        assertDoesNotThrow(() -> {
                    player[0]
                            = game.getPlayer("42");
                }
        );
        assertNotEquals(null, player[0]);


    }


    /**
     * Method under test: {@link StandardGame#getCommonGoals()}
     */
    @Test
    void testGetCommonGoals2() throws IllegalArgumentException {
        ArrayList<Player> availablePlayers = new ArrayList<>();
        StandardLivingRoom livingRoom = new StandardLivingRoom(2);
        assertTrue((new StandardGame(availablePlayers, livingRoom, new ArrayList<>())).getCommonGoals().isEmpty());
    }

    /**
     * Method under test: {@link StandardGame#setLastTurn()}
     */
    @Test
    void testSetLastTurn() throws IllegalArgumentException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        game.setLastTurn();
        assertTrue(game.isLastTurn());
    }

    /**
     * Method under test: {@link StandardGame#getTurnPlayerId()}
     */
    @Test
    void testGetTurnPlayerId() throws IllegalArgumentException, MatchmakingClosedException, PlayerAlreadyExistsException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        game.addPlayer("42");
        game.addPlayer("43");
        assertTrue(Objects.equals(game.getTurnPlayerId(), "42") || Objects.equals(game.getTurnPlayerId(), "43"));

    }


    /**
     * Method under test: {@link StandardGame#close()}
     */
    @Test
    void testClose2() throws IllegalArgumentException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));

        game.close();
        assertEquals(Game.GameStatus.ENDED, game.getGameStatus());
        assertThrows(MatchmakingClosedException.class, () -> game.addPlayer("42"));
    }


    /**
     * Method under test: {@link StandardGame#updatePlayersTurn()}
     */
    @Test
    void testUpdatePlayersTurn() throws IllegalArgumentException, MatchmakingClosedException, PlayerAlreadyExistsException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        game.addPlayer("42");
        game.addPlayer("43");
        game.setLastTurn();
        assertThrows(GameEndedException.class, () -> {
            game.updatePlayersTurn();
            game.updatePlayersTurn();
        });
    }


    /**
     * Method under test: {@link StandardGame#updatePlayersTurn()}
     */
    @Test
    void testUpdatePlayersTurn2() throws IllegalArgumentException, MatchmakingClosedException, PlayerAlreadyExistsException, PlayerNotExistsException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 3, System.currentTimeMillis()));
        game.addPlayer("42");
        game.addPlayer("43");
        game.addPlayer("44");
        game.removePlayer("42");
        assertDoesNotThrow(() -> {
            game.updatePlayersTurn();
            game.updatePlayersTurn();
            game.updatePlayersTurn();
        });
    }


    /**
     * Method under test: {@link StandardGame#getInfo()}
     */
    @Test
    void testGetInfo() throws IllegalArgumentException, MatchmakingClosedException, PlayerAlreadyExistsException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 3, System.currentTimeMillis()));
        game.addPlayer("42");
        //getInfo()
        GameInfo gameInfo = game.getInfo();
        assertEquals(game.getGameStatus(), gameInfo.status());
        game.close();
        gameInfo = game.getInfo();
        assertEquals(Game.GameStatus.ENDED, gameInfo.status());

    }

    @Test
    void depthSearchTest() {
        Game test = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 3, System.currentTimeMillis()));
        try {
            test.addPlayer("p1");
        } catch (PlayerAlreadyExistsException | MatchmakingClosedException e) {
            fail();
        }
        Tile[][] modifiedShelf = new Tile[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                modifiedShelf[i][j] = Tile.EMPTY;
            }
        }
        modifiedShelf[0][0] = Tile.TROPHIES_1;
        modifiedShelf[0][1] = Tile.TROPHIES_1;
        modifiedShelf[0][2] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, (int) test.getInfo().points().get("p1"));

        modifiedShelf[0][3] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, (int) test.getInfo().points().get("p1"));

        modifiedShelf[1][0] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertEquals(6, (int) test.getInfo().points().get("p1"));

        modifiedShelf[2][0] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertEquals(8, (int) test.getInfo().points().get("p1"));

        modifiedShelf[3][0] = Tile.GAMES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertEquals(8, (int) test.getInfo().points().get("p1"));
    }


    @Test
    void goalTest() {
        Game test = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 3, System.currentTimeMillis()));
        try {
            test.addPlayer("p1");
            test.addPlayer("p2");

            test.close();
            assertEquals(Game.GameStatus.ENDED, test.getGameStatus());

            assertEquals(0, test.getInfo().points().get("p1"));

            test.getPlayer("p1").getPersonalGoals().get(0).setAchieved();
            assertEquals(1, test.getInfo().points().get("p1"));

            test.getPlayer("p1").getPersonalGoals().get(1).setAchieved();
            assertEquals(2, test.getInfo().points().get("p1"));

            test.getPlayer("p1").getPersonalGoals().get(2).setAchieved();
            assertEquals(4, test.getInfo().points().get("p1"));

            test.getPlayer("p1").getPersonalGoals().get(3).setAchieved();
            assertEquals(6, test.getInfo().points().get("p1"));

            test.getPlayer("p1").getPersonalGoals().get(4).setAchieved();
            assertEquals(9, test.getInfo().points().get("p1"));

            test.getPlayer("p1").getPersonalGoals().get(5).setAchieved();
            assertEquals(12, test.getInfo().points().get("p1"));

            test.getPlayer("p1").addAchievedCommonGoal(test.getCommonGoals().get(0).getEvaluator().getDescription(), test.getCommonGoals().get(0).getTopToken());

            assertEquals(20, test.getInfo().points().get("p1"));
        } catch (Exception e) {
            fail();
        }

    }


}