package controller;

import controller.interfaces.GameController;
import controller.interfaces.GameManagerController;
import controller.interfaces.ServerController;
import distibuted.ServerEndpoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.networkObservers.UserNetworkObserver;
import model.User;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import model.StandardGame;
import model.abstractModel.Game;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class StandardServerController implements ServerController, GameManagerController, AppServer{

    private final Map<ClientInterface, User> users;
    private final Map<String, StandardGameController> gameControllers;

    public StandardServerController() throws RemoteException {
        super();
        this.gameControllers = new HashMap<>();
        this.users = new HashMap<>();
    }

    ///APP SERVER//////////////////////////////

    @Override
    public ServerInterface connect(ClientInterface client) throws RemoteException {

        User newUser = new User();
        newUser.addObserver(new UserNetworkObserver(client));
        users.put(client, newUser);

        return new ServerEndpoint(this,this);
    }

    ///SERVER CONTROLLER//////////////////

    @Override
    public void joinGame(ClientInterface client, LoginInfo info) throws RemoteException{
        try{
            this.gameControllers.get(info.getGameId()).joinPlayer(client,info.getPlayerId());
        } catch (Exception e) {
            users.get(client).forceNotifyObservers();//TODO
        }
    }
    @Override
    public void createGame(ClientInterface client, NewGameInfo gameInfo) throws RemoteException{
        try {
            createGame(gameInfo.getGameId(),gameInfo.getPlayerNumber());
        } catch (GameAlreadyExistsException e) {
            users.get(client).forceNotifyObservers();//TODO aggiungere evento errore
        }
    }

    ///GAMES MANAGER CONTROLLER/////////////////

    @Override
    public GameController getGame(String gameId) throws GameNotExistsException {
        if(!gameControllers.containsKey(gameId))
            throw new GameNotExistsException();

        return gameControllers.get(gameId);
    }



    @Override
    public void createGame(String gameId, int playerNumber) throws GameAlreadyExistsException {
        if (gameControllers.containsKey(gameId))
            throw new GameAlreadyExistsException();

        Game newGame = new StandardGame(gameId, playerNumber);

        gameControllers.put(gameId, new StandardGameController(newGame));
    }
}
