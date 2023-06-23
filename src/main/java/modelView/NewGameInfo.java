package modelView;

import java.io.Serializable;

/**
 * This record contains information of a {@link model.abstractModel.Game} creation request
 * @param gameId the game id
 * @param type the type of the game
 * @param playerNumber number of players of the game
 * @param sessionID the new sessionID
 */
public record NewGameInfo(String gameId, String type, int playerNumber, long sessionID) implements Serializable {}
