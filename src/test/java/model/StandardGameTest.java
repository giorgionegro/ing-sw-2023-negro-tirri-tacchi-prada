package model;

import model.abstractModel.Game;
import model.abstractModel.PersonalGoal;
import model.abstractModel.Game;
import model.abstractModel.LivingRoom;
import model.abstractModel.Player;
import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import model.instances.StandardGameInstance;
import modelView.GameInfo;
import modelView.GameInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class StandardGameTest {
    @Test
    void newStandardGameTest(){
        try{
            StandardGame test = new StandardGame("testID", 5);
            fail();
        }
        catch(IllegalArgumentException e){
            System.out.println("Illegal argument caught nPlayer > 4");
        }
        try{
            StandardGame test = new StandardGame("testID", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            System.out.println("Illegal argument caught nPlayer < 2");
        }
        try{
            StandardGame test = new StandardGame("testID", 4);
            assertTrue(Objects.equals(test.getGameId(), "testID"));
        }
        catch (Exception e){
            fail();
        }
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
    void closeGameTest() {
       StandardGame test = new StandardGame("1", 2);
       test.close();
       assertEquals(test.getGameStatus(), Game.GameStatus.ENDED);
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
    void setLastTurnTest(){
        StandardGame test = new StandardGame("testID", 2);
        assertFalse(test.isLastTurn());
        test.setLastTurn();
        assertTrue(test.isLastTurn());
    }
    @Test
    void turnPlayerTest(){
         StandardGame test = new StandardGame("testID", 2);
        try{
            test.addPlayer("player1");
        }
        catch(Exception e){
            fail();
        }
        assertEquals(test.getTurnPlayerId(), "player1");

        try{
            test.addPlayer("player2");
        }
        catch(Exception e){
            fail();
        }
        String beforeUpdate = test.getTurnPlayerId();
        try{
            test.updatePlayersTurn();
        }
        catch (GameEndedException e){
            fail();
        }
        assertFalse(Objects.equals(test.getTurnPlayerId(), beforeUpdate));
        if(Objects.equals(beforeUpdate, "player1")){
            assertEquals(test.getTurnPlayerId(), "player2");
        }
        else{
            assertEquals(test.getTurnPlayerId(), "player1");
        }
        test.setLastTurn();
        try{
            test.updatePlayersTurn();
        }
        catch(Exception e){
            fail();
        }

        try{
            test.updatePlayersTurn();
            fail();
        }
        catch(GameEndedException e){
            System.out.println("Game ended caught");
        }
    }
    @Test
    void getStatusTest(){
        StandardGame test = new StandardGame("testID", 2);
        try{
            test.addPlayer("p1");
        }
        catch (Exception e){
            fail();
        }
        assertEquals(test.getGameStatus(), Game.GameStatus.MATCHMAKING);
        try{
            test.addPlayer("p2");
        }
        catch (Exception e){
            fail();
        }
        assertEquals(test.getGameStatus(), Game.GameStatus.STARTED);
    }
    @Test
    void getInfoTest(){
        StandardGame test = new StandardGame("testID", 2);
        try{
            test.addPlayer("p1");
        }
        catch (Exception e){
            fail();
        }
        try{
            test.addPlayer("p2");
        }
        catch (Exception e){
            fail();
        }
        String turn = test.getTurnPlayerId();
        test.setLastTurn();
        test.close();
        GameInfo testInfo2 = test.getInfo();
        //assertTrue(testInfo2.lastTurn() == true && testInfo2.playerOnTurn() == "p1" && testInfo2.status().equals(Game.GameStatus.ENDED) && testInfo2.points().get("p1") == 0);
        assertEquals(testInfo2.playerOnTurn(), turn);
        assertEquals(true, testInfo2.lastTurn());
        assertEquals(Game.GameStatus.ENDED, testInfo2.status());
        assertEquals(0, (int) testInfo2.points().get("p1"));
    }
    //TODO tanti casi da controllare

    @Test
    void getLivingRoomTest(){
        StandardGame test = new StandardGame("testID", 2);
        LivingRoom livingTest = test.getLivingRoom();
        //TODO pensare a un test
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




    @Test
    void goalTest(){
        StandardGame test = new StandardGame("testID", 2);
        try{
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
        }
        catch (Exception e){
            fail();
        }

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
    void depthSearchTest(){
        StandardGame test = new StandardGame("testID", 2);
        try {
            test.addPlayer("p1");
        } catch (PlayerAlreadyExistsException e) {
            fail();
        } catch (MatchmakingClosedException e) {
            fail();
        }
        Tile[][] modifiedShelf = new Tile[5][6];
        for(int i=0; i<5; i++){
            for(int j=0; j<6; j++){
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
        assertTrue(test.getInfo().points().get("p1") == 2);

        modifiedShelf[0][3] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertTrue(test.getInfo().points().get("p1") == 3);

        modifiedShelf[1][0] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
        throw new RuntimeException(e);
        }
        assertTrue(test.getInfo().points().get("p1") == 6);

        modifiedShelf[2][0] = Tile.TROPHIES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertTrue(test.getInfo().points().get("p1") == 8);

        modifiedShelf[3][0] = Tile.GAMES_1;
        try {
            test.getPlayer("p1").getShelf().setTiles(modifiedShelf);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        assertTrue(test.getInfo().points().get("p1") == 8);
    }
    @Test
    void rejoinTest(){

    }
}