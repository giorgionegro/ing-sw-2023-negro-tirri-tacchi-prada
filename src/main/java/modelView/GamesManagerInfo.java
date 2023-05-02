package modelView;

import java.io.Serializable;

/**
 * @param gameId game id
 * @param maxPlayers max players
 * @param connectedPlayers connected players
 */
public record GamesManagerInfo(String gameId, int maxPlayers, int connectedPlayers) implements Serializable{}
