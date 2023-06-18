package controller.interfaces;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;
import model.User;
import modelView.LoginInfo;

/**
 * This is the interface of a game lobby controller
 * <p>
 * It defines the methods to join or leave a game
 */
public interface LobbyController {
    /**
     * This method allow a client to join the lobby and to be attached to the game
     * @param newClient the client interface that needs to be attached to the game
     * @param user the user reference of the client into the server
     * @param info the login info
     * @throws GameAccessDeniedException if the client cannot join the specified game
     */
    void joinPlayer(ClientInterface newClient, User user, LoginInfo info) throws GameAccessDeniedException;

    /**
     * This method allow a client to leave the lobby and to be detached to the game it is attached to
     * @param client the client that needs to be detached
     * @throws GameAccessDeniedException if the client cannot leave the specified game
     */
    void leavePlayer(ClientInterface client) throws GameAccessDeniedException;
}
