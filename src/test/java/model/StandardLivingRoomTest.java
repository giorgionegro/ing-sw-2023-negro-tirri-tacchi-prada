package model;

import model.abstractModel.Game;
import modelView.NewGameInfo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("UseOfObsoleteCollectionType")
class StandardLivingRoomTest {
    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
    @Test
    void testConstructor() throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> new StandardLivingRoom(1));
        assertThrows(IllegalArgumentException.class, () -> new StandardLivingRoom(7));
    }

    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
    @Test
    void testConstructor2() throws IllegalArgumentException {
        StandardLivingRoom actualStandardLivingRoom = new StandardLivingRoom(2);
        assertEquals(132, actualStandardLivingRoom.getBag().size());
        assertFalse(actualStandardLivingRoom.hasChanged());
        assertEquals(9, actualStandardLivingRoom.getBoard().length);
    }

    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
    @Test
    void twoPlayerBoardHasCorrectEmptyTiles() {
        //after refillBoard(), check that all the board cells that should be empty actually are
        //when there are 2 Players
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
    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
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
    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
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
    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
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

        if (actualBoard[2][5] == Tile.EMPTY || actualBoard[5][6] == Tile.EMPTY || actualBoard[2][6] == Tile.EMPTY || actualBoard[3][8] == Tile.EMPTY || actualBoard[6][6] == Tile.EMPTY || actualBoard[8][5] == Tile.EMPTY || actualBoard[6][2] == Tile.EMPTY || actualBoard[5][0] == Tile.EMPTY || actualBoard[2][2] == Tile.EMPTY) {
            fail();
        }
    }




    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
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
    /**
     * Method under test: {@link StandardLivingRoom#StandardLivingRoom(int)}
     */
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

        if (actualBoard[2][5] == Tile.EMPTY || actualBoard[5][6] == Tile.EMPTY || actualBoard[2][6] == Tile.EMPTY || actualBoard[3][8] == Tile.EMPTY || actualBoard[6][6] == Tile.EMPTY || actualBoard[8][5] == Tile.EMPTY || actualBoard[6][2] == Tile.EMPTY || actualBoard[5][0] == Tile.EMPTY || actualBoard[0][4] == Tile.EMPTY || actualBoard[1][5] == Tile.EMPTY || actualBoard[4][8] == Tile.EMPTY || actualBoard[5][7] == Tile.EMPTY || actualBoard[8][4] == Tile.EMPTY || actualBoard[7][3] == Tile.EMPTY || actualBoard[4][0] == Tile.EMPTY || actualBoard[3][1] == Tile.EMPTY || actualBoard[2][2] == Tile.EMPTY) {
            fail();
        }
    }


    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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

    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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


    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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


    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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


    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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

    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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

    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
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


    /**
     * Method under test: {@link StandardLivingRoom#getBoard()}
     */
    @Test
    void testGetBoard2() throws IllegalArgumentException {
        assertEquals(9, (new StandardLivingRoom(2)).getBoard().length);
    }


    /**
     * Method under test: {@link StandardLivingRoom#refillBoard()}
     */
    @Test
    void testRefillBoard2() throws IllegalArgumentException {
        StandardLivingRoom standardLivingRoom = new StandardLivingRoom(2);
        standardLivingRoom.refillBoard();
        assertEquals(103, standardLivingRoom.getBag().size());
        assertFalse(standardLivingRoom.hasChanged());
        assertEquals(9, standardLivingRoom.getBoard().length);
    }


    /**
     * Method under test: {@link StandardLivingRoom#getInfo()}
     */
    @Test
    void testGetInfo2() throws IllegalArgumentException {
        assertEquals(9, (new StandardLivingRoom(2)).getInfo().board().length);
    }

    /**
     * Method under test: {@link StandardLivingRoom#getBag()}
     */
    @Test
    void testGetBag() {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        Stack<Tile> actualBag = ((StandardLivingRoom) game.getLivingRoom()).getBag();
        assertFalse(actualBag.isEmpty());
    }
}//package model;
