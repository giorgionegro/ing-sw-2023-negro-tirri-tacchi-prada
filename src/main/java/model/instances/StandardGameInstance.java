package model.instances;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This record represents a {@link model.StandardGame} instance
 * @param players the representation of instance's association playerId -> {@link model.abstractModel.Player}
 * @param commonGoals the representation of instance's list of {@link model.abstractModel.CommonGoal}
 * @param livingRoom the representation of instance's {@link model.StandardLivingRoom}
 * @param gameId the representation of instance's gameId
 * @param playersTurnQueue the representation of instance's list of {@link model.abstractModel.Player} representing the turn queue
 * @param firstPlayer the representation of instance's {@link model.abstractModel.Player} representing the first player
 * @param maxPlayerNumber the representation of instance's max player number
 * @param lastTurn the representation of instance's is_last_turn value
 */
public record StandardGameInstance(
        Map<String,Serializable> players,
        List<Serializable> commonGoals,
        Serializable livingRoom,
        String gameId,
        List<String> playersTurnQueue,
        String firstPlayer,
        int maxPlayerNumber,
        boolean lastTurn
)implements Serializable{}
