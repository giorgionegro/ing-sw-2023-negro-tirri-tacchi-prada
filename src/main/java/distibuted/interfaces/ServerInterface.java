package distibuted.interfaces;

import model.GameInfo;

public interface ServerInterface {
    void connectToGame(ClientInterface client, String playerId, String gameId);
    void createNewGame(ClientInterface client, GameInfo newGameInfo);
}
