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
        System.out.println("Connected to server");

        sc = new Scanner(System.in);

        boolean exit = false;
        while(!exit) {
            String readLine = readLine("(h for commands)-> ");
            switch (readLine) {
                case "h" -> {
                    System.out.println("1: Create a game");
                    System.out.println("2: Join a game");
                    System.out.println("exit: Close this window");
                }
                case "1" -> createGame(client, sInt);
                case "2" -> joinGame(cli, client, sInt);
                case "exit" -> exit = true;
                default -> printError("Wrong command");
            }
        }
    }

    private static void createGame(ClientInterface client, ServerInterface sInt) throws RemoteException {
        String gameId = readLine("GameId: ");
        String p = readLine("PlayerNumber (between 2 and 4): ");
        int k = Integer.parseInt(p);
        if(k>1 && k<5) {
            if(sInt.createNewGame(client, new NewGameInfo(gameId, "STANDARD", k)) == ServerInterface.ServerEvent.GAME_CREATED)
                System.out.println("Game created");
            else
                printError("Game not created");
        }else{
            printError("Wrong parameters");
        }
    }

    private static void joinGame(CLI cli, ClientInterface client, ServerInterface sInt) throws RemoteException {
        String gameId = readLine("GameId: ");
        if(sInt.getGame(client,gameId)== ServerInterface.ServerEvent.GAME_RETRIEVED){
            GameController.Event e;
            String playerId;
            do {
                playerId = readLine("Write playerId (empty to exit): ");
                if(playerId.equals(""))
                    break;
                else
                    e = sInt.join(client,playerId);
                if (e == GameController.Event.JOINED){
                    System.out.println("Joined the game");
                    System.out.println("Connesso alla partita");
                    cli.setThisPlayerId(playerId);
                    gameRoutine(cli, client, sInt, playerId);
                } else {
                    printError("ID already taken, chose another id");
                }
            }while(e != GameController.Event.JOINED);
        }else{
            printError("GAME NOT RETRIEVED");
        }
    }

    private static void gameRoutine(CLI cli, ClientInterface client, ServerInterface sInt, String playerId) throws RemoteException {
        String readLine;
        while (true) {
            readLine = readLine("(h for commands)-> ");
            switch (readLine) {
                case "h" -> {
                    System.out.println("1: Update view status");
                    System.out.println("2: Pick tiles");
                    System.out.println("3: Send message");
                }
                case "1" -> cli.updateCLI();
                case "2" -> {
                    List<PickedTile> tiles = new ArrayList<>();
                    int pickableNum = 3;
                    boolean choising = true;
                    do {
                        System.out.println("Remaining pickable tiles: "+pickableNum);
                        System.out.println("Empty to stop, + to add, - to remove last chosen tile");
                        String choice = readLine("-> ");
                        switch (choice){
                            case "+" ->{
                                if(pickableNum>0) {
                                    String row = readLine("Row: ");
                                    String col = readLine("Col: ");
                                    int r;
                                    int c;
                                    r = Integer.parseInt(row);
                                    c = Integer.parseInt(col);
                                    tiles.add(new PickedTile(r, c));
                                    pickableNum--;
                                }else{
                                    System.out.println("No more tiles to pick");
                                }
                            }
                            case "-" -> {
                                if(tiles.size()>0)
                                    tiles.remove(tiles.size()-1);
                            }
                            case "" -> choising=false;
                            default -> System.out.println("Wrong command");
                        }
                    }while(choising);
                    String sCol = readLine("Shelf col: ");
                    int sC;
                    sC = Integer.parseInt(sCol);
                    sInt.doPlayerMove(client, new PlayerMoveInfo(tiles, sC));
                }
                case "3" -> {
                    String subject = readLine("Message Subject (empty for everyone): ");
                    String content = readLine("Message content: ");
                    sInt.sendMessage(client, new StandardMessage(playerId,subject,content));
                }
                default -> printError("Wrong command");
            }
        }
    }


    public static String readLine(String message){
            System.out.print((char)27+"[33m"+message+(char)27+"[39m");
            return sc.nextLine();
    }

    public static void printError(String message){
        System.out.println((char)27+"[31m"+message+(char)27+"[39m");
    }
}
