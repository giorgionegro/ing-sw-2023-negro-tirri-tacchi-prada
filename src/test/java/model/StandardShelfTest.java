package model;

import model.instances.StandardShelfInstance;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardShelfTest {
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
     * test: {@link StandardShelf#StandardShelf(StandardShelfInstance)}
     */
    @Test
    void testConstructor() {
        StandardShelf actualStandardShelf = new StandardShelf(
                new StandardShelfInstance(new Tile[][]{new Tile[]{Tile.CATS_1}}));
        assertFalse(actualStandardShelf.hasChanged());
        assertEquals(1, actualStandardShelf.getTiles().length);
    }
    @Test
    void shelfInstanceTest(){
        StandardShelf test = new StandardShelf();
        StandardShelf instanceTest = new StandardShelf((StandardShelfInstance) test.getInstance());
        assertTrue(Arrays.deepEquals(test.getTiles(), instanceTest.getTiles()));

        StandardShelf test2 = new StandardShelf();
        Tile[][] modifiedShelf = new Tile[6][5];
        modifiedShelf[0][0] = Tile.FRAMES_1;
        test2.setTiles(modifiedShelf);
        StandardShelf instanceTest2 = new StandardShelf((StandardShelfInstance) test2.getInstance());
        assertTrue(Arrays.deepEquals(test2.getTiles(), instanceTest2.getTiles()));
    }
}