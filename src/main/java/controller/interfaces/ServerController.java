package controller.interfaces;

import distibuted.interfaces.ClientInterface;
import modelView.LoginInfo;
import modelView.NewGameInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the interface of a server controller
 * <p>
 * It specifies all the method needed to interact with the games-provider server
 */
public interface ServerController extends Remote {
    /**
     * This method allows a client to join a game
     * @param client The client asking to join
     * @param info The join-info
     * @throws RemoteException If an error occurred reaching the remote object
     */
    void joinGame(ClientInterface client, LoginInfo info) throws RemoteException;

    /**
     * This method allows a client to leave the game it has joined
     * @param client The client asking to leave
     * @throws RemoteException If an error occurred reaching the remote object
     */
    void leaveGame(ClientInterface client) throws RemoteException;

    /**
     * This method allows a client to create a game, given the {@link NewGameInfo}
     * @param client The client asking to create the game
     * @param info The new-game info
     * @throws RemoteException If an error occurred reaching the remote object
     */
    void createGame(ClientInterface client, NewGameInfo info) throws RemoteException;
}
