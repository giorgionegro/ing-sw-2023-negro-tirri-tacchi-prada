import distibuted.interfaces.AppServer;
import distibuted.ClientEndPoint;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ServerStub;
import view.CLI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ProvaCLI {


    public static void main(String[] args) throws RemoteException, NotBoundException {
        CLI cli = new CLI();
        ClientInterface client = new ClientEndPoint(cli);
        ServerInterface server = null;
        do{
            String choise = readLine("Connect with RMI (r) or SOCKET (s)? : ",cli);
            switch (choise){
                case "r" -> {
                    try{
                        server = ((AppServer) LocateRegistry.getRegistry().lookup("server")).connect(client);
                    } catch (RemoteException e){
                        e.printStackTrace();
                        printError("Unable to use RMI",cli);
                    }
                }
                case "s" -> {
                    try{
                        ServerStub s = new ServerStub("localhost", 1234);
                        server = s.connect(client);
                        new Thread(()->{
                            while(true){
                                try {
                                    s.waitForReceive(client);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).start();
                    } catch (RemoteException e){
                        printError("Unable to use SOCKET", cli);
                    }
                }
                default -> printError("Wrong choise",cli);
            }
        }while(server==null);

        cli.runLoginView(client,server);
    }




    public static String readLine(String message,CLI cli){

        return cli.readCommandLine(message);
//        System.out.print("\u001B[33m"+message+"\u001B[39m");
//        return sc.nextLine();
    }

    public static void printError(String message,CLI cli){

//        System.out.println("\u001B[31m"+message+"\u001B[31m");
        cli.printCommandLine(message,CLI.RED);
    }

}
