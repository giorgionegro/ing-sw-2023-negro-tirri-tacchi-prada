package modelView;

import java.io.Serializable;

/**
 * This class represents a collection of information that characterize a game
 *
 * @param type TODO rivedere il game type
 * @param playerNumber number of players
 */
public record NewGameInfo(String gameId, String type, int playerNumber, long time) implements Serializable {

}
