package modelView;

import model.Token;

import java.io.Serializable;
import java.util.Map;

/**
 * @param errorMessage error message
 * @param achievedCommonGoals - map of achieved common goals
 */
public record PlayerInfo(String errorMessage, Map<String, Token> achievedCommonGoals) implements Serializable {}
