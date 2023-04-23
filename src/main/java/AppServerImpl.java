import distibuted.interfaces.AppServer;
import controller.GamesManagerController;
import distibuted.ServerEndpoint;
import distibuted.interfaces.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class AppServerImpl extends UnicastRemoteObject implements AppServer
{
    private static AppServerImpl instance;
    private static GamesManagerController gamesManagerController;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    protected AppServerImpl() throws RemoteException {
        gamesManagerController = new GamesManagerController();
    }

    public static AppServerImpl getInstance() throws RemoteException {
        if (instance == null) {
            instance = new AppServerImpl();
        }
        return instance;
    }

    public static void main(String[] args) {
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI();
            } catch (RemoteException e) {
                System.err.println("Cannot start RMI. Trying to start RMI on port 1099...");
                //try to start RMI "manually" by executing the rmiregistry command on classpath
                try {
                    //find the classpath
                    String classpath = System.getProperty("java.class.path");
                    System.err.println("Classpath: " + classpath);
                    //execute the command in classpath directory
                    Process process = Runtime.getRuntime().exec("rmiregistry", null, new java.io.File(classpath));
                    //wait for the process to end or kill it after 5 seconds
                    process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);

                    //start RMI
                    startRMI();
                } catch (Exception e1) {
                    System.err.println("Cannot start RMI. This protocol will be disabled.");
                }


            }
        });

        rmiThread.start();

        try {
            rmiThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }

    private static void startRMI() throws RemoteException {
        AppServerImpl server = getInstance();
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", server);
        System.out.println("RUNNING");
    }

    @Override
    public ServerInterface connect() throws RemoteException {
        return new ServerEndpoint(gamesManagerController);
    }



}
