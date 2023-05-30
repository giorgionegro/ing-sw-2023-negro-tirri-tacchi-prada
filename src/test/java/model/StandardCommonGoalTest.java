package model;

import model.goalEvaluators.Standard2ColumnsRowOfDifferentTiles;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class StandardCommonGoalTest {
    @Test
    void constructorWorksCorrectly4Players(){
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        try{
            StandardCommonGoal test = new StandardCommonGoal(4,evalTest);
        }
        catch (UnsupportedOperationException e){
            fail();
        }
    }
    @Test
    void constructorThrowsExceptionMoreThan4Players(){
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
    }
}