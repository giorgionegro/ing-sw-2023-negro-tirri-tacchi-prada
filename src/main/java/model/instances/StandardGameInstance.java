package model.instances;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
