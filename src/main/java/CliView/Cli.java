package CliView;

import model.Tile;
import modelView.*;
import java.util.Scanner;

public class Cli {
    private String idPlayer;
    private ShelfsView shelfsView;
    private LivingRoomView livingRoomView;

    public void printMatrix(Tile[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == null) {
                    System.out.print(" ");
                } else {
                    String colour = matrix[i][j].getColor();
                    //if the tile is empty, print a O
                    if (colour.equals(Tile.EMPTY.getColor())) {
                        System.out.print("  ");
                    } else {
                        //if the tile is not empty, print X in the colour of the tile
                        switch (colour) {
                            case "Green" -> System.out.print("\u001B[32m█ \u001B[0m");
                            case "White" -> System.out.print("\u001B[37m█ \u001B[0m");
                            case "Yellow" -> System.out.print("\u001B[33m█ \u001B[0m");
                            case "Blue" -> System.out.print("\u001B[34m█ \u001B[0m");
                            case "LightBlue" -> System.out.print("\u001B[36m█ \u001B[0m");
                            case "Magenta" -> System.out.print("\u001B[35m█ \u001B[0m");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    //main menu
    /*
    join game
    create game
     */
    public void StartMenu() {
        System.out.println("Welcome to Myshelfie!");
        System.out.println("1. Join Game");
        System.out.println("2. Create Game");
        System.out.println("3. Exit");

        //read input
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> JoinGame();
            case "2" -> CreateGame();
            case "3" -> System.exit(0);
            default -> System.out.println("Invalid input");
        }



    }

    private void CreateGame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void JoinGame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //turn menu
    /*
    print shelf
    print board
    print enemy shelf
    print status (common goals, personal goals)
    print enemy status -personal goals
    do action
     */
    public void TurnMenu() {
     System.out.println("1. Print Shelf");
        System.out.println("2. Print Board");
        System.out.println("3. Print Enemy Shelf");
        System.out.println("4. Print Status");
        System.out.println("5. Print Enemy Status");
        System.out.println("6. Do Action");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> PrintShelf();
            case "2" -> PrintBoard();
            case "3" -> PrintEnemyShelf();
            case "4" -> PrintStatus();
            case "5" -> PrintEnemyStatus();
            case "6" -> DoAction();
            default -> System.out.println("Invalid input");
        }


    }

    private void DoAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void PrintEnemyStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void PrintStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void PrintEnemyShelf() {
        String[] players = shelfsView.getPlayersIds();
        for (String player : players) {
            if (!player.equals(idPlayer)) {
                System.out.println("Player " + player + " shelf:");
                Tile[][] tiles = shelfsView.getPlayerShelf(player);
                printMatrix(tiles);
            }
        }
    }

    private void PrintBoard() {
        Tile[][] tiles=livingRoomView.getBoard();
        printMatrix(tiles);//TODO print pickable tiles in a different char(?)
    }

    private void PrintShelf() {
        Tile[][] tiles=shelfsView.getPlayerShelf(idPlayer);
        printMatrix(tiles);
    }
}

