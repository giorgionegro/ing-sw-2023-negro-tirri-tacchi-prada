package model;

import model.abstractModel.LivingRoom;

import java.util.*;

public class StandardLivingRoom implements LivingRoom {
    private Tile[][] board;
    private final Stack<Tile> bag;
    private final int numberOfPlayers;

    public StandardLivingRoom(int nPlayers){
        this.numberOfPlayers = nPlayers;
        this.bag = new Stack<>();
        throw new UnsupportedOperationException("not implemented yet");
    }
    @Override
    public Tile[][] getBoard() {
        return Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    @Override
    public void setBoard(Tile[][] modifiedBoard) {
        board = Arrays.stream(modifiedBoard).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    @Override
    public void refillBoard() {
        if(needsToBeRefilled()){
            Tile[][] newBoard = getBoard();
            refillAlgorythm(newBoard);
        }
        throw new UnsupportedOperationException("not implemented yet");
    }

    private List<Tile> loadBag(){
        return new ArrayList<>();//TODO aggiungere tiles e fare sorting casuale
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
        for(int i=4; i<7; i++){
            for(int j=4; j<7; j++){
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
            board[5][6] = bag.pop();
            board[6][4] = bag.pop();
            board[4][1] = bag.pop();
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
            board[2][6] = bag.pop();
            board[3][8] = bag.pop();
            board[6][6] = bag.pop();
            board[8][5] = bag.pop();
            board[6][2] = bag.pop();
            board[5][0] = bag.pop();
            board[2][2] = bag.pop();
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
        throw new UnsupportedOperationException("not implemented yet");
    }

    private void fillBag8(Stack<Tile> bag, Tile tile){
        throw new UnsupportedOperationException("not implemented yet");
    }

    private void fillBag7(Stack<Tile> bag, Tile tile){
        throw new UnsupportedOperationException("not implemented yet");
    }
}
