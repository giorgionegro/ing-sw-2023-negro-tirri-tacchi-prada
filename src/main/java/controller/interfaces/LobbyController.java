package controller.interfaces;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;

public interface LobbyController {
    void joinPlayer(ClientInterface newClient, String playerId) throws GameAccessDeniedException;
    void leavePlayer(ClientInterface client);
}
