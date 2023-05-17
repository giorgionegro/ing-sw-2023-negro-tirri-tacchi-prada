package modelView;

import java.io.Serializable;

/**
 * @param playerId player id
 * @param gameId game id
 * @param time timeStamp of login request
 */
public record LoginInfo(String playerId, String gameId, long time) implements Serializable {}
