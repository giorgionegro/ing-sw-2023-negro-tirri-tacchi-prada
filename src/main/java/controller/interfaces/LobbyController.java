package controller.interfaces;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;
import model.User;

/**
 * This is the interface of a game lobby controller
 * <p>
 * It defines the methods to join or leave a {@link model.abstractModel.Game}
 */
public interface LobbyController {
    /**
     * This method allows a client to join the lobby and to be attached to the {@link model.abstractModel.Game}
     * @param newClient the client interface that needs to be attached to the game
     * @param user the user reference of the client into the server
     * @param playerId the new player ID
     * @throws GameAccessDeniedException if the client cannot join the specified game
     */
    void joinPlayer(ClientInterface newClient, User user, String playerId) throws GameAccessDeniedException;

    /**
     * This method allows a client to leave the lobby and to be detached from the {@link model.abstractModel.Game} it is attached to
     * @param client the client that needs to be detached
     * @throws GameAccessDeniedException if the client cannot leave the specified game
     */
    void leavePlayer(ClientInterface client) throws GameAccessDeniedException;
}
