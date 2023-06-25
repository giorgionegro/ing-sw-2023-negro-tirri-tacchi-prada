package model;

import model.abstractModel.GoalEvaluator;
import model.goalEvaluators.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalsEvaluatorsTest {


    /**
     * Methods under test:
     * <ul>
     *     <li>{@link Standard2ColumnsRowOfDifferentTiles#evaluate(Tile[][])}
     *     <li>{@link Standard2ColumnsRowOfDifferentTiles#getDescription()}
     *     <li>{@link Standard2ColumnsRowOfDifferentTiles#Standard2ColumnsRowOfDifferentTiles(boolean)}
     *     </ul>
     * <p>
     *         Reasons:
     *         <ul>
     *               <li>test with true constructor parameter(Columns)</li>
     *          <li>test with a correct configuration</li>
     *         <li>test with a standard shelf and a shelf with 2 missing columns for the goal to be true.</li>
     *         </ul>
     */
    @Test
    void testStandard2ColumnsOfDifferentTiles() {
        GoalEvaluator goal = new Standard2ColumnsRowOfDifferentTiles(true);
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "YYYYYY", "BBBBBB", "LLLLLL", "MMMMMM"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = this.StringToTileArray(new String[]{"RRRRRR", "RRRRRG", "RRRRRY", "RRRRRB", "RRRRRL", "RRRRRM"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertEquals("Two columns formed each from 6 different types of tiles", goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link Standard2ColumnsRowOfDifferentTiles#evaluate(Tile[][])}
     *     <li>{@link Standard2ColumnsRowOfDifferentTiles#getDescription()}
     *     <li>{@link Standard2ColumnsRowOfDifferentTiles#Standard2ColumnsRowOfDifferentTiles(boolean)}
     *     </ul>
     * <p>
     *         Reasons:
     *         <ul>
     *                             <li>test with false constructor parameter(Rows)</li>
     *          <li>test with a correct configuration</li>
     *         <li>test with a incorrect configuration.</li>
     *         </ul>
     */
    @Test
    void testStandard2RowsOfDifferentTiles() {
        Standard2ColumnsRowOfDifferentTiles goal = new Standard2ColumnsRowOfDifferentTiles(false);
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRRR", "RGYBLM", "YYYYYY", "MGYBLR", "LLLLLL", "MMMMMM"});
        assertTrue(goal.evaluate(shelf));
        //test false
        shelf = this.StringToTileArray(new String[]{"RRRRRR", "RLRRRG", "RRRRRY", "RRRRRB", "RRRRRL", "RRRRRM"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(goal.getDescription(), null);
    }


    /**
     * Methods under test:
     * <ul>
     *     <li>{@link Standard3or4ColumnsRowMax3Types#evaluate(Tile[][])}
     *     <li>{@link Standard3or4ColumnsRowMax3Types#getDescription()}
     *     <li>{@link Standard3or4ColumnsRowMax3Types#Standard3or4ColumnsRowMax3Types(boolean)}
     *     </ul>
     * <p>
     *         Reasons:
     *         <ul>
     *             <li>test with true constructor parameter(Columns)</li>
     *             <li>test with a correct configuration</li>
     *             <li>test with an incorrect configuration</li>
     *             <li>test get description</li>
     *             <li>test with false constructor parameter(Rows)</li>
     *             <li>test with a correct configuration</li>
     *             <li>test with an incorrect configuration</li>
     *             <li>test get description</li>
     *             </ul>
     */
    @Test
    void testStandard34ColumnsRowMax3Types() {
        GoalEvaluator goal = new Standard3or4ColumnsRowMax3Types(true);
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "YYYYYY", "GGGGGG", "YYYYYY", "RRRRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = this.StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "RGGYYY", "RGGBBB", "RGGLLL", "RGWMMM"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
        goal = new Standard3or4ColumnsRowMax3Types(false);
        shelf = this.StringToTileArray(new String[]{"RGYBLM", "MBBBBR", "YGGGLL", "BMMMMY", "GWWWWB", "RMMMMG"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = this.StringToTileArray(new String[]{"WWWWWM", "WWWWWM", "WWWWWM", "WWWWWM", "WWWWWM", "RMLYBG"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link Standard4Groups4Tiles#evaluate(Tile[][])}
     *     <li>{@link Standard4Groups4Tiles#getDescription()}
     *     <li>{@link Standard4Groups4Tiles)} default constructor
     *     </ul>
     * <p>
     *         Reasons:
     *         <ul>
     *             <li>test with a correct configuration</li>
     *             <li>test with an incorrect configuration</li>
     *             <li>test get description</li>
     *             </ul>
     */
    @Test
    void testStandard4Groups4Tiles() {
        Standard4Groups4Tiles goal = new Standard4Groups4Tiles();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRGG", "GGGGYG", "YYYYMG", "RGLMGG", "RGLMGY", "MMMGMM"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = this.StringToTileArray(new String[]{"RGYBLM", "MLYBGR", "YBGRML", "BGRMLY", "GRMLYB", "RMLYBG"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link Standard5TileDiagonal#evaluate(Tile[][])}
     *     <li>{@link Standard5TileDiagonal#getDescription()}
     *     <li>{@link Standard5TileDiagonal)} default constructor
     *     </ul>
     * <p>
     *         Reasons:
     *         <ul>
     *             <li>test with a correct configuration</li>
     *             <li>test with an incorrect configuration</li>
     *             <li>test get description</li>
     *             </ul>
     */
    @Test
    void testStandard5TileDiagonal() {
        //Test top left to bottom right
        Standard5TileDiagonal goal = new Standard5TileDiagonal();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRR", "GRGGG", "YYRYY", "BBBRB", "LLLLR", "MMMMM"});
        assertTrue(goal.evaluate(shelf));
        //Test top right to bottom left
        shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGRG", "YYRYY", "BRBRB", "RLLLR", "MMMMM"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGRG", "YYMYY", "BRBRB", "MLLLR", "MMMMM"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link Standard8TilesSameType#evaluate(Tile[][])}
     *     <li>{@link Standard8TilesSameType#getDescription()}
     *     <li>{@link Standard8TilesSameType)} default constructor
     *     </ul>
     */
    @Test
    void testStandard8TileSameType() {
        Standard8TilesSameType goal = new Standard8TilesSameType();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link StandardCorners#evaluate(Tile[][])}
     *     <li>{@link StandardCorners#getDescription()}
     *     <li>{@link StandardCorners)} default constructor
     * </ul>
     */
    @Test
    void testStandardCorners() {
        StandardCorners goal = new StandardCorners();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "RMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = this.StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }


    /**
     * Methods under test:
     * <ul>
     *     <li>{@link StandardSixGroup2Tiles#evaluate(Tile[][])}
     *     <li>{@link StandardSixGroup2Tiles#getDescription()}
     *     <li>{@link StandardSixGroup2Tiles)} default constructor
     * </ul>
     */
    @Test
    void testStandard6Group2Tiles() {
        StandardSixGroup2Tiles goal = new StandardSixGroup2Tiles();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGR", "MMMLM", "BYBYB", "MLMLM", "BYBYB"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGR", "MLMLM", "BYBYB", "MLMLM", "BYBYB"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = this.StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link StandardTwoSquares#evaluate(Tile[][])}
     *     <li>{@link StandardTwoSquares#getDescription()}
     *     <li>{@link StandardTwoSquares)} default constructor
     * </ul>
     */
    @Test
    void testStandardTwoSquares() {
        StandardTwoSquares goal = new StandardTwoSquares();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RRRRR", "RRGGG", "YYYYY", "BBBYY", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RRRRR", "RRGGG", "RRYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = this.StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());

    }


    /**
     * Methods under test:
     * <ul>
     *     <li>{@link StandardStairs#evaluate(Tile[][])}
     *     <li>{@link StandardStairs#getDescription()}
     *     <li>{@link StandardStairs)} default constructor
     * </ul>
     */
    @Test
    void testStandardStairs() {
        StandardStairs goal = new StandardStairs();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RWWWW", "GGWWW", "YYYWW", "BBBBW", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test Stairs in reverse
        shelf = this.StringToTileArray(new String[]{"WWWWR", "WWWGG", "WWYYY", "WBBBB", "LLLLL", "MMRRM"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RWWWR", "GGWWR", "YYYWR", "BBBBR", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = this.StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Methods under test:
     * <ul>
     *     <li>{@link StandardXOfDifferentTiles#evaluate(Tile[][])}
     *     <li>{@link StandardXOfDifferentTiles#getDescription()}
     *     <li>{@link StandardXOfDifferentTiles)} default constructor
     * </ul>
     */
    @Test
    void testStandardXOfDifferentTiles() {
        StandardXOfDifferentTiles goal = new StandardXOfDifferentTiles();
        Tile[][] shelf = this.StringToTileArray(new String[]{"RWRWR", "GRGGG", "RYRYY", "BBBBB", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = this.StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = this.StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    /**
     * Method under test {@link Standard4Groups4Tiles()}
     */
    @Test
    void testConstructor() {
        // Arrange, Act and Assert
        assertEquals("Standard4Groups4Tiles", (new Standard4Groups4Tiles()).getId());
    }

    /**
     * Utility method to convert a string array to a Tile[][] array
     * @param input String array of tiles
     * @return Tile[][] of the input
     */
    private Tile[][] StringToTileArray(String[] input) {
        //String will be in the form of {"RRRRRR", "GGGGGG", "YYYYYY", "BBBBBB", "LLLLLL", "MMMMMM"}
        Tile[][] output = new Tile[input.length][input[0].length()];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length(); j++) {
                switch (input[i].charAt(j)) {
                    case 'R' -> output[i][j] = Tile.CATS_1;
                    case 'G' -> output[i][j] = Tile.BOOKS_1;
                    case 'Y' -> output[i][j] = Tile.GAMES_1;
                    case 'B' -> output[i][j] = Tile.FRAMES_1;
                    case 'L' -> output[i][j] = Tile.TROPHIES_1;
                    case 'M' -> output[i][j] = Tile.PLANTS_1;
                    case 'W' -> output[i][j] = Tile.EMPTY;
                }
            }
        }
        return output;
    }

}