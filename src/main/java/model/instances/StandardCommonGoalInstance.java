package model.instances;

import model.Token;
import model.abstractModel.GoalEvaluator;

import java.io.Serializable;
import java.util.Stack;

public record StandardCommonGoalInstance(
        GoalEvaluator evaluator,
        Stack<Token> tokenStack

) implements Serializable{}
