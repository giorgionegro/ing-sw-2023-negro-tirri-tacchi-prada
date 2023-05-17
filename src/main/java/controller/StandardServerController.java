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
import util.Observer;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class StandardServerController implements ServerController, GameManagerController, AppServer{

    private final Map<ClientInterface, User> users;
    private final Map<User,StandardGameController> activeUsers;
    private final Map<String, StandardGameController> gameControllers;

    public StandardServerController() throws RemoteException {
        super();
        this.gameControllers = new HashMap<>();
        this.users = new HashMap<>();
        this.activeUsers = new HashMap<>();
    }

    ///APP SERVER//////////////////////////////

    @Override
    public ServerInterface connect(ClientInterface client) throws RemoteException {

        User newUser = new User();
        newUser.addObserver(
                (Observer<User, User.Event>) (o, arg) -> {
                    try{
                        client.update(o.getInfo(),arg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                       //throw new RuntimeException(e);
                    }
                }
        );
        users.put(client, newUser);

        System.err.println("UTENTE CONNESSO");
        return new ServerEndpoint(this,this);
    }

    @Override
    public void disconnect(ClientInterface client){
        User user = users.remove(client);
        user.deleteObservers();
    }

    ///SERVER CONTROLLER//////////////////

    @Override
    public void joinGame(ClientInterface client, LoginInfo info) throws RemoteException{
        try{
            if(!this.gameControllers.containsKey(info.gameId()))
                throw new GameAccessDeniedException();

            if(users.get(client).getStatus() == User.Status.JOINED)
                throw new GameAccessDeniedException("User already joined");

            StandardGameController controller = this.gameControllers.get(info.gameId());
            User user = this.users.get(client);

            controller.joinPlayer(client,info.playerId());
            activeUsers.put(user,controller);

            user.setStatus(User.Status.JOINED,info.time());

            System.err.println("GIOCATORE JOIN");
        } catch (GameAccessDeniedException e) {
            users.get(client).setStatus(User.Status.NOT_JOINED,info.time(),e.getMessage());
        }
    }

    @Override
    public void leaveGame(ClientInterface client) throws RemoteException{
        User user = users.get(client);
        if(user!=null) {
            activeUsers.remove(user).leavePlayer(client);
            user.setStatus(User.Status.NOT_JOINED, System.currentTimeMillis());
        }
        else
            throw new RemoteException("Client not connected to any match");
    }

    @Override
    public void createGame(ClientInterface client, NewGameInfo gameInfo) throws RemoteException{
        try {
            createGame(gameInfo.gameId(),gameInfo.playerNumber());
            System.err.println("GIOCO CREATO");
        } catch (GameAlreadyExistsException e) {
            users.get(client).setStatus(User.Status.NOT_JOINED, gameInfo.time(), e.getMessage());
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
