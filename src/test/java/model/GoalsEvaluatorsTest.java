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
        shelf = StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "RYYYYY", "RBBBBB", "RLLLLL", "RMMMMM"});
        assertFalse(goal.evaluate(shelf));
        //test get description
        assertNotEquals(null, goal.getDescription());
    }

    @Test
    void testStandard4Groups4Tiles()
    {
        Standard4Groups4Tiles goal = new Standard4Groups4Tiles();
        Tile[][] shelf = StringToTileArray(new String[]{"RRRRGG", "GGGGYG", "YYYYMG", "RGLMGY", "RGLMGY", "MMMGMM"});
        assertTrue(goal.evaluate(shelf));
        //Test 2 missing columns
        shelf = StringToTileArray(new String[]{"RRRRRR", "GGGGGG", "YYYYYY", "BBBBBB", "LLLLLL", "RRRRRR"});
        assertFalse(goal.evaluate(shelf));
        //test get description
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
                switch(input[i].charAt(j))
                {
                    case 'R':
                        output[i][j] = Tile.CATS_1;
                        break;
                    case 'G':
                        output[i][j] = Tile.BOOKS_1;
                        break;
                    case 'Y':
                        output[i][j] = Tile.GAMES_1;
                        break;
                    case 'B':
                        output[i][j] = Tile.FRAMES_1;
                        break;
                    case 'L':
                        output[i][j] = Tile.TROPHIES_1;
                        break;
                    case 'M':
                        output[i][j] = Tile.PLANTS_1;
                        break;
                }
            }
        }
        return output;
    }

}