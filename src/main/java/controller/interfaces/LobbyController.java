package controller.interfaces;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;
import model.User;

public interface LobbyController {
    void joinPlayer(ClientInterface newClient, User user, String playerId) throws GameAccessDeniedException;
    void leavePlayer(ClientInterface client) throws GameAccessDeniedException;
}
