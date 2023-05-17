import controller.StandardServerController;
import distibuted.socket.middleware.ServerSocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server{
    private static StandardServerController serverController;

    protected Server() throws RemoteException {
        serverController = new StandardServerController();
    }
    public static void main(String[] args) throws RemoteException {
        new Server().run();
    }

    public void run(){
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI();
            } catch (RemoteException e) {
                System.err.println("Cannot start RMI. Trying to start RMI on port 1099...");
                try {
                    LocateRegistry.createRegistry(1099);
                    //start RMI
                    startRMI();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.err.println("Cannot start RMI. This protocol will be disabled.");
                }
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
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", serverController);
        System.out.println("RUNNING RMI");
    }

    public void startSocket() throws RemoteException {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("RUNNING SOCKET");
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
        }
    }
}
