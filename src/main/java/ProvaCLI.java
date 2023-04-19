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
        System.out.println("Connesso");

        sc = new Scanner(System.in);

        boolean exit = false;
        while(!exit){
            System.out.print("-> ");
            String readLine = readLine();
            switch(readLine){
                case "1" -> createGame(client, sInt);
                case "2" -> joinGame(cli, client, sInt);
                case "exit" -> exit = true;
                default -> System.out.println("HAI SBAGLIATO COMANDO");
            }
        }
    }

    private static void createGame(ClientInterface client, ServerInterface sInt) throws RemoteException {
        System.out.println("Write gameId:");
        String gameId = readLine();
        System.out.println("Write PlayerNumber (between 2 and 4)");
        String p = readLine();
        int k = Integer.parseInt(p);
        if(k>1 && k<5) {
            if(sInt.createNewGame(client, new NewGameInfo(gameId, "STANDARD", k)) == ServerInterface.ServerEvent.GAME_CREATED)
                System.out.println("Gioco creato");
            else
                System.out.println("Gioco non creato");
        }else{
            System.out.println("Parametri scritti male");
        }
    }

    private static void joinGame(CLI cli, ClientInterface client, ServerInterface sInt) throws RemoteException {
        System.out.println("Write gameId:");
        String gameId = readLine();
        if(sInt.getGame(client,gameId)== ServerInterface.ServerEvent.GAME_RETRIEVED){
            GameController.Event e;
            String playerId;
            do {
                System.out.println("Write playerId, empty to exit");
                playerId = readLine();
                if(playerId.equals(""))
                    e = GameController.Event.NOT_JOINED;
                else
                    e = sInt.join(client,playerId);
                if (e == GameController.Event.JOINED){
                    System.out.println("Connesso alla partita");
                    gameRoutine(cli, client, sInt, playerId);
                } else {
                    System.err.println("ID already taken, chose another id");
                }
            }while(e != GameController.Event.JOINED);
        }else{
            System.err.println("GAME NOT RETRIEVED");
        }
    }

    private static void gameRoutine(CLI cli, ClientInterface client, ServerInterface sInt, String playerId) throws RemoteException {
        String readLine;
        while (true) {
            System.out.print("-> ");
            readLine = readLine();
            switch (readLine) {
                case "1" -> cli.updateCLI();
                case "2" -> {
                    List<PickedTile> tiles = new ArrayList<>();
                    int pickableNum = 3;
                    do {
                        System.out.println("Remaining pickable tiles: "+pickableNum);
                        System.out.println("Write - to stop");
                        System.out.print("Row: ");
                        String row = readLine();
                        if(row.equals("-"))
                            break;
                        System.out.print("Col: ");
                        String col = readLine();
                        if(col.equals("-"))
                            break;
                        int r;
                        int c;
                        r = Integer.parseInt(row);
                        c = Integer.parseInt(col);
                        tiles.add(new PickedTile(r, c));
                        pickableNum--;
                    }while(pickableNum>0);
                    System.out.print("Shelf col: ");
                    String sCol = readLine();
                    int sC;
                    sC = Integer.parseInt(sCol);
                    sInt.doPlayerMove(client, new PlayerMoveInfo(tiles, sC));
                }
                case "3" -> {
                    System.out.print("Message Subject (empty for everyone): ");
                    String subject = readLine();
                    System.out.print("Message content: ");
                    String content = readLine();
                    sInt.sendMessage(client, new StandardMessage(playerId,subject,content));
                }
            }
        }
    }


    public static String readLine(){
            return sc.nextLine();
    }
}
