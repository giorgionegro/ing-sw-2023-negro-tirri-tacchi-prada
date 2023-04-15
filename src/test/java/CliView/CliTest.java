package CliView;

import model.Tile;
import org.junit.jupiter.api.Test;

public class CliTest {
    @Test
    void printMatrixTest() {
        String[] input = {"WRGYBL", "GGGGGG", "YYYYYY", "BBBBBB", "LLLLLL", "MMMMMM"};
        Tile[][] matrix = StringToTileArray(input);
        Cli cli = new Cli();
        cli.printMatrix(matrix);
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
