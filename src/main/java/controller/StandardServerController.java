package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.GameManagerController;
import controller.interfaces.ServerController;
import distibuted.ServerEndpoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.exceptions.GameAlreadyExistsException;
import model.exceptions.GameNotExistsException;
import modelView.LoginInfo;
import modelView.NewGameInfo;
import model.StandardGame;
import model.abstractModel.Game;
import modelView.UserInfo;
import util.Observer;


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
        newUser.addObserver(
                (Observer<User, User.Event>) (o, arg) -> {
                    try{
                        client.update(new UserInfo(o.getStatus(),o.getReportedError()),arg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        users.put(client, newUser);

        return new ServerEndpoint(this,this);
    }

    @Override
    public void disconnect(ClientInterface client) throws RemoteException {
        User user = users.remove(client);
        user.deleteObservers();
    }

    ///SERVER CONTROLLER//////////////////

    @Override
    public void joinGame(ClientInterface client, LoginInfo info) throws RemoteException{
        try{
            this.gameControllers.get(info.gameId()).joinPlayer(client,info.playerId());
            User user = users.get(client);
            user.setStatus(User.Status.JOINED);
        } catch (GameAccessDeniedException e) {
            users.get(client).reportError(e.getMessage());
        }
    }

    @Override
    public void createGame(ClientInterface client, NewGameInfo gameInfo) throws RemoteException{
        try {
            createGame(gameInfo.gameId(),gameInfo.playerNumber());
        } catch (GameAlreadyExistsException e) {
            users.get(client).reportError(e.getMessage());
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
