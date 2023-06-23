package model.abstractModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import model.StandardCommonGoal;
import model.Token;
import model.goalEvaluators.Standard4Groups4Tiles;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CommonGoalTest {
    /**
     * Method under test: {@link CommonGoal#getTopToken()}
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

