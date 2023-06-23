package modelView;

import java.io.Serializable;

/**
 * This record contains information about a Login request
 * @param playerId player id
 * @param gameId game id
 * @param sessionID the new sessionID
 */
public record LoginInfo(String playerId, String gameId, long sessionID) implements Serializable {}
