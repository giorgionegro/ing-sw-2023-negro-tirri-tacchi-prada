import distibuted.ClientEndPoint;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ClientSocketHandler;
import view.GUI.GUI;
import view.TUI.TUI;
import view.interfaces.UI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private static final String UItype = "TUI";
    private static final String IPhost = "localhost";
    private static final int RMIhostPORT = Registry.REGISTRY_PORT;
    private static final int SOCKEThostPORT = 10101;

    private static final Map<String, String> parameters = new HashMap<>() {{
        put("-ui", UItype);
        put("-ip", IPhost);
        put("-rP", String.valueOf(RMIhostPORT));
        put("-sP", String.valueOf(SOCKEThostPORT));
    }};
    private final UI ui;
    private AppServer server;
    private ServerInterface serverEndpoint;
    private Client(UI ui) {
        this.ui = ui;
        server = null;
        serverEndpoint = null;
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        parameters.put(args[i], args[i + 1]);
                        i++;
                    } else {
                        parameters.put(args[i], args[i]);
                    }
                } else {
                    System.err.println("Parameter \"" + args[i] + "\" has no tag");
                    return;
                }
            }
        }

        if (parameters.containsKey("-h")) {
            System.out.println("-ui -> specifies UI type:\n" +
                    "\t(Default = " + UItype + ")\n" +
                    "\t\tTUI for Textual User Interface\n" +
                    "\t\tGUI for Graphical User Interface\n" +
                    "-ip -> specifies host Ip\n\t(Default = " + IPhost + ")\n" +
                    "-rP -> specifies host RMI port\n\t(Default = " + RMIhostPORT + ")\n" +
                    "-rP -> specifies host SOCKET port\n\t(Default = " + SOCKEThostPORT + ")\n" +
                    "-h -> show this message");
            return;
        }

        switch (parameters.get("-ui")) {
            case "GUI" -> new Client(new GUI()).run();
            case "TUI" -> new Client(new TUI()).run();
            default -> System.err.println("Parameter -ui has unknown value: " + parameters.get("-ui"));
        }

        System.exit(0);
    }

    private void run() {
        try {
            ClientEndPoint clientEndPoint = new ClientEndPoint(ui);

            //while server or serverEndpoint are not constructed repeat
            while (server == null || serverEndpoint == null) {
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
        server = (AppServer) LocateRegistry.getRegistry(parameters.get("-ip"), Integer.parseInt(parameters.get("-rP"))).lookup("server");
        serverEndpoint = server.connect(client);
    }

    private void connectSocket(ClientInterface client) throws RemoteException {
        server = new ClientSocketHandler(parameters.get("-ip"), Integer.parseInt(parameters.get("-sP")));
        serverEndpoint = server.connect(client);
    }
}
