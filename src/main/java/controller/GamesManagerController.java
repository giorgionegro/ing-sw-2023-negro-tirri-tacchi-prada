package controller;

import model.StandardGame;
import model.abstractModel.Game;
import model.abstractModel.GamesManager;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import model.exceptions.PlayerAlreadyExistsException;

public class GamesManagerController {

    private final GamesManager gamesManager;

    public GamesManagerController(GamesManager gamesManager) {
        this.gamesManager = gamesManager;
    }

    public void connect(String playerId, String gameId){
        try {
            gamesManager.getGame(gameId).addPlayer(playerId);
        } catch (PlayerAlreadyExistsException e) {
            throw new RuntimeException(e); //TODO mandare errore alla view
        } catch (GameNotExistsException e) {
            throw new RuntimeException(e); //TODO mandare errore alla view
        }
    }

    public void createGame(String matchId, GameType gameType, int playerNum){
        try{
            Game newGame = new StandardGame(playerNum);
            gamesManager.addGame(matchId, newGame);
        } catch (GameAlreadyExistsException e) {
            throw new RuntimeException(e); //TODO inviare errore alla view
        }
    }
}

enum GameType{

}
