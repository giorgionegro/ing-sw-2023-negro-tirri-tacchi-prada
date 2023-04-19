package controller;

import distibuted.interfaces.ClientInterface;
import modelView.GameInfo;
import model.StandardGame;
import model.abstractModel.Game;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class GamesManagerController {

    private final Map<String,GameController> gameControllers;

    public GamesManagerController() {
        this.gameControllers = new HashMap<>();
    }

    public GameController getGameController(ClientInterface client, String gameId) {
        try {
            if(!gameControllers.containsKey(gameId))
                throw new GameNotExistsException();

            return gameControllers.get(gameId);

        } catch (GameNotExistsException e) {
            try {
                client.signalError("GAME NOT EXISTS");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //TODO mandare errore alla view
        }

        return null;
    }

    public void createGame(ClientInterface client, GameInfo gameInfo) {
        try {
            if(gameControllers.containsKey(gameInfo.getGameId()))
                throw new GameAlreadyExistsException();

            Game newGame = new StandardGame(gameInfo.getGameId(), gameInfo.getPlayerNumber());

            gameControllers.put(gameInfo.getGameId(), new GameController(newGame));

            System.out.println("GAME CREATED");
        } catch (GameAlreadyExistsException e) {
            try {
                client.signalError("GAME ALREADY EXISTS");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //TODO inviare errore alla view
        } catch (IllegalArgumentException e){
            try {
                client.signalError("ILLEGAL ARGUMENTS");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //TODO inviare errore alla view
        }


    }
}
