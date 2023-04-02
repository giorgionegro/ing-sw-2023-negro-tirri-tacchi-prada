package model;

import org.junit.jupiter.api.Test;
import model.goalEvaluators.*;
import model.Tile;
import static org.junit.jupiter.api.Assertions.*;

class GoalsEvaluatorsTest {



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
    }
}