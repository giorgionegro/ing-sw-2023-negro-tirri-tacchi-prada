package model;

import org.junit.jupiter.api.Test;
import model.goalEvaluators.*;
import model.Tile;
import static org.junit.jupiter.api.Assertions.*;

class GoalsEvaluatorsTest {

    @Test
    void testStandard2ColumnsOfDifferentTiles()
    {
        Standard2ColumnsOfDifferentTiles goal = new Standard2ColumnsOfDifferentTiles();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "YYYYYY", "BBBBBB", "LLLLLL", "MMMMMM"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = StringToTileArray(new String[]{"RRRRRR", "RRRRRG", "RRRRRY", "RRRRRB", "RRRRRL", "RRRRRM"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertEquals("Two columns formed each from 6 different types of tiles", goal.getDescription());
    }

    @Test
    void testStandard2RowsOfDifferentTiles()
    {
       Standard2RowsOfDifferentTiles goal = new Standard2RowsOfDifferentTiles();
         Tile[][] shelf = StringToTileArray(new String[]{"RRRRRR", "RGYBLM", "YYYYYY", "MGYBLR", "LLLLLL", "MMMMMM"});
        assertTrue(goal.evaluate(shelf));
        //test false
        shelf = StringToTileArray(new String[]{"RRRRRR", "RLRRRG", "RRRRRY", "RRRRRB", "RRRRRL", "RRRRRM"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(goal.getDescription(),null);
    }

    @Test
    void testStandard3ColumnsMax3Types()
    {
        Standard3ColumnsMax3Types goal = new Standard3ColumnsMax3Types();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "YYYYYY", "GGGGGG", "YYYYYY", "RRRRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "RGGYYY", "RGGBBB", "RGGLLL", "RGWMMM"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandard4Groups4Tiles()
    {
        Standard4Groups4Tiles goal = new Standard4Groups4Tiles();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRGG", "GGGGYG", "YYYYMG", "RGLMGG", "RGLMGY", "MMMGMM"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = StringToTileArray(new String[]{"RGYBLM","MLYBGR","YBGRML","BGRMLY","GRMLYB","RMLYBG"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandard4RowsMax3Types()
    {
        Standard4RowsMax3Types goal = new Standard4RowsMax3Types();
        Tile[][] shelf = StringToTileArray(new String[]{"RGYBLM","MBBBBR","YGGGLL","BMMMMY","GWWWWB","RMMMMG"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = StringToTileArray(new String[]{"WWWWWM","WWWWWM","WWWWWM","WWWWWM","WWWWWM","RMLYBG"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandard5TileDiagonal()
    {
        //Test top left to bottom right
        Standard5TileDiagonal goal = new Standard5TileDiagonal();
    Tile[][] shelf = StringToTileArray(new String[]{"RRRRR", "GRGGG", "YYRYY", "BBBRB", "LLLLR", "MMMMM"});
        assertTrue(goal.evaluate(shelf));
        //Test top right to bottom left
        shelf = StringToTileArray(new String[]{"RRRRR", "GGGRG", "YYRYY", "BRBRB", "RLLLR", "MMMMM"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = StringToTileArray(new String[]{"RRRRR", "GGGRG", "YYMYY", "BRBRB", "MLLLR", "MMMMM"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandard8TileSameType()
    {
        Standard8TilesSameType goal = new Standard8TilesSameType();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandardCorners()
    {
        StandardCorners goal = new StandardCorners();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "RMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }


    @Test
    void testStandard6Group2Tiles()
    {
    StandardSixGroup2Tiles goal = new StandardSixGroup2Tiles();
    Tile[][] shelf = StringToTileArray(new String[]{"RRRRR", "GGGGR", "MMMLM", "BYBYB", "MLMLM", "BYBYB"});
    assertTrue(goal.evaluate(shelf));
    //Test false
    shelf = StringToTileArray(new String[]{"RRRRR", "GGGGR", "MLMLM", "BYBYB", "MLMLM", "BYBYB"});
    assertFalse(goal.evaluate(shelf));
    //all blank
    shelf = StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
    assertFalse(goal.evaluate(shelf));
    assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandardTwoSquares()
    {
        StandardTwoSquares goal = new StandardTwoSquares();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRR", "RRGGG", "YYYYY", "BBBYY", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = StringToTileArray(new String[]{"RRRRR", "RRGGG", "RRYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());

    }


    @Test
    void testStandardStairs()
    {
        StandardStairs goal = new StandardStairs();
        Tile[][] shelf = StringToTileArray(new String[]{"RWWWW", "GGWWW", "YYYWW", "BBBBW", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test Stairs in reverse
        shelf = StringToTileArray(new String[]{"WWWWR", "WWWGG", "WWYYY", "WBBBB", "LLLLL", "MMRRM"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = StringToTileArray(new String[]{"RWWWR", "GGWWR", "YYYWR", "BBBBR", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandardXOfDifferentTiles()
    {
        StandardXOfDifferentTiles goal = new StandardXOfDifferentTiles();
        Tile[][] shelf = StringToTileArray(new String[]{"RWRWR", "GRGGG", "RYRYY", "BBBBB", "LLLLL", "MMRRR"});
        assertTrue(goal.evaluate(shelf));
        //Test false
        shelf = StringToTileArray(new String[]{"RRRRR", "GGGGG", "YYYYY", "BBBBB", "LLLLL", "MMRRM"});
        assertFalse(goal.evaluate(shelf));
        //all blank
        shelf = StringToTileArray(new String[]{"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"});
        assertFalse(goal.evaluate(shelf));
        assertNotEquals(null, goal.getDescription());
    }



    private Tile[][] StringToTileArray(String[] input)
    {
        //String will be in the form of {"RRRRRR", "GGGGGG", "YYYYYY", "BBBBBB", "LLLLLL", "MMMMMM"}
        Tile[][] output = new Tile[input.length][input[0].length()];
        for(int i = 0; i < input.length; i++)
        {
            for(int j = 0; j < input[0].length(); j++)
            {
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