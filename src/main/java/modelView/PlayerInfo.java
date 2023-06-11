package modelView;

import model.Token;

import java.io.Serializable;
import java.util.Map;

/**
 * This record contains information about the state of a {@link model.abstractModel.Player}
 * @param errorMessage message of en error encountered during gameplay
 * @param achievedCommonGoals map of achieved common goals with earned token
 */
public record PlayerInfo(String errorMessage, Map<String, Token> achievedCommonGoals) implements Serializable {}
