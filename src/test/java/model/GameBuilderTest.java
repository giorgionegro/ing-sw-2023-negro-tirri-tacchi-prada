package model;

import modelView.NewGameInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class tests the game builder
 */
public class GameBuilderTest {
    /**
     * Method under test: {@link GameBuilder#build(NewGameInfo)}
     */
    @Test
    void nonStandardGameTest(){

        assertThrows(IllegalArgumentException.class, () ->
            GameBuilder.build(new NewGameInfo("gameId", "NONSTANDARD", 2, System.currentTimeMillis()))
        );


    }
}
