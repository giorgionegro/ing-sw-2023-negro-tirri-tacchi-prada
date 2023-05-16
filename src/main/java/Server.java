import controller.StandardServerController;
import distibuted.interfaces.AppServer;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ServerSocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server extends UnicastRemoteObject implements AppServer
{
    private static StandardServerController serverController;

    private static Server instance;

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    protected Server() throws RemoteException {
        serverController = new StandardServerController();
    }

    public static AppServer getInstance() throws RemoteException {
        if (instance == null)
            instance = new Server();

        return instance;
    }

    public static void main(String[] args) {
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

    private static void startRMI() throws RemoteException {
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", getInstance());
        System.out.println("RUNNING RMI");
    }

    public static void startSocket() throws RemoteException {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("RUNNING SOCKET");
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    ServerSocketHandler serverSocketHandler = new ServerSocketHandler(socket);
                    try {
                        serverSocketHandler.open();
                        ServerInterface server = getInstance().connect(serverSocketHandler);
                        while (true) {
                            serverSocketHandler.waitForReceive(server);
                        }
                    } catch (RemoteException e) {
                        System.err.println("Cannot receive from client: "+e.getMessage()+".\n-> Closing this connection...");
                    } finally {
                        serverController.disconnect(serverSocketHandler);
                        try {
                            socket.close();
                        } catch (IOException e) {
                            System.err.println("Cannot close socket");
                        }
                    }
                });
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot start socket server", e);
        }
    }

    @Override
    public ServerInterface connect(ClientInterface client) throws RemoteException {
        return serverController.connect(client);
    }

    @Override
    public void disconnect(ClientInterface client) throws RemoteException {
        serverController.disconnect(client);
    }
}
