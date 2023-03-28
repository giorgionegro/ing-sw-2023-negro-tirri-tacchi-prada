package model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StandardLivingRoomTest {
    @Test
    void StandardLivingRoomContainsEmptyBoard() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] actualBoard = test.getBoard();
        Tile[][] expectedBoard = new Tile[9][9];
        assertTrue(Arrays.deepEquals(expectedBoard, actualBoard));
    }

    @Test
    void twoPlayerBoardHasCorrectEmptyTiles(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        test.setBoard(test.getBoard());
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
    void twoPlayerBoardFilledCorrectly(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        test.setBoard(test.getBoard());
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
    void threePlayerBoardHasCorrectEmptyTiles(){
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        test.setBoard(test.getBoard());
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
    void threePlayerBoardFilledCorrectly(){
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        test.setBoard(test.getBoard());
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
    void fourPlayerBoardHasCorrectEmptyTiles(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        test.setBoard(test.getBoard());
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
    void fourPlayerBoardFilledCorrectly(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        test.setBoard(test.getBoard());
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
    void boardFilledRandomly(){
        StandardLivingRoom test1 = new StandardLivingRoom(4);
        test1.refillBoard();
        test1.setBoard(test1.getBoard());
        Tile[][] board1 = test1.getBoard();
        StandardLivingRoom test2 = new StandardLivingRoom(4);
        test1.refillBoard();
        test2.setBoard(test2.getBoard());
        Tile[][] board2 = test2.getBoard();
        assertFalse(Arrays.deepEquals(board1, board2));
    }

    @Test
    void SetGetBoard(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        test.setBoard(test.getBoard());
        Tile[][] initialBoard = test.getBoard();
        Tile[][] newBoard = new Tile[9][9];
        newBoard[4][4] = test.getBag().pop();
        test.setBoard(newBoard);
        assertTrue(Arrays.deepEquals(newBoard, test.getBoard()) && !Arrays.deepEquals(initialBoard, test.getBoard()));
    }

    @Test
    void boardRefilledWhenOnlyIslands(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] newBoard = new Tile[9][9];
        newBoard[4][4] = test.getBag().pop();
        test.setBoard(newBoard);
        assertTrue(Arrays.deepEquals(newBoard, test.getBoard()));
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertTrue(!Arrays.deepEquals(newBoard, test.getBoard()));
    }

    @Test
    void boardNotRefilledWhenNotOnlyIslands(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] newBoard1 = new Tile[9][9];
        newBoard1[4][4] = test.getBag().pop();
        newBoard1[4][1] = test.getBag().pop();
        newBoard1[5][1] = test.getBag().pop();
        test.setBoard(newBoard1);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertTrue(Arrays.deepEquals(newBoard1, test.getBoard()));
        Tile[][] newBoard2 = new Tile[9][9];
        newBoard2[4][4] = test.getBag().pop();
        newBoard2[4][1] = test.getBag().pop();
        newBoard2[4][2] = test.getBag().pop();
        test.setBoard(newBoard2);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertTrue(Arrays.deepEquals(newBoard2, test.getBoard()));
    }

    @Test
    void bagWorksCorrectly2Players(){
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertTrue(test.getBag().size() == 132-29);
    }

    @Test
    void bagWorksCorrectly3Players(){
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertTrue(test.getBag().size() == 132-37);
    }

    @Test
    void bagWorksCorrectly4Players(){
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertTrue(test.getBag().size() == 132-45);
    }
}