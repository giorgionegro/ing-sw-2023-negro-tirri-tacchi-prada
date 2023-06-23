package model;

import modelView.NewGameInfo;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameBuilderTest {
    @Test
    void nonStandardGameTest(){

        assertThrows(IllegalArgumentException.class, () ->
            GameBuilder.build(new NewGameInfo("gameId", "NONSTANDARD", 2, System.currentTimeMillis()))
        );


    }
}
