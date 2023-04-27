package modelView;

import java.io.Serializable;

/**
 * @param playerId player id
 * @param gameId game id
 */
public record LoginInfo(String playerId, String gameId) implements Serializable {
}
