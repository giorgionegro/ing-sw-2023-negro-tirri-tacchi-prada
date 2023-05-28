package model;

import model.goalEvaluators.Standard2ColumnsRowOfDifferentTiles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardCommonGoalTest {
    @Test
    void constructorWorksCorrectly4Players() {
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        assertDoesNotThrow(() -> new StandardCommonGoal(4, evalTest));
    }

    @Test
    void constructorThrowsExceptionMoreThan4Players() {
        Standard2ColumnsRowOfDifferentTiles evalTest = new Standard2ColumnsRowOfDifferentTiles(true);
        assertThrows(UnsupportedOperationException.class, () -> new StandardCommonGoal(5, evalTest));

    }
}