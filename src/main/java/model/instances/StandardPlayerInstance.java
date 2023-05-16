package model.instances;

import model.Token;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record StandardPlayerInstance(
        String idPlayer,
        Serializable shelf,
        List<Serializable> personalGoal,
        Map<String, Token> achievedCommonGoals,
        Serializable chat
) implements Serializable {}
