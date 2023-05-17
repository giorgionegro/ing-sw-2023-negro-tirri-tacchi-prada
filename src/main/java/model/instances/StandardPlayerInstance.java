package model.instances;

import model.Token;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This record represent a {@link model.StandardPlayer} instance
 * @param idPlayer the representation of instance's player id
 * @param shelf the representation of instance's {@link model.abstractModel.Shelf}
 * @param personalGoal the representation of instance's list of {@link model.abstractModel.PersonalGoal}
 * @param achievedCommonGoals the representation of instance's association commonGoalId -> {@link Token} of achieved common goals
 * @param chat the representation of instance's {@link model.abstractModel.PlayerChat}
 */
public record StandardPlayerInstance(
        String idPlayer,
        Serializable shelf,
        List<Serializable> personalGoal,
        Map<String, Token> achievedCommonGoals,
        Serializable chat
) implements Serializable {}
