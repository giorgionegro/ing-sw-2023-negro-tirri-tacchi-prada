package distibuted.interfaces;

import model.GameInfo;
import model.PlayerMove;

public interface ServerInterface {
    void connectToGame(ClientInterface client, String playerId, String gameId);
    void createNewGame(ClientInterface client, GameInfo newGameInfo);
    void makeMove(ClientInterface client, PlayerMove move);
}
