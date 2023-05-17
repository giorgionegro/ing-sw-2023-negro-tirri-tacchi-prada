package model.instances;

import model.Token;
import model.abstractModel.GoalEvaluator;

import java.io.Serializable;
import java.util.Stack;

/**
 * This record represents a {@link model.StandardCommonGoal} instance
 * @param evaluator the representation of instance's {@link GoalEvaluator}
 * @param tokenStack the representation of instance's {@link Stack<Token>} token stack
 */
public record StandardCommonGoalInstance(
        GoalEvaluator evaluator,
        Stack<Token> tokenStack

) implements Serializable{}
