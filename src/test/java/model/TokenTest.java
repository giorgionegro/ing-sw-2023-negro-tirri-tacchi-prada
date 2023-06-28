package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This class tests the Token class
 */
class TokenTest {
    /**
     * test:
     *
     * <ul>
     *   <li>{@link Token#valueOf(String)}
     *   <li>{@link Token#getPoints()}
     * </ul>
     */
    @Test
    void testValueOf() {
        assertEquals(8, Token.valueOf("TOKEN_8_POINTS").getPoints());
    }
}

