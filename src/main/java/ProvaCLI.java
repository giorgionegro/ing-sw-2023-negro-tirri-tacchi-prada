import distibuted.interfaces.AppServer;
import controller.interfaces.GameController;
import distibuted.ClientEndPoint;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.StandardMessage;
import modelView.NewGameInfo;
import modelView.PickedTile;
import modelView.PlayerMoveInfo;
import view.CLI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProvaCLI {

    static Scanner sc;
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        AppServer server = (AppServer) registry.lookup("server");

        CLI cli = new CLI();
        ClientInterface client = new ClientEndPoint(cli);

        ServerInterface sInt = server.connect();
        cli.printCommandLine("Connected to server");
        sc = new Scanner(System.in);

        boolean exit = false;
        while(!exit) {
            String readLine = readLine("(h for commands)-> ",cli);
            switch (readLine) {
                case "h" -> {
                    System.out.println("1: Create a game");
                    System.out.println("2: Join a game");
                    System.out.println("exit: Close this window");
                }
                case "1" -> createGame(cli,client, sInt);
                case "2" -> joinGame(cli, client, sInt);
                case "exit" -> exit = true;
                default -> printError("Wrong command",cli);
            }
        }
    }

    private static void createGame(CLI cli,ClientInterface client, ServerInterface sInt) throws RemoteException {
        String gameId = readLine("GameId: ",cli);
        String p = readLine("PlayerNumber (between 2 and 4): ",cli);
        int k = Integer.parseInt(p);
        if(k>1 && k<5)
            if(sInt.createNewGame(client, new NewGameInfo(gameId, "STANDARD", k)) == ServerInterface.ServerEvent.GAME_CREATED)
                cli.printCommandLine("Game created");
            else
                printError("Game not created",cli);
        else
            printError("Wrong parameters",cli);

    }

    private static void joinGame(CLI cli, ClientInterface client, ServerInterface sInt) throws RemoteException {
        String gameId = readLine("GameId: ",cli);
        if(sInt.getGame(client,gameId)== ServerInterface.ServerEvent.GAME_RETRIEVED){
            GameController.Event e;
            String playerId;
            do {
                playerId = readLine("Write playerId (empty to exit): ",cli);
                if(playerId.equals(""))
                    break;
                else
                    e = sInt.join(client,playerId);

                if (e == GameController.Event.JOINED){
                    cli.printCommandLine("Joined the game");
                    cli.printCommandLine("Waiting for other players");
                    cli.setThisPlayerId(playerId);
                    gameRoutine(cli, client, sInt, playerId);
                } else {
                    printError("ID already taken, chose another id",cli);
                }
            }while(e != GameController.Event.JOINED);
        }else{
            printError("GAME NOT RETRIEVED",cli);
        }
    }

    private static void gameRoutine(CLI cli, ClientInterface client, ServerInterface sInt, String playerId) throws RemoteException {
        String readLine;
        while (true) {
            readLine = readLine("(h for commands)-> ",cli);

            switch (readLine) {
                case "h" -> {
                    cli.printCommandLine("1: Update view status");
                    cli.printCommandLine("2: Pick tiles");
                    cli.printCommandLine("3: Send message");
//                    System.out.println("1: Update view status");
//                    System.out.println("2: Pick tiles");
//                    System.out.println("3: Send message");
                }
                case "1" -> {

                }
                case "2" -> {
                    List<PickedTile> tiles = new ArrayList<>();
                    int pickableNum = 3;
                    boolean choising = true;
                    do {
//                        System.out.println("Remaining pickable tiles: "+pickableNum);
//                        System.out.println("write x,y x2,y2 to pick up to three tiles");
                        cli.printCommandLine("Remaining pickable tiles: "+pickableNum);
                        cli.printCommandLine("write x,y x2,y2 to pick up to three tiles");
                        String choice = readLine("-> ",cli);
                        String[] split = choice.split(" ");
                        //if more than 3 tiles are picked ignores the rest
                        List<PickedTile> tTiles = new ArrayList<PickedTile>();
                        for (int i = 0; i < split.length && i < 3; i++) {
                            try {
                                String[] split1 = split[i].split(",");
                                int x = Integer.parseInt(split1[0]);
                                int y = Integer.parseInt(split1[1]);
                                tTiles.add(new PickedTile(x, y));
                            }
                            catch (NumberFormatException e){
                                printError("Illegal character",cli);
                                break;
                            }
                            catch (ArrayIndexOutOfBoundsException e){
                                printError("Wrong format, Should bex1,y1 x2,y2 x3,y3",cli);
                                break;
                            }
                        }
                        //check if the tiles are pickable, pickable if they are all in the same row or column and adiacent to each other
                        boolean pickable = true;
                        for (int i = 0; i < tTiles.size(); i++) {
                            for (int j = i + 1; j < tTiles.size(); j++) {
                                if (tTiles.get(i).getCol() != tTiles.get(j).getCol() && tTiles.get(i).getRow() != tTiles.get(j).getRow()) {
                                    pickable = false;
                                    break;
                                }
                            }
                            if (!pickable)
                                break;
                        }
                        if (pickable) {
                            tiles.addAll(tTiles);
                            pickableNum -= tTiles.size();
                            choising = false;
                        } else {
                            printError("Tiles not pickable",cli);
                        }
                    }while(choising);
                    choising=true;
                    int sC = 0;
                    do {
                        try {
                            String sCol = readLine("Shelf col: ",cli);
                            sC = Integer.parseInt(sCol);
                            choising=false;
                        }catch(NumberFormatException e){
                            printError("Not a number",cli);
                        }
                    }while(choising);
                    sInt.doPlayerMove(client, new PlayerMoveInfo(tiles, sC));
                }
                case "3" -> {
                    String subject = readLine("Message Subject (empty for everyone): ",cli);
                    String content = readLine("Message content: ",cli);
                    sInt.sendMessage(client, new StandardMessage(playerId,subject,content));
                }
                default -> printError("Wrong command",cli);
            }
        }
    }


    public static String readLine(String message,CLI cli){

        return cli.readCommandLine(message);
//        System.out.print("\u001B[33m"+message+"\u001B[39m");
//        return sc.nextLine();
    }

    public static String readLine(String message){
        System.out.print("\u001B[33m"+message+"\u001B[39m");
        return sc.nextLine();
    }
    public static void printError(String message,CLI cli){

//        System.out.println("\u001B[31m"+message+"\u001B[31m");
        cli.printCommandLine(message,CLI.RED);
    }

}
