package model;

import model.goalEvaluators.Standard2ColumnsRowOfDifferentTiles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the StandardCommonGoal class
 */
class StandardCommonGoalTest {
    /**
     * Method under test: {@link StandardCommonGoal#StandardCommonGoal(int, model.abstractModel.GoalEvaluator)}
     */
    @Test
    void constructorWorksCorrectly4Players() {
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        assertDoesNotThrow(() -> new StandardCommonGoal(4, evalTest));
    }

    /**
     * Method under test: {@link StandardCommonGoal#StandardCommonGoal(int, model.abstractModel.GoalEvaluator)}
     */
    @Test
    void constructorThrowsExceptionMoreThan4Players() {
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);

        assertThrows(UnsupportedOperationException.class, () -> new StandardCommonGoal(5, evalTest));

    }




}