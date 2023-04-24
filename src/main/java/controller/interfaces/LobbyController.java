package controller.interfaces;

import distibuted.interfaces.ClientInterface;
import model.exceptions.PlayerAlreadyExistsException;

public interface LobbyController {
    void joinPlayer(ClientInterface newClient, String playerId) throws PlayerAlreadyExistsException;
}
