import controller.StandardServerController;
import distibuted.socket.middleware.ServerSocketHandler;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class Server{
    private static final int RMIport = Registry.REGISTRY_PORT;
    private static final String RMIip = "localhost";
    private static final int SOCKETport = 10101;

    private static final Map<String,String> parameters = new HashMap<>(){{
        put("-ip", RMIip);
        put("-rP",String.valueOf(RMIport));
        put("-sP",String.valueOf(SOCKETport));
    }};

    public static void main(String[] args) throws RemoteException {
        if(args.length!=0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        parameters.put(args[i], args[i + 1]);
                        i++;
                    }else {
                        parameters.put(args[i], args[i]);
                    }
                } else {
                    System.err.println("Parameter \"" + args[i] + "\" has no tag");
                    return;
                }
            }
        }
        if(parameters.containsKey("-h")){
            System.out.println("-ip -> specifies Ip used by RMI to export RemoteObjects\n\t(Default = localhost found by InetAddress)\n" +
                    "-rP -> specifies Port used by RMI to export Registry\n\t(Default = "+RMIport+")\n"+
                    "-sP -> specifies Port used by Socket to accept incoming connections\n\t(Default = "+SOCKETport+")\n"+
                    "-h -> show this message"
            );
            return;
        }

        System.setProperty("java.rmi.server.hostname",parameters.get("-ip"));
        new Server().run();
    }

    private final StandardServerController serverController;

    protected Server() throws RemoteException {
        serverController = new StandardServerController();
    }


    public void run(){
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI();
            } catch (RemoteException e) {
                System.err.println("Cannot start RMI. This protocol will be disabled.");
            }
        });

        rmiThread.start();

        Thread socketThread = new Thread(() -> {
            try {
                startSocket();
            } catch (RemoteException e) {
                System.err.println("Cannot start socket. This protocol will be disabled.");
            }
        });

        socketThread.start();

        try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }

    private void startRMI() throws RemoteException {
        Registry registry;
        try{
            registry = LocateRegistry.createRegistry(Integer.parseInt(parameters.get("-rP")));
        }catch(NumberFormatException e){
            System.err.println("Wrong -rP parameter: "+parameters.get("-rP")+" is not a number");
            throw new RemoteException();
        }

        registry.rebind("server", serverController);
        System.out.println("RUNNING RMI on ip: "+System.getProperty("java.rmi.server.hostname")+":"+parameters.get("-rP"));
    }

    public void startSocket() throws RemoteException {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(parameters.get("-sP")))) {
            System.out.println("RUNNING SOCKET on ip: "+serverSocket.getInetAddress().getHostAddress()+":"+parameters.get("-sP"));
            while (true) {
                Socket socket = serverSocket.accept();

                ServerSocketHandler serverSocketHandler = new ServerSocketHandler(socket);
                try {
                    serverSocketHandler.open();
                    serverController.connect(serverSocketHandler);
                } catch (RemoteException e) {
                    System.err.println("Cannot receive from client: "+e.getMessage()+".\n-> Closing this connection...");
                }

            }
        } catch (IOException e) {
            throw new RemoteException("Cannot start socket server", e);
        } catch (NumberFormatException e){
            System.err.println("Wrong -sP parameter: "+parameters.get("-sP")+" is not a number");
        }
    }
}
