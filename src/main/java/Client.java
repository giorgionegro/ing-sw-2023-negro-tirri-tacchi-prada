import view.GUI.GUI;
import view.TUI.TUI;
import view.ViewLogic;
import view.graphicInterfaces.AppGraphics;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import static util.Parser.argumentsParser;

/**
 * This class is the entry point of the client
 */
public class Client {
    /**
     * Private constructor to hide the implicit public one
     */
    private Client() {
    }

    /**
     * UI type
     */
    private static final String UI_type = "TUI";
    /**
     * Default host ip
     */
    private static final String DEFAULT_hostIp = "localhost";
    /**
     * Default client ip
     */
    private static final String DEFAULT_clientIp = "localhost";
    /**
     * Default RMI host port
     */
    private static final int DEFAULT_RMI_hostPort = Registry.REGISTRY_PORT;
    /**
     * Default SOCKET host port
     */
    private static final int DEFAULT_SOCKET_hostPort = 10101;
    /**
     * This map contains the parameters passed that can be optionally specified
     */
    private static final Map<String, String> parameters = new HashMap<>() {{
        this.put("-ui", UI_type);
        this.put("-hip", DEFAULT_hostIp);
        this.put("-cip", DEFAULT_clientIp);
        this.put("-rP", String.valueOf(DEFAULT_RMI_hostPort));
        this.put("-sP", String.valueOf(DEFAULT_SOCKET_hostPort));
    }};

    /**
     * This method is the entry point of the client, it reads the parameters and starts the client
     *
     * @param args command line arguments
     * @throws RemoteException if there are connection problems
     */
    public static void main(String[] args) throws RemoteException {
        //parse arguments
        if (argumentsParser(args, parameters)) return;

        //show help if requested
        if (parameters.containsKey("-h")) {
            System.out.println("-ui -> specifies UI type:\n" +
                    "\t(Default = " + UI_type + ")\n" +
                    "\t\tTUI for Textual User Interface\n" +
                    "\t\tGUI for Graphical User Interface\n" +
                    "-hip -> specifies host Ip\n\t(Default = " + DEFAULT_hostIp + ")\n" +
                    "-cip -> specifies client RMI Ip\n\t(Default = " + DEFAULT_clientIp + ")\n" +
                    "-rP -> specifies host RMI port\n\t(Default = " + DEFAULT_RMI_hostPort + ")\n" +
                    "-rP -> specifies host SOCKET port\n\t(Default = " + DEFAULT_SOCKET_hostPort + ")\n" +
                    "-h -> show this message");
            return;
        }

        int RMIhostPort;
        int SockethostPort;
        //check if the arguments are valid
        try {
            RMIhostPort = Integer.parseInt(parameters.get("-rP"));
        } catch (NumberFormatException e) {
            System.err.println("Host RMI port is not a number");
            return;
        }
        try {
            SockethostPort = Integer.parseInt(parameters.get("-sP"));
        } catch (NumberFormatException e) {
            System.err.println("Host RMI port is not a number");
            return;
        }

        AppGraphics appGraphics;
        //check if the arguments are valid and create the UI accordingly
        switch (parameters.get("-ui")) {
            case "GUI" -> appGraphics = new GUI();
            case "TUI" -> appGraphics = new TUI();
            default -> {
                System.err.println("Parameter -ui has unknown value: " + parameters.get("-ui"));
                return;
            }
        }
        //set the client ip for RMI
        System.setProperty("java.rmi.server.hostname", parameters.get("-cip"));
        //start the client
        new ViewLogic(appGraphics, parameters.get("-hip"), RMIhostPort, SockethostPort).start();
    }


}
