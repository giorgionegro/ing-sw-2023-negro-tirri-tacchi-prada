package model;

import model.abstractModel.LivingRoom;

import java.util.*;

public class StandardLivingRoom extends LivingRoom {
    private Tile[][] board;
    private final Stack<Tile> bag;
    private final int numberOfPlayers;

    /**
     * Class constructor, initializes the attribute numberOfPlayer, initializes the bag and loads it with {@link #loadBag()}, initializes the board with an empty board
     * @param nPlayers the number of players for this game
     * @throws IllegalArgumentException if the number of player is below 2 or over 4
     */
    public StandardLivingRoom(int nPlayers) throws IllegalArgumentException{
        if(nPlayers <2 || nPlayers > 4){
            throw new IllegalArgumentException("Number of players must be between 2 and 4.");
        }
        this.numberOfPlayers = nPlayers;
        this.bag = loadBag();
        this.board = new Tile[9][9];
    }

    /**
     * this method returns a copy of the game board
     * @return a 2D array of Tiles representing the game board
     */
    @Override
    public Tile[][] getBoard() {
        return Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * sets board attribute with a modified board
     * @param modifiedBoard a 2D array of Tiles
     */
    @Override
    public void setBoard(Tile[][] modifiedBoard) {
        //TODO: check modifiedBoard is valid (correct empty spaces in relation to number of Players)
        board = Arrays.stream(modifiedBoard).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * refillBoard() is called before every turn
     * if {@link #needsToBeRefilled()} is true this method acquires a copy of the game board, uses the method {@link #refillAlgorithm(Tile[][])} to refill it and sets the board with this filled copy
     */
    @Override
    public void refillBoard() {
        if(needsToBeRefilled()){
            Tile[][] newBoard = getBoard();
            refillAlgorithm(newBoard);
            setBoard(newBoard);
        }
    }

    /**
     * this method returns the game bag
     * @return a stack of Tiles representing the game bag
     */
    public Stack<Tile> getBag(){
        return bag;
    }

    /**
     * load the game bag using {@link #initializeBag(Stack)}
     * @return full stack of Tiles representing the game bag
     */
    private Stack<Tile> loadBag(){
        Stack<Tile> bag = new Stack<>();
        initializeBag(bag);
        return bag;
    }

    /**
     * this returns true if the board is empty and if the board is made only of "islands" of tiles (on which case the player can only pick one Tile at a time), false otherwise
     * @return true or false whether the board needs to be refilled or not
     */
    private boolean needsToBeRefilled(){
        for(int i=0; i< board.length-1; i++){
            for(int j=0; j<board[i].length-1; j++){
                if(board[i][j] != null){
                    if(board[i+1][j] != null || board[i][j+1] != null){
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
     * {@link #fillMissingCells(Tile[][])} (Tile[][])} that fills the missing cells needed for the 2 player baord, always called
     * {@link #fillMissingCells3Players(Tile[][])} (Tile[][])} (Tile[][])} that fills the missing cells needed for the 3 player baord, called if 3 players are in the game
     * {@link #fillMissingCells4Players(Tile[][])} (Tile[][])} (Tile[][])} that fills the missing cells needed for the 4 player baord, called if 4 players are in the game
     * @param board a 2D array of Tiles representing the game board
     */
    private void refillAlgorithm(Tile[][] board){
        fillCenterSquare(board);
        fillSideSquares(board, 1, 3);
        fillSideSquares(board, 3, 6);
        fillSideSquares(board, 6, 4);
        fillSideSquares(board, 4, 1);
        fillMissingCells(board);
        if(numberOfPlayers == 3){
            fillMissingCells3Players(board);
        }
        if(numberOfPlayers == 4){
            fillMissingCells4Players(board);
        }
    }

    /**
     * this method fills the center part of the board (3x3 square) with tile form the bag
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     * if the bag is empty it stops refilling
     * @param board a 2D array of Tiles representing the game board
     */
    private void fillCenterSquare(Tile[][] board){
        for(int i=3; i<6; i++){
            for(int j=3; j<6; j++){
                if(board[i][j] == null && bag.size() != 0){
                    board[i][j] = bag.pop();
                    if (bag.size() == 0)
                        break;
                }
                else if(bag.isEmpty())
                        break;
            }
        }
    }

    /**
     * this method fills a 2x2 square of the board starting from row "row" and column "col" with tiles from the bag
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     * if the bag is empty it stops refilling
     * @param board a 2D array of Tiles representing the game board
     * @param row starting row of square that needs to be filled
     * @param col starting column of square that need to be refilled
     */
    private void fillSideSquares(Tile[][] board, int row, int col){
        for(int i=row; i<row+2; i++){
            for(int j=col; j<col+2; j++){
                if(board[i][j] == null && bag.size() != 0){
                    board[i][j] = bag.pop();
                    if (bag.size() == 0)
                        break;
                }
                else if(bag.isEmpty())
                    break;
            }
        }
    }

    /**
     * this method fills the missing cells of the 2 player board
     * every two cells of the pair array represents the row and column of the cells that need to be filled
     * @param board a 2D array of Tiles representing the game board
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     */
    private void fillMissingCells(Tile[][] board){
        int[] pairs = new int[] {2,5,3,2,5,6,6,3};
        for(int i=0; i<pairs.length; i=i+2){
            if(board[pairs[i]][pairs[i+1]] == null && bag.size() != 0){
                board[pairs[i]][pairs[i+1]] = bag.pop();
                if (bag.size() == 0)
                    break;
            }
            else if(bag.isEmpty())
                break;
        }
    }

    /**
     * this method fills the missing cells of the 3 player board
     * every two cells of the pair array represents the row and column of the cells that need to be filled
     * @param board a 2D array of Tiles representing the game board
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     */
    private void fillMissingCells3Players(Tile[][] board){
        int[] pairs = new int[] {0,3,2,2,2,6,3,8,6,6,8,5,6,2,5,0};
        for(int i=0; i<pairs.length; i=i+2){
            if(board[pairs[i]][pairs[i+1]] == null && bag.size() != 0){
                board[pairs[i]][pairs[i+1]] = bag.pop();
                if (bag.size() == 0)
                    break;
            }
            else if(bag.isEmpty())
                break;
        }
    }
    /**
     * this method fills the missing cells of the 4 player board
     * every two cells of the pair array represents the row and column of the cells that need to be filled
     * @param board a 2D array of Tiles representing the game board
     * if a cell is empty it is filled with a Tile removed from the bag, otherwise it is unchanged
     */
    private void fillMissingCells4Players(Tile[][] board){
        fillMissingCells3Players(board);
        int[] pairs = new int[] {0,4,1,5,4,8,5,7,8,4,7,3,4,0,3,1};
        for(int i=0; i<pairs.length; i=i+2){
            if(board[pairs[i]][pairs[i+1]] == null && bag.size() != 0){
                board[pairs[i]][pairs[i+1]] = bag.pop();
                if (bag.size() == 0)
                    break;
            }
            else if(bag.isEmpty())
                break;
        }
    }

    /**
     * this method loads the bag with 132 tiles using methods
     * {@link #fillBag7(Stack)}
     * and
     * {@link #fillBag8(Stack)}
     * and then shuffles the Tiles so that they're in random order
     * @param initialBag the stack of Tiles representing the bag
     */
    private void initializeBag(Stack<Tile> initialBag){
        fillBag7(initialBag);
        fillBag8(initialBag);
        Collections.shuffle(initialBag);
    }

    /**
     * fills the bag with 8 of one type of one color Tile foreach color of Tile
     * @param bag stack of Tiles representing the game bag
     */
    private void fillBag8(Stack<Tile> bag){
        for(int i=0; i<8; i++){
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
     * @param bag stack of Tiles representing the game bag
     */
    private void fillBag7(Stack<Tile> bag){
        for(int i=0; i<7; i++){
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
