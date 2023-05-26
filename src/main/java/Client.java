import distibuted.ClientEndPoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ClientSocketHandler;
import view.CLI;
import view.GUI.GUI;
import view.interfaces.UI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Client {

    UI ui;
    AppServer server;
    ServerInterface serverEndpoint;

    public static void main(String[] args){
        //TODO check on args
        switch(args[0]){
            case "-GUI" -> new Client(new GUI()).run();
            case "-CLI" -> new Client(new CLI()).run();
        }
        System.exit(0);
    }

    private Client(UI  ui){
        //TODO ask if use gui or cli
        this.ui = ui;
        server = null;
        serverEndpoint = null;
    }

    private void run(){
        try {
            ClientEndPoint clientEndPoint = new ClientEndPoint(ui);

            //while server or serverEndpoint are not constructed repeat
            while(server==null || serverEndpoint==null){
                //ask if to use rmi or socket, empty to exit
                String choise;
                do {
                    choise = ui.askRMIorSocket();
                } while (!(choise.equals("r") || choise.equals("s") || choise.equals("")));

                //if choose exit then return and close application
                if (choise.equals(""))
                    System.exit(0);

                //else try to connect with socket or rmi
                try {
                    switch (choise) {
                        case "r" -> connectRMI(clientEndPoint);
                        case "s" -> connectSocket(clientEndPoint);
                    }
                } catch (RemoteException | NotBoundException e) {
                    server = null;
                    serverEndpoint = null;
                    ui.showError("ERROR WHILE CONNECTING TO SERVER");
                }
            }

            ui.run(serverEndpoint, clientEndPoint);

        } catch (RemoteException e) {
            ui.showError("ERROR WHILE CREATING CLIENT ENDPOINT");
        }
    }

    private void connectRMI(ClientInterface client) throws RemoteException, NotBoundException {
        server = (AppServer) LocateRegistry.getRegistry().lookup("server");
        serverEndpoint = server.connect(client);
    }

    private void connectSocket(ClientInterface client) throws RemoteException {
        server = new ClientSocketHandler("localhost", 1234);
        serverEndpoint = server.connect(client);
    }
}
