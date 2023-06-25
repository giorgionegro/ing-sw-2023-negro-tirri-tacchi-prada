import view.GUI.GUI;
import view.TUI.TUI;
import view.ViewLogic;
import view.graphicInterfaces.AppGraphics;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private Client() {
    }
    private static final String UI_type = "TUI";
    private static final String DEFAULT_hostIp = "localhost";
    private static final String DEFAULT_clientIp = "localhost";
    private static final int DEFAULT_RMI_hostPort = Registry.REGISTRY_PORT;
    private static final int DEFAULT_SOCKET_hostPort = 10101;
    private static final Map<String, String> parameters = new HashMap<>() {{
        this.put("-ui", UI_type);
        this.put("-hip", DEFAULT_hostIp);
        this.put("-cip", DEFAULT_clientIp);
        this.put("-rP", String.valueOf(DEFAULT_RMI_hostPort));
        this.put("-sP", String.valueOf(DEFAULT_SOCKET_hostPort));
    }};

    public static void main(String[] args) throws RemoteException {
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

        switch (parameters.get("-ui")) {
            case "GUI" -> appGraphics = new GUI();
            case "TUI" -> appGraphics = new TUI();
            default -> {
                System.err.println("Parameter -ui has unknown value: " + parameters.get("-ui"));
                return;
            }
        }

        System.setProperty("java.rmi.server.hostname", parameters.get("-cip"));

        new ViewLogic(appGraphics, parameters.get("-hip"), RMIhostPort, SockethostPort).run();
    }
}
