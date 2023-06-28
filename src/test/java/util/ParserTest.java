package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ParserTest {
    /**
     * Method under test: {@link Parser#argumentsParser(String[], Map)}
     */
    @Test
    void testArgumentsParser() {
        assertTrue(Parser.argumentsParser(new String[]{"Args"}, new HashMap<>()));
        assertFalse(Parser.argumentsParser(new String[]{}, new HashMap<>()));
    }

    /**
     * Method under test: {@link Parser#argumentsParser(String[], Map)}
     */
    @Test
    void testArgumentsParser2() {
        HashMap<String, String> parameters = new HashMap<>();
        assertFalse(Parser.argumentsParser(new String[]{"-"}, parameters));
        assertEquals(1, parameters.size());
    }

    /**
     * Method under test: {@link Parser#argumentsParser(String[], Map)}
     */
    @Test
    void testArgumentsParser3() {
        HashMap<String, String> parameters = new HashMap<>();
        assertFalse(Parser.argumentsParser(new String[]{"-", "Args"}, parameters));
        assertEquals(1, parameters.size());
    }

    /**
     * Method under test: {@link Parser#argumentsParser(String[], Map)}
     */
    @Test
    void testArgumentsParser4() {
        HashMap<String, String> parameters = new HashMap<>();
        assertFalse(Parser.argumentsParser(new String[]{"-", "-"}, parameters));
        assertEquals(1, parameters.size());
    }
}

