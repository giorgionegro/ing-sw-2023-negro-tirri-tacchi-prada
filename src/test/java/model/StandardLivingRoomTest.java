package model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StandardLivingRoomTest {
    @Test
    //after being created, the StandardLivingRoom instance must contain an empty board
    void StandardLivingRoomContainsEmptyBoard() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] actualBoard = test.getBoard();
        Tile[][] expectedBoard = new Tile[9][9];
        assertTrue(Arrays.deepEquals(expectedBoard, actualBoard));
    }

    @Test
    //after refillBoard(), check that all the board cells that should be empty actually are
    //when there are 2 Players
    void twoPlayerBoardHasCorrectEmptyTiles(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=6; i<9; i++){
            for(int j=0; j<3; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=0; i<3; i++){
            for(int j=6; j<9; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=6; i<9; i++){
            for(int j=6; j<9; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        if( actualBoard[0][3] != null ||
                actualBoard[0][4] != null ||
                actualBoard[0][5] != null ||
                actualBoard[1][5] != null ||
                actualBoard[3][0] != null ||
                actualBoard[3][1] != null ||
                actualBoard[3][8] != null ||
                actualBoard[4][0] != null ||
                actualBoard[4][8] != null ||
                actualBoard[5][0] != null ||
                actualBoard[5][7] != null ||
                actualBoard[5][8] != null ||
                actualBoard[7][3] != null ||
                actualBoard[8][4] != null ||
                actualBoard[8][5] != null){
            fail();
        }
    }

    @Test
    //after refillBoard(), check that all the board cells that should contain a TIle actually do
    //when there are 2 Players
    void twoPlayerBoardFilledCorrectly(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for(int i=3; i<6; i++){
            for(int j=3; j<6; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=1; i<3; i++){
            for(int j=3; j<5; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=3; i<5; i++){
            for(int j=6; j<8; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=4; i<6; i++){
            for(int j=1; j<3; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=6; i<8; i++){
            for(int j=4; j<6; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        if(actualBoard[2][5] == null || actualBoard[5][6] == null || actualBoard[6][4] == null || actualBoard[4][1] == null){
            fail();
        }
    }

    @Test
    //after refillBoard(), check that all the board cells that should be empty actually are
    //when there are 3 Players
    void threePlayerBoardHasCorrectEmptyTiles(){
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for(int i=0; i<3; i++){
            for(int j=0; j<2; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=6; i<9; i++){
            for(int j=0; j<2; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=0; i<3; i++){
            for(int j=7; j<9; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=6; i<9; i++){
            for(int j=7; j<9; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        if( actualBoard[0][2] != null ||
                actualBoard[0][4] != null ||
                actualBoard[0][5] != null ||
                actualBoard[1][2] != null ||
                actualBoard[1][5] != null ||
                actualBoard[1][6] != null ||
                actualBoard[3][0] != null ||
                actualBoard[3][1] != null ||
                actualBoard[4][0] != null ||
                actualBoard[4][8] != null ||
                actualBoard[5][7] != null ||
                actualBoard[5][8] != null ||
                actualBoard[7][2] != null ||
                actualBoard[7][3] != null ||
                actualBoard[7][6] != null ||
                actualBoard[8][2] != null ||
                actualBoard[8][3] != null ||
                actualBoard[8][4] != null ||
                actualBoard[8][6] != null){
            fail();
        }
    }

    @Test
    //after refillBoard(), check that all the board cells that should contain a TIle actually do
    //when there are 3 Players
    void threePlayerBoardFilledCorrectly(){
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();

        for(int i=3; i<6; i++){
            for(int j=3; j<6; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=1; i<3; i++){
            for(int j=3; j<5; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=3; i<5; i++){
            for(int j=6; j<8; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=4; i<6; i++){
            for(int j=1; j<3; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=6; i<8; i++){
            for(int j=4; j<6; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        if(actualBoard[2][5] == null ||
                actualBoard[5][6] == null ||
                actualBoard[6][4] == null ||
                actualBoard[4][1] == null ||
                actualBoard[1][3] == null ||
                actualBoard[2][6] == null ||
                actualBoard[3][8] == null ||
                actualBoard[6][6] == null ||
                actualBoard[8][5] == null ||
                actualBoard[6][2] == null ||
                actualBoard[5][0] == null ||
                actualBoard[2][2] == null){
            fail();
        }
    }

    @Test
    //after refillBoard(), check that all the board cells that should be empty actually are
    //when there are 4 Players
    void fourPlayerBoardHasCorrectEmptyTiles(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for(int i=0; i<3; i++){
            for(int j=0; j<2; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=6; i<9; i++){
            for(int j=0; j<2; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=0; i<3; i++){
            for(int j=7; j<9; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        for(int i=6; i<9; i++){
            for(int j=7; j<9; j++){
                if(actualBoard[i][j] != null){
                    fail();
                }
            }
        }

        if( actualBoard[0][2] != null ||
                actualBoard[0][5] != null ||
                actualBoard[1][2] != null ||
                actualBoard[1][6] != null ||
                actualBoard[3][0] != null ||
                actualBoard[5][8] != null ||
                actualBoard[7][2] != null ||
                actualBoard[7][6] != null ||
                actualBoard[8][2] != null ||
                actualBoard[8][3] != null ||
                actualBoard[8][6] != null){
            fail();
        }
    }

    @Test
    //after refillBoard(), check that all the board cells that should contain a TIle actually do
    //when there are 4 Players
    void fourPlayerBoardFilledCorrectly(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();

        for(int i=3; i<6; i++){
            for(int j=3; j<6; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=1; i<3; i++){
            for(int j=3; j<5; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=3; i<5; i++){
            for(int j=6; j<8; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=4; i<6; i++){
            for(int j=1; j<3; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        for(int i=6; i<8; i++){
            for(int j=4; j<6; j++){
                if(actualBoard[i][j] == null){
                    fail();
                }
            }
        }

        if(actualBoard[2][5] == null ||
                actualBoard[5][6] == null ||
                actualBoard[6][4] == null ||
                actualBoard[4][1] == null ||
                actualBoard[1][3] == null ||
                actualBoard[2][6] == null ||
                actualBoard[3][8] == null ||
                actualBoard[6][6] == null ||
                actualBoard[8][5] == null ||
                actualBoard[6][2] == null ||
                actualBoard[5][0] == null ||
                actualBoard[0][4] == null ||
                actualBoard[1][5] == null ||
                actualBoard[4][8] == null ||
                actualBoard[5][7] == null ||
                actualBoard[8][4] == null ||
                actualBoard[7][3] == null ||
                actualBoard[4][0] == null ||
                actualBoard[3][1] == null ||
                actualBoard[2][2] == null){
            fail();
        }
    }

    @Test
    //two different instances of StandardLivingRoom should have a randomly arranged bag
    //and consequently different Tiles inside the Board
    void boardFilledRandomly(){
        StandardLivingRoom test1 = new StandardLivingRoom(4);
        test1.refillBoard();
        Tile[][] board1 = test1.getBoard();
        StandardLivingRoom test2 = new StandardLivingRoom(4);
        test2.refillBoard();
        Tile[][] board2 = test2.getBoard();
        assertFalse(Arrays.deepEquals(board1, board2));
    }

    @Test
    //check that set and get board work correctly
    void SetGetBoard(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        Tile[][] initialBoard = test.getBoard();
        Tile[][] newBoard = new Tile[9][9];
        newBoard[4][4] = test.getBag().pop();
        test.setBoard(newBoard);
        assertTrue(Arrays.deepEquals(newBoard, test.getBoard()) && !Arrays.deepEquals(initialBoard, test.getBoard()));
    }

    @Test
    //if the Player cannot pick more than 1 Tile from the board (it only contains islands of Tiles)
    //calling refillBoard() should fill the board
    void boardRefilledWhenOnlyIslands(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] newBoard = new Tile[9][9];
        newBoard[4][4] = test.getBag().pop();
        test.setBoard(newBoard);
        assertTrue(Arrays.deepEquals(newBoard, test.getBoard()));
        test.refillBoard();
        assertFalse(Arrays.deepEquals(newBoard, test.getBoard()));
    }

    @Test
    //if the Player can pick more than 1 Tile from the board (it doesn't only contain islands of Tiles)
    //calling refillBoard() should not fill the board
    void boardNotRefilledWhenNotOnlyIslands(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] newBoard1 = new Tile[9][9];
        newBoard1[4][4] = test.getBag().pop();
        newBoard1[4][1] = test.getBag().pop();
        newBoard1[5][1] = test.getBag().pop();
        test.setBoard(newBoard1);
        test.refillBoard();
        assertTrue(Arrays.deepEquals(newBoard1, test.getBoard()));
        Tile[][] newBoard2 = new Tile[9][9];
        newBoard2[4][4] = test.getBag().pop();
        newBoard2[4][1] = test.getBag().pop();
        newBoard2[4][2] = test.getBag().pop();
        test.setBoard(newBoard2);
        test.refillBoard();
        assertTrue(Arrays.deepEquals(newBoard2, test.getBoard()));
    }

    @Test
    //when the board is filled the tiles are taken from the bag (29 Tiles in case of 2 Players and an empty board)
    void bagWorksCorrectly2Players(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertEquals(test.getBag().size(), 132-29);
    }

    @Test
    //when the board is filled the tiles are taken from the bag (37 Tiles in case of 3 Players and an empty board)
    void bagWorksCorrectly3Players(){
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        assertEquals(test.getBag().size(), 132-37);
    }

    @Test
    //when the board is filled the tiles are taken from the bag (45 Tiles in case of 3 Players and an empty board)
    void bagWorksCorrectly4Players(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        assertEquals(test.getBag().size(), 132-45);
    }

    @Test
    //when new StandardLivingRoom(int nPlayers) is called with nPlayers>4
    void numberOfPlayersGreaterThan4(){
        try{
            StandardLivingRoom test = new StandardLivingRoom(5);
            fail();
        }
        catch (IllegalArgumentException e){
            System.out.println("Received");
        }
    }

    @Test
    //when new StandardLivingRoom(int nPlayers) is called with nPlayers<2
    void numberOfPlayersLesserThan2(){
        try{
            StandardLivingRoom test = new StandardLivingRoom(1);
            fail();
        }
        catch (IllegalArgumentException e){
            System.out.println("Received");
        }
    }

    @Test
    //pop() generates an EmptyStackException if bag is empty
    void bagEmptyExceptionWhenRefillingBoard(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        for(int i=0; i<120; i++){
            test.getBag().pop();
        }
        test.refillBoard();
    }
}