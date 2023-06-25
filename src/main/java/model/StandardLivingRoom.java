package model;

import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * This class is an implementation of {@link LivingRoom}
 */
@SuppressWarnings("UseOfObsoleteCollectionType")
public class StandardLivingRoom extends LivingRoom {
    /**
     * Bag of Tiles used to fill the board
     */
    private final Stack<Tile> bag;
    /**
     * Number of players of the game that uses this Living Room
     */
    private final int numberOfPlayers;
    /**
     * Game board of this living room
     */
    private Tile[][] board;

    /**
     * Class constructor, initializes the attribute numberOfPlayer, initializes the bag and loads it with {@link #loadBag()}, initializes the board with an empty board
     *
     * @param nPlayers the number of players for this game
     * @throws IllegalArgumentException if the number of player is below 2 or over 4
     */
    public StandardLivingRoom(int nPlayers) throws IllegalArgumentException {
        super();
        if (nPlayers < 2 || nPlayers > 4) {
            throw new IllegalArgumentException("Number of players must be between 2 and 4.");
        }
        this.numberOfPlayers = nPlayers;
        this.bag = this.loadBag();
        this.board = new Tile[9][9];
        Arrays.stream(this.board).forEach(tiles -> Arrays.fill(tiles, Tile.EMPTY));
    }

    /**
     * this method returns a copy of the game board
     *
     * @return a 2D array of Tiles representing the game board
     */
    @Override
    public Tile[][] getBoard() {
        return Arrays.stream(this.board).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * sets board attribute with a modified board
     *
     * @param modifiedBoard a 2D array of Tiles
     */
    @Override
    public void setBoard(Tile[][] modifiedBoard) {
        this.board = Arrays.stream(modifiedBoard).map(Tile[]::clone).toArray(Tile[][]::new);
        this.setChanged();
        this.notifyObservers(Event.BOARD_MODIFIED);
    }

    /**
     * refillBoard() is called before every turn
     * if {@link #needsToBeRefilled()} is true this method acquires a copy of the game board, uses the method {@link #refillAlgorithm(Tile[][])} to refill it and sets the board with this filled copy
     */
    @Override
    public void refillBoard() {
        if (this.needsToBeRefilled()) {
            Tile[][] newBoard = this.getBoard();
            this.refillAlgorithm(newBoard);
            this.setBoard(newBoard);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return An {@link LivingRoomInfo} representing this object instance
     */
    @Override
    public LivingRoomInfo getInfo() {
        return new LivingRoomInfo(this.getBoard());
    }

    /**
     * this method returns the game bag
     *
     * @return {@link #bag}
     */
    public Stack<Tile> getBag() {
        return this.bag;
    }

    /**
     * load the game bag using {@link #initializeBag(Stack)}
     *
     * @return full stack of Tiles representing the game bag
     */
    private Stack<Tile> loadBag() {
        Stack<Tile> bag = new Stack<>();
        this.initializeBag(bag);
        return bag;
    }

    /**
     * this returns true if the board is empty and if the board is made only of "islands" of tiles (on which case the player can only pick one Tile at a time), false otherwise
     *
     * @return true or false whether the board needs to be refilled or not
     */
    private boolean needsToBeRefilled() {
        for (int i = 0; i < this.board.length - 1; i++) {
            for (int j = 0; j < this.board[i].length - 1; j++) {
                if (this.board[i][j] != Tile.EMPTY) {
                    if (this.board[i + 1][j] != Tile.EMPTY || this.board[i][j + 1] != Tile.EMPTY) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * this method is responsible for calling the methods needed to fill the board, namely
     * {@link #fillCenterSquare(Tile[][])} that fills the center part of the board, always called
     * {@link #fillSideSquares(Tile[][], int, int)} (Tile[][])} that fills the squares around the center, always called
     * {@link #fillMissingCells(Tile[][])} (Tile[][])} that fills the missing cells needed for the 2 player board, always called
     * {@link #fillMissingCells3Players(Tile[][])} (Tile[][])} (Tile[][])} that fills the missing cells needed for the 3 player board, called if 3 players are in the game
     * {@link #fillMissingCells4Players(Tile[][])} (Tile[][])} (Tile[][])} that fills the missing cells needed for the 4 player board, called if 4 players are in the game
     *
     * @param board a 2D array of Tiles representing the game board
     */
    private void refillAlgorithm(Tile[][] board) {
        this.fillCenterSquare(board);
        this.fillSideSquares(board, 1, 3);
        this.fillSideSquares(board, 3, 6);
        this.fillSideSquares(board, 6, 4);
        this.fillSideSquares(board, 4, 1);
        this.fillMissingCells(board);
        if (this.numberOfPlayers == 3) {
            this.fillMissingCells3Players(board);
        }
        if (this.numberOfPlayers == 4) {
            this.fillMissingCells4Players(board);
        }

    }


    /**
     * this method fills the center part of the board (3x3 square) with tile form the bag
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     * if the bag is empty it stops refilling
     *
     * @param board a 2D array of Tiles representing the game board
     */
    private void fillCenterSquare(Tile[][] board) {
        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 6; j++) {
                if (board[i][j] == Tile.EMPTY && this.bag.size() != 0) {
                    board[i][j] = this.bag.pop();
                    if (this.bag.size() == 0)
                        break;
                } else if (this.bag.isEmpty())
                    break;
            }
        }
    }

    /**
     * this method fills a 2x2 square of the board starting from row "row" and column "col" with tiles from the bag
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     * if the bag is empty it stops refilling
     *
     * @param board a 2D array of Tiles representing the game board
     * @param row   starting row of square that needs to be filled
     * @param col   starting column of square that need to be refilled
     */
    private void fillSideSquares(Tile[][] board, int row, int col) {
        for (int i = row; i < row + 2; i++) {
            for (int j = col; j < col + 2; j++) {
                if (board[i][j] == Tile.EMPTY && this.bag.size() != 0) {
                    board[i][j] = this.bag.pop();
                    if (this.bag.size() == 0)
                        break;
                } else if (this.bag.isEmpty())
                    break;
            }
        }
    }

    /**
     * this method fills the missing cells of the 2 player board
     * every two cells of the pair array represents the row and column of the cells that need to be filled
     *
     * @param board a 2D array of Tiles representing the game board
     *              if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     */
    private void fillMissingCells(Tile[][] board) {
        int[] pairs = new int[]{2, 5, 3, 2, 5, 6, 6, 3};
        for (int i = 0; i < pairs.length; i = i + 2) {
            if (board[pairs[i]][pairs[i + 1]] == Tile.EMPTY && this.bag.size() != 0) {
                board[pairs[i]][pairs[i + 1]] = this.bag.pop();
                if (this.bag.size() == 0)
                    break;
            } else if (this.bag.isEmpty())
                break;
        }
    }

    /**
     * this method fills the missing cells of the 3 player board
     * every two cells of the pair array represents the row and column of the cells that need to be filled
     *
     * @param board a 2D array of Tiles representing the game board
     *              if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     */
    private void fillMissingCells3Players(Tile[][] board) {
        int[] pairs = new int[]{0, 3, 2, 2, 2, 6, 3, 8, 6, 6, 8, 5, 6, 2, 5, 0};
        for (int i = 0; i < pairs.length; i = i + 2) {
            if (board[pairs[i]][pairs[i + 1]] == Tile.EMPTY && this.bag.size() != 0) {
                board[pairs[i]][pairs[i + 1]] = this.bag.pop();
                if (this.bag.size() == 0)
                    break;
            } else if (this.bag.isEmpty())
                break;
        }
    }

    /**
     * this method fills the missing cells of the 4 player board
     * every two cells of the pair array represents the row and column of the cells that need to be filled
     *
     * @param board a 2D array of Tiles representing the game board
     *              if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     */
    private void fillMissingCells4Players(Tile[][] board) {
        this.fillMissingCells3Players(board);
        int[] pairs = new int[]{0, 4, 1, 5, 4, 8, 5, 7, 8, 4, 7, 3, 4, 0, 3, 1};
        for (int i = 0; i < pairs.length; i = i + 2) {
            if (board[pairs[i]][pairs[i + 1]] == Tile.EMPTY && this.bag.size() != 0) {
                board[pairs[i]][pairs[i + 1]] = this.bag.pop();
                if (this.bag.size() == 0)
                    break;
            } else if (this.bag.isEmpty())
                break;
        }
    }

    /**
     * this method loads the bag with 132 tiles using methods
     * {@link #fillBag7(Stack)}
     * and
     * {@link #fillBag8(Stack)}
     * and then shuffles the Tiles so that they're in random order
     *
     * @param initialBag the stack of Tiles representing the bag
     */
    private void initializeBag(Stack<Tile> initialBag) {
        this.fillBag7(initialBag);
        this.fillBag8(initialBag);
        Collections.shuffle(initialBag);
    }

    /**
     * fills the bag with 8 of one type of one color Tile foreach color of Tile
     *
     * @param bag stack of Tiles representing the game bag
     */
    private void fillBag8(Stack<Tile> bag) {
        for (int i = 0; i < 8; i++) {
            bag.push(Tile.BOOKS_1);
            bag.push(Tile.CATS_1);
            bag.push(Tile.FRAMES_1);
            bag.push(Tile.GAMES_1);
            bag.push(Tile.PLANTS_1);
            bag.push(Tile.TROPHIES_1);
        }
    }

    /**
     * fills the bag with 7 of two types of one color Tile foreach color of Tile
     *
     * @param bag stack of Tiles representing the game bag
     */
    private void fillBag7(Stack<Tile> bag) {
        for (int i = 0; i < 7; i++) {
            bag.push(Tile.BOOKS_2);
            bag.push(Tile.BOOKS_3);
            bag.push(Tile.CATS_2);
            bag.push(Tile.CATS_3);
            bag.push(Tile.FRAMES_2);
            bag.push(Tile.FRAMES_3);
            bag.push(Tile.GAMES_2);
            bag.push(Tile.GAMES_3);
            bag.push(Tile.PLANTS_2);
            bag.push(Tile.PLANTS_3);
            bag.push(Tile.TROPHIES_2);
            bag.push(Tile.TROPHIES_3);
        }
    }
}
