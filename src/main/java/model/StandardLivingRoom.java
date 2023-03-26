package model;

import model.abstractModel.LivingRoom;

import java.util.*;

public class StandardLivingRoom implements LivingRoom {
    private Tile[][] board;
    private final Stack<Tile> bag;
    private final int numberOfPlayers;

    public StandardLivingRoom(int nPlayers){
        this.numberOfPlayers = nPlayers;
        this.bag = loadBag();
        this.board = new Tile[9][9];
    }
    @Override
    public Tile[][] getBoard() {
        return Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    @Override
    public void setBoard(Tile[][] modifiedBoard) {
        //TODO: check modifiedBoard is valid (correct empty spaces in relation to number of Players)
        board = Arrays.stream(modifiedBoard).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    @Override
    public Tile[][] refillBoard() {
        if(needsToBeRefilled()){
            Tile[][] newBoard = getBoard();
            refillAlgorythm(newBoard);
            return newBoard;
        }
        return getBoard();
    }

    public Stack<Tile> getBag(){
        return bag;
    }

    private Stack<Tile> loadBag(){
        Stack<Tile> bag = new Stack<>();
        initializeBag(bag);
        return bag;
    }

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

    private void refillAlgorythm(Tile[][] board){
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

    private void fillCenterSquare(Tile[][] board){
        for(int i=3; i<6; i++){
            for(int j=3; j<6; j++){
                if(board[i][j] == null){
                    try{
                        board[i][j] = bag.pop();
                    }
                    catch (Exception e){
                        if(e instanceof EmptyStackException){
                            System.out.println("Stack vuoto\n");
                        }
                        else {
                            System.out.println("Altra eccezione");
                        }
                    }
                }
            }
        }
    }

    private void fillSideSquares(Tile[][] board, int row, int col){
        for(int i=row; i<row+2; i++){
            for(int j=col; j<col+2; j++){
                if(board[i][j] == null){
                    try{
                        board[i][j] = bag.pop();
                    }
                    catch (Exception e){
                        if(e instanceof EmptyStackException){
                            System.out.println("Stack vuoto\n");
                        }
                        else {
                            System.out.println("Altra eccezione");
                        }
                    }
                }
            }
        }
    }

    private void fillMissingCells(Tile[][] board){
        try {
            board[2][5] = bag.pop();
            board[3][2] = bag.pop();
            board[5][6] = bag.pop();
            board[6][3] = bag.pop();
        }
        catch (Exception e){
            if(e instanceof EmptyStackException){
                System.out.println("Stack vuoto\n");
            }
            else {
                System.out.println("Altra eccezione");
            }
        }
    }

    private void fillMissingCells3Players(Tile[][] board){
        try{
            board[1][3] = bag.pop();
            board[2][2] = bag.pop();
            board[2][6] = bag.pop();
            board[3][8] = bag.pop();
            board[6][6] = bag.pop();
            board[8][5] = bag.pop();
            board[6][2] = bag.pop();
            board[5][0] = bag.pop();
        }
        catch (Exception e){
            if(e instanceof EmptyStackException){
                System.out.println("Stack vuoto\n");
            }
            else {
                System.out.println("Altra eccezione");
            }
        }
    }

    private void fillMissingCells4Players(Tile[][] board){
        fillMissingCells3Players(board);
        try {
            board[0][4] = bag.pop();
            board[1][5] = bag.pop();
            board[4][8] = bag.pop();
            board[5][7] = bag.pop();
            board[8][4] = bag.pop();
            board[7][3] = bag.pop();
            board[4][0] = bag.pop();
            board[3][1] = bag.pop();
        }
        catch (Exception e){
            if(e instanceof EmptyStackException){
                System.out.println("Stack vuoto\n");
            }
            else {
                System.out.println("Altra eccezione");
            }
        }
    }

    private void initializeBag(Stack<Tile> initialBag){
        fillBag7(initialBag);
        fillBag8(initialBag);
        Collections.shuffle(initialBag);
    }

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
