import distibuted.ClientEndPoint;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import modelView.GameInfo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ProvaCLI {

    static Scanner sc;
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        AppServer server = (AppServer) registry.lookup("server");

        ClientInterface client = new ClientEndPoint(server.connect());

        ServerInterface sInt = server.connect();
        System.out.println("Connesso");

        sc = new Scanner(System.in);

        while(true){
            String readLine = readLine();
            switch(readLine){
                case "1" -> {
                    System.out.println("Write gameId:");
                    String gameId = readLine();
                    System.out.println("Write PlayerNumber (between 2 and 4)");
                    String p = readLine();
                    int k;
                    k = Integer.parseInt(p);
                    if(k>1 && k<5) {
                        sInt.createNewGame(client, new GameInfo(gameId, "STANDARD", k));
                        System.out.println("Gioco creato :: gameId: " + gameId + ", playerNumber: "+k);
                    }else{
                        System.out.print("Parametri scritti male");
                    }
                }
                case "2" -> {
                    System.out.println("Write gameId:");
                    String gameId = readLine();
                    System.out.println("Write playerId");
                    String playerId = readLine();
                    sInt.connectToGame(client, playerId,gameId);
                    //System.out.println("Gioco creato :: gameId: " + gameId + ", playerId: " + playerId+", playerNumber: "+k);
                }
                default -> System.out.println("Wrong choise");
            }
        }


    }

    public static String readLine(){
            return sc.next();
    }
}
