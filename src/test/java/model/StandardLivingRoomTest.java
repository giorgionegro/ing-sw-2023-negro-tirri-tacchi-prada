package model;

import java.util.Stack;

import model.instances.StandardLivingRoomInstance;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardLivingRoomTest {
    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(StandardLivingRoomInstance)}
     */
    @Test
    void testConstructor() {
        StandardLivingRoom actualStandardLivingRoom = new StandardLivingRoom(
                new StandardLivingRoomInstance(new Tile[][]{new Tile[]{Tile.CATS_1}}, new Stack<>(), 10));
        assertTrue(actualStandardLivingRoom.getBag().isEmpty());
        assertFalse(actualStandardLivingRoom.hasChanged());
        assertTrue(actualStandardLivingRoom.getInstance() instanceof StandardLivingRoomInstance);
        assertEquals(1, actualStandardLivingRoom.getBoard().length);
    }

    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(StandardLivingRoomInstance)}
     */
    @Test
    void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> new StandardLivingRoom(null));
    }

    @Test
        //after refillBoard(), check that all the board cells that should be empty actually are
        //when there are 2 Players
    void twoPlayerBoardHasCorrectEmptyTiles() {
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 6; j < 9; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 9; i++) {
            for (int j = 6; j < 9; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        if (actualBoard[0][3] != Tile.EMPTY ||
                actualBoard[0][4] != Tile.EMPTY ||
                actualBoard[0][5] != Tile.EMPTY ||
                actualBoard[1][5] != Tile.EMPTY ||
                actualBoard[3][0] != Tile.EMPTY ||
                actualBoard[3][1] != Tile.EMPTY ||
                actualBoard[3][8] != Tile.EMPTY ||
                actualBoard[4][0] != Tile.EMPTY ||
                actualBoard[4][8] != Tile.EMPTY ||
                actualBoard[5][0] != Tile.EMPTY ||
                actualBoard[5][7] != Tile.EMPTY ||
                actualBoard[5][8] != Tile.EMPTY ||
                actualBoard[7][3] != Tile.EMPTY ||
                actualBoard[8][4] != Tile.EMPTY ||
                actualBoard[8][5] != Tile.EMPTY) {
            fail();
        }
    }

    @Test
        //after refillBoard(), check that all the board cells that should contain a TIle actually do
        //when there are 2 Players
    void twoPlayerBoardFilledCorrectly() {
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 6; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 3; j < 5; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 3; i < 5; i++) {
            for (int j = 6; j < 8; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 4; i < 6; i++) {
            for (int j = 1; j < 3; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 8; i++) {
            for (int j = 4; j < 6; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        if (actualBoard[2][5] == Tile.EMPTY || actualBoard[5][6] == Tile.EMPTY || actualBoard[6][4] == Tile.EMPTY || actualBoard[4][1] == Tile.EMPTY) {
            fail();
        }
    }

    @Test
        //after refillBoard(), check that all the board cells that should be empty actually are
        //when there are 3 Players
    void threePlayerBoardHasCorrectEmptyTiles() {
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 9; i++) {
            for (int j = 0; j < 2; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 7; j < 9; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 9; i++) {
            for (int j = 7; j < 9; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        if (actualBoard[0][2] != Tile.EMPTY ||
                actualBoard[0][4] != Tile.EMPTY ||
                actualBoard[0][5] != Tile.EMPTY ||
                actualBoard[1][2] != Tile.EMPTY ||
                actualBoard[1][5] != Tile.EMPTY ||
                actualBoard[1][6] != Tile.EMPTY ||
                actualBoard[3][0] != Tile.EMPTY ||
                actualBoard[3][1] != Tile.EMPTY ||
                actualBoard[4][0] != Tile.EMPTY ||
                actualBoard[4][8] != Tile.EMPTY ||
                actualBoard[5][7] != Tile.EMPTY ||
                actualBoard[5][8] != Tile.EMPTY ||
                actualBoard[7][2] != Tile.EMPTY ||
                actualBoard[7][3] != Tile.EMPTY ||
                actualBoard[7][6] != Tile.EMPTY ||
                actualBoard[8][2] != Tile.EMPTY ||
                actualBoard[8][3] != Tile.EMPTY ||
                actualBoard[8][4] != Tile.EMPTY ||
                actualBoard[8][6] != Tile.EMPTY) {
            fail();
        }
    }

    @Test
        //after refillBoard(), check that all the board cells that should contain a TIle actually do
        //when there are 3 Players
    void threePlayerBoardFilledCorrectly() {
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();

        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 6; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 3; j < 5; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 3; i < 5; i++) {
            for (int j = 6; j < 8; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 4; i < 6; i++) {
            for (int j = 1; j < 3; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 8; i++) {
            for (int j = 4; j < 6; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        if (actualBoard[2][5] == Tile.EMPTY ||
                actualBoard[5][6] == Tile.EMPTY ||
                actualBoard[6][4] == Tile.EMPTY ||
                actualBoard[4][1] == Tile.EMPTY ||
                actualBoard[1][3] == Tile.EMPTY ||
                actualBoard[2][6] == Tile.EMPTY ||
                actualBoard[3][8] == Tile.EMPTY ||
                actualBoard[6][6] == Tile.EMPTY ||
                actualBoard[8][5] == Tile.EMPTY ||
                actualBoard[6][2] == Tile.EMPTY ||
                actualBoard[5][0] == Tile.EMPTY ||
                actualBoard[2][2] == Tile.EMPTY) {
            fail();
        }
    }

    @Test
        //after refillBoard(), check that all the board cells that should be empty actually are
        //when there are 4 Players
    void fourPlayerBoardHasCorrectEmptyTiles() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 9; i++) {
            for (int j = 0; j < 2; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 7; j < 9; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 9; i++) {
            for (int j = 7; j < 9; j++) {
                if (actualBoard[i][j] != Tile.EMPTY) {
                    fail();
                }
            }
        }

        if (actualBoard[0][2] != Tile.EMPTY ||
                actualBoard[0][5] != Tile.EMPTY ||
                actualBoard[1][2] != Tile.EMPTY ||
                actualBoard[1][6] != Tile.EMPTY ||
                actualBoard[3][0] != Tile.EMPTY ||
                actualBoard[5][8] != Tile.EMPTY ||
                actualBoard[7][2] != Tile.EMPTY ||
                actualBoard[7][6] != Tile.EMPTY ||
                actualBoard[8][2] != Tile.EMPTY ||
                actualBoard[8][3] != Tile.EMPTY ||
                actualBoard[8][6] != Tile.EMPTY) {
            fail();
        }
    }

    @Test
        //after refillBoard(), check that all the board cells that should contain a TIle actually do
        //when there are 4 Players
    void fourPlayerBoardFilledCorrectly() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        Tile[][] actualBoard = test.getBoard();

        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 6; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 3; j < 5; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 3; i < 5; i++) {
            for (int j = 6; j < 8; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 4; i < 6; i++) {
            for (int j = 1; j < 3; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        for (int i = 6; i < 8; i++) {
            for (int j = 4; j < 6; j++) {
                if (actualBoard[i][j] == Tile.EMPTY) {
                    fail();
                }
            }
        }

        if (actualBoard[2][5] == Tile.EMPTY ||
                actualBoard[5][6] == Tile.EMPTY ||
                actualBoard[6][4] == Tile.EMPTY ||
                actualBoard[4][1] == Tile.EMPTY ||
                actualBoard[1][3] == Tile.EMPTY ||
                actualBoard[2][6] == Tile.EMPTY ||
                actualBoard[3][8] == Tile.EMPTY ||
                actualBoard[6][6] == Tile.EMPTY ||
                actualBoard[8][5] == Tile.EMPTY ||
                actualBoard[6][2] == Tile.EMPTY ||
                actualBoard[5][0] == Tile.EMPTY ||
                actualBoard[0][4] == Tile.EMPTY ||
                actualBoard[1][5] == Tile.EMPTY ||
                actualBoard[4][8] == Tile.EMPTY ||
                actualBoard[5][7] == Tile.EMPTY ||
                actualBoard[8][4] == Tile.EMPTY ||
                actualBoard[7][3] == Tile.EMPTY ||
                actualBoard[4][0] == Tile.EMPTY ||
                actualBoard[3][1] == Tile.EMPTY ||
                actualBoard[2][2] == Tile.EMPTY) {
            fail();
        }
    }

    @Test
        //two different instances of StandardLivingRoom should have a randomly arranged bag
        //and consequently different Tiles inside the Board
    void boardFilledRandomly() {
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
    void SetGetBoard() {
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
    void boardRefilledWhenOnlyIslands() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        Tile[][] newBoard = new Tile[9][9];
        Arrays.stream(newBoard).forEach(row -> Arrays.fill(row, Tile.EMPTY));
        newBoard[4][4] = test.getBag().pop();
        test.setBoard(newBoard);
        assertTrue(Arrays.deepEquals(newBoard, test.getBoard()));
        test.refillBoard();
        assertFalse(Arrays.deepEquals(newBoard, test.getBoard()));
    }

    @Test
        //if the Player can pick more than 1 Tile from the board (it doesn't only contain islands of Tiles)
        //calling refillBoard() should not fill the board
    void boardNotRefilledWhenNotOnlyIslands() {
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
    void bagWorksCorrectly2Players() {
        StandardLivingRoom test = new StandardLivingRoom(2);
        test.refillBoard();
        test.setBoard(test.getBoard());
        assertEquals(test.getBag().size(), 132 - 29);
    }

    @Test
        //when the board is filled the tiles are taken from the bag (37 Tiles in case of 3 Players and an empty board)
    void bagWorksCorrectly3Players() {
        StandardLivingRoom test = new StandardLivingRoom(3);
        test.refillBoard();
        assertEquals(test.getBag().size(), 132 - 37);
    }

    @Test
        //when the board is filled the tiles are taken from the bag (45 Tiles in case of 3 Players and an empty board)
    void bagWorksCorrectly4Players() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        test.refillBoard();
        assertEquals(test.getBag().size(), 132 - 45);
    }

    @Test
        //when new StandardLivingRoom(int nPlayers) is called with nPlayers>4
    void numberOfPlayersGreaterThan4() {
        try {
            StandardLivingRoom test = new StandardLivingRoom(5);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println("Received");
        }
    }

    @Test
        //when new StandardLivingRoom(int nPlayers) is called with nPlayers<2
    void numberOfPlayersLesserThan2() {
        try {
            StandardLivingRoom test = new StandardLivingRoom(1);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println("Received");
        }
    }

    @Test
        //board stops refilling if bag is empty
    void boardRefillWorksCorrectlyCenterSquare() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        for (int i = 0; i < 130; i++) {
            test.getBag().pop();
        }
        test.refillBoard();
        assertTrue(test.getBoard()[3][5] == Tile.EMPTY && test.getBoard()[3][4] != Tile.EMPTY);
    }

    @Test
        //board stops refilling if bag is empty
    void boardRefillWorksCorrectlySideSquares() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        for (int i = 0; i < 122; i++) {
            test.getBag().pop();
        }
        test.refillBoard();
        assertTrue(test.getBoard()[1][4] == Tile.EMPTY && test.getBoard()[1][3] != Tile.EMPTY);
    }

    @Test
        //board stops refilling if bag is empty
    void boardRefillWorksCorrectlyMissingCells() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        for (int i = 0; i < 106; i++) {
            test.getBag().pop();
        }
        test.refillBoard();
        assertTrue(test.getBoard()[3][2] == Tile.EMPTY && test.getBoard()[2][5] != Tile.EMPTY);
    }

    @Test
        //board stops refilling if bag is empty
    void boardRefillWorksCorrectlyMissingCells3Players() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        for (int i = 0; i < 102; i++) {
            test.getBag().pop();
        }
        test.refillBoard();
        assertTrue(test.getBoard()[2][2] == Tile.EMPTY && test.getBoard()[0][3] != Tile.EMPTY);
    }

    @Test
        //board stops refilling if bag is empty
    void boardRefillWorksCorrectlyMissingCells4Players() {
        StandardLivingRoom test = new StandardLivingRoom(4);
        for (int i = 0; i < 94; i++) {
            test.getBag().pop();
        }
        test.refillBoard();
        assertTrue(test.getBoard()[1][5] == Tile.EMPTY && test.getBoard()[0][4] != Tile.EMPTY);
    }

    @Test
    void getInfoTest() {
        StandardLivingRoom test = new StandardLivingRoom(2);
        assertTrue(Arrays.deepEquals(test.getBoard(), test.getInfo().board()));
    }
}