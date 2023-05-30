package model;

import model.goalEvaluators.Standard2ColumnsRowOfDifferentTiles;
import model.instances.StandardCommonGoalInstance;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class StandardCommonGoalTest {
    @Test
    void constructorWorksCorrectly4Players() {
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        assertDoesNotThrow(() -> new StandardCommonGoal(4, evalTest));
    }

    @Test
    void constructorThrowsExceptionMoreThan4Players() {
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        try{
            StandardCommonGoal test = new StandardCommonGoal(5,evalTest);
            fail();
        }
        catch (UnsupportedOperationException e){
            e.printStackTrace();
        }
    }
    @Test
    void getIdTest(){
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        try{
            StandardCommonGoal test = new StandardCommonGoal(4,evalTest);
            assertEquals("Standard2ColumnsOfDifferentTiles", test.getEvaluator().getId());
        }
        catch (UnsupportedOperationException e){
            fail();
        }
        assertThrows(UnsupportedOperationException.class, () -> new StandardCommonGoal(5, evalTest));

    }
    @Test
    void instanceTest(){
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        try {
            StandardCommonGoal test = new StandardCommonGoal(4, evalTest);
            Serializable instanceTest = test.getInstance();
            StandardCommonGoal iTest = new StandardCommonGoal((StandardCommonGoalInstance) instanceTest);
            assertEquals(test.getInfo(), iTest.getInfo());
        }
        catch (Exception e){
            fail();
        }

    }
}