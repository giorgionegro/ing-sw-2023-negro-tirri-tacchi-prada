package model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StandardShelfTest {
    /**
     * Methods under test:
     * <ul>
     *     <li>{@link StandardShelf#setTiles(Tile[][])}
     *     <li>{@link StandardShelf#getTiles()}
     *     <li>{@link StandardShelf#getInfo(String)}
     *     <li>{@link StandardShelf}
     * </ul>
     */
    @Test
    void shelfTest() {
        Tile[][] modifiedShelf = new Tile[6][5];
        for (Tile[] a : modifiedShelf)
            Arrays.fill(a, Tile.EMPTY);
        StandardShelf test = new StandardShelf();
        assertTrue(Arrays.deepEquals(modifiedShelf, test.getTiles()));
        assertTrue(Arrays.deepEquals(modifiedShelf, test.getInfo("p").shelf()) && test.getInfo("p").playerId().equals("p"));

        modifiedShelf[3][3] = Tile.TROPHIES_1;

        test.setTiles(modifiedShelf);
        assertTrue(Arrays.deepEquals(modifiedShelf, test.getTiles()));
        assertTrue(Arrays.deepEquals(modifiedShelf, test.getInfo("p").shelf()) && test.getInfo("p").playerId().equals("p"));
    }

    /**
     * test: {@link StandardShelf#StandardShelf()}
     */
    @Test
    void testConstructor() {
        StandardShelf actualStandardShelf = new StandardShelf();
        assertFalse(actualStandardShelf.hasChanged());
        assertEquals(6, actualStandardShelf.getTiles().length);
        assertEquals(5, actualStandardShelf.getTiles()[0].length);
    }

}