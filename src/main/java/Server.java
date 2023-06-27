import controller.StandardServerController;
import distibuted.socket.middleware.ServerSocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import static util.Parser.argumentsParser;

public class Server {


    /**
     * Default Rmi port used by the server
     */
    private static final int RMIport = Registry.REGISTRY_PORT;
    /**
     * Default Rmi ip used by the server
     */
    private static final String RMIip = "localhost";
    /**
     * Default Socket port used by the server
     */
    private static final int SOCKETport = 10101;

    /**
     * This map contains the parameters passed that can be optionally specified
     */
    private static final Map<String, String> parameters = new HashMap<>() {{
        this.put("-ip", RMIip);
        this.put("-rP", String.valueOf(RMIport));
        this.put("-sP", String.valueOf(SOCKETport));
    }};
    /**
     * This is the server controller
     */
    private final StandardServerController serverController;

    /**
     * Constructor of the server object
     * @throws RemoteException if there are connection problems
     */
    protected Server() throws RemoteException {
        this.serverController = new StandardServerController();
    }

    /**
     * This method parse the arguments passed to the server and create a Server object
     * @param args command line arguments
     * @throws RemoteException if there are connection problems
     */
    public static void main(String[] args) throws RemoteException {
        //parse arguments
        if (argumentsParser(args, parameters)) return;
        //show help if requested
        if (parameters.containsKey("-h")) {
            System.out.println("-ip -> specifies Ip used by RMI to export RemoteObjects\n\t(Default = localhost found by InetAddress)\n" +
                    "-rP -> specifies Port used by RMI to export Registry\n\t(Default = " + RMIport + ")\n" +
                    "-sP -> specifies Port used by Socket to accept incoming connections\n\t(Default = " + SOCKETport + ")\n" +
                    "-h -> show this message"
            );
            return;
        }
        //set ip to export remote objects
        System.setProperty("java.rmi.server.hostname", parameters.get("-ip"));
        //disable http protocol
        System.setProperty("java.rmi.server.disableHttp", "true");
        new Server().start();
    }

    /**
     * This method starts the server
     */
    public void start() {
        Thread rmiThread = new Thread(() -> {
            try {
                this.startRMI();
            } catch (RemoteException e) {
                System.err.println("Cannot start RMI. This protocol will be disabled.");
            }
        });
        //start rmi thread
        rmiThread.start();

        Thread socketThread = new Thread(() -> {
            try {
                this.startSocket();
            } catch (RemoteException e) {
                System.err.println("Cannot start socket. This protocol will be disabled.");
            }
        });
        //start socket thread
        socketThread.start();

        try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }

    /**
     * This method starts the RMI protocol
     *
     * @throws RemoteException if there are connection problems
     */
    private void startRMI() throws RemoteException {
        Registry registry;
        try {
            //create registry
            registry = LocateRegistry.createRegistry(Integer.parseInt(parameters.get("-rP")));
        } catch (NumberFormatException e) {
            System.err.println("Wrong -rP parameter: " + parameters.get("-rP") + " is not a number");
            throw new RemoteException();
        }
        //bind server controller to registry
        registry.rebind("server", this.serverController);
        System.out.println("RUNNING RMI on ip: " + System.getProperty("java.rmi.server.hostname") + ":" + parameters.get("-rP"));
    }

    /**
     * This method starts the Socket protocol
     *
     * @throws RemoteException if there are connection problems
     */
    public void startSocket() throws RemoteException {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(parameters.get("-sP")))) {
            System.out.println("RUNNING SOCKET on ip: " + serverSocket.getInetAddress().getHostAddress() + ":" + parameters.get("-sP"));
            while (true) {
                Socket socket = serverSocket.accept();

                ServerSocketHandler serverSocketHandler = new ServerSocketHandler(socket);
                try {
                    //when a new connection is accepted, create a new ServerSocketHandler and connect it to the server controller
                    serverController.connect(serverSocketHandler);
                } catch (RemoteException e) {
                    System.err.println("Cannot receive from client: " + e.getMessage() + ".\n-> Closing this connection...");
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot start socket server", e);
        } catch (NumberFormatException e) {
            System.err.println("Wrong -sP parameter: " + parameters.get("-sP") + " is not a number");
        }
    }
}
