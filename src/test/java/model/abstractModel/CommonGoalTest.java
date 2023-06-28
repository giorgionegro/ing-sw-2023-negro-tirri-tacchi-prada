package model.abstractModel;

import model.StandardCommonGoal;
import model.Token;
import model.goalEvaluators.Standard4Groups4Tiles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the common goal
 */
class CommonGoalTest {
    /**
     * Method under test: {@link CommonGoal#getTopToken()}
     * Testing Token stack
     */
    @Test
    void testGetTopToken() {
        // Arrange, Act and Assert
        var commonGoal = (new StandardCommonGoal(4, new Standard4Groups4Tiles()));
        assertEquals(Token.TOKEN_8_POINTS, commonGoal.popToken());
        assertEquals(Token.TOKEN_6_POINTS, commonGoal.popToken());
        assertEquals(Token.TOKEN_4_POINTS, commonGoal.popToken());
        assertEquals(Token.TOKEN_2_POINTS, commonGoal.popToken());
        assertEquals(Token.TOKEN_EMPTY, commonGoal.getTopToken());

    }
}

