import distibuted.interfaces.AppServer;
import distibuted.ClientEndPoint;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ClientSocketHandler;
import view.CLI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ProvaCLI {
    public static void main(String[] args){
        CLI cli = new CLI();
        try{
            ClientInterface client = new ClientEndPoint(cli);
            AppServer app = null;
            ServerInterface server = null;
            do {
                String choise = readLine("Connect with RMI (r) or SOCKET (s)? : ", cli);
                switch (choise) {
                    case "r" -> {
                        try {
                            app = (AppServer) LocateRegistry.getRegistry().lookup("server");
                            server = app.connect(client);
                            cli.printCommandLine("Connected with RMI", CLI.GREEN);
                        } catch (RemoteException | NotBoundException e) {
                            printError("Unable to use RMI", cli);
                        }
                    }
                    case "s" -> {
                        try {
                            app = new ClientSocketHandler("localhost", 1234);
                            server = app.connect(client);
                            cli.printCommandLine("Connected with SOCKET", CLI.GREEN);
                        } catch (RemoteException e) {
                            printError("Unable to use SOCKET", cli);
                        }
                    }
                    default -> printError("Wrong choise", cli);
                }
            } while (server == null);

            cli.runLoginView(client, server);

            app.disconnect(client);
        }catch (RemoteException e){
            printError("Critical error, shutting down",cli);
        }
    }




    public static String readLine(String message,CLI cli){
        String s = cli.readCommandLine(message);
        cli.render();
        return s;
    }

    public static void printError(String message,CLI cli){
        cli.printCommandLine(message,CLI.RED);
    }

}
