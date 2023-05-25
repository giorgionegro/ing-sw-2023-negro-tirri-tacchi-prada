package model;

import model.instances.StandardPersonalGoalInstance;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardPersonalGoalTest {

    /**
     * Method under test: {@link StandardPersonalGoal#StandardPersonalGoal(StandardPersonalGoalInstance)}
     */
    @Test
    void testConstructor() {
        StandardPersonalGoal actualStandardPersonalGoal = new StandardPersonalGoal(
                new StandardPersonalGoalInstance(Tile.CATS_1, 1, 1, true));
        assertEquals(6, actualStandardPersonalGoal.getDescription().length);
        assertTrue(actualStandardPersonalGoal.isAchieved());
        assertFalse(actualStandardPersonalGoal.hasChanged());
    }

    /**
     * Method under test: {@link StandardPersonalGoal#StandardPersonalGoal(StandardPersonalGoalInstance)}
     */
    @Test
    void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> new StandardPersonalGoal(null));
    }

    @Test
    void TestDescription() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        Tile[][] TestMatrix = new Tile[6][5];
        Arrays.stream(TestMatrix).forEach(row -> Arrays.fill(row, Tile.EMPTY));
        TestMatrix[3][4] = Tile.BOOKS_2;
        assertArrayEquals(TestMatrix, test.getDescription());
    }

    @Test
    void testAchieving() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        if (test.isAchieved())
            fail("Goal achieved at instance");

        test.setAchieved();
        if (!test.isAchieved())
            fail("Goal achieved is not set as achieved");
    }

    @Test
        //Test Tile out of bound.
    void TestEvaluate() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        try {
            test.evaluate(new Tile[2][3]);
            fail();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }
    }

    @Test
        //Test shelf cell empty.
    void TestEvaluate2() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        Tile[][] matrice = new Tile[9][9];
        Arrays.stream(matrice).forEach(row -> Arrays.fill(row, Tile.EMPTY));
        try {
            assertFalse(test.evaluate(matrice));
        } catch (Exception e) {
            System.out.println("Shelf cell empty");
        }
    }

    @Test
        //Tile in the correct position.
    void TestEvaluate3() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        Tile[][] matrice = new Tile[9][9];
        Arrays.stream(matrice).forEach(row -> Arrays.fill(row, Tile.EMPTY));
        matrice[3][4] = Tile.BOOKS_3;
        assertTrue(test.evaluate(matrice));
    }

    @Test
        //Tile in the wrong position.
    void TestEvaluate4() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        Tile[][] matrice = new Tile[9][9];
        Arrays.stream(matrice).forEach(row -> Arrays.fill(row, Tile.EMPTY));
        matrice[3][4] = Tile.CATS_1;
        assertFalse(test.evaluate(matrice));
    }

    @Test
    void getInfoTest() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 4);
        assertTrue(Arrays.deepEquals(test.getDescription(), test.getInfo().description()));
    }
}