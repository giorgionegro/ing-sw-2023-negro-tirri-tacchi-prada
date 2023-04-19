package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardPersonalGoalTest {

    @Test
    void TestDescription() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 5);
        assertEquals("Put Tile White in row: 3 and column 5", test.getDescription());
    }

    @Test
    void testAchieving(){
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 5);
        if(test.isAchieved())
            fail("Goal achieved at instance");

        test.setAchieved();
        if(!test.isAchieved())
            fail("Goal achieved is not set as achieved");
    }

    @Test //Test Tile out of bound.
    void TestEvaluate() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 5);
        try {
            test.evaluate(new Tile[2][4]);
            fail();
        }catch(IndexOutOfBoundsException e){
            System.out.println("Index out of bound");
        }
    }
    @Test //Test shelf cell empty.
    void TestEvaluate2() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 5);
        try {
            assertFalse(test.evaluate(new Tile[9][9]));
            fail();
        }catch(Exception e){
            System.out.println("Shelf cell empty");
        }
    }
    @Test //Tile in the correct position.
    void TestEvaluate3() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 5);
        Tile[][] matrice = new  Tile[9][9];
        matrice[3][5] = Tile.BOOKS_2;
        assertTrue(test.evaluate(matrice));
    }
    @Test //Tile in the wrong position.
    void TestEvaluate4() {
        StandardPersonalGoal test = new StandardPersonalGoal(Tile.BOOKS_2, 3, 5);
        Tile[][] matrice = new  Tile[9][9];
        matrice[3][5] = Tile.CATS_1;
        assertFalse(test.evaluate(matrice));
    }
}