/*
public class ProvaGui implements ActionListener {

    public static void main(String[] args){
        GUI gui = new GUI();


        try{
            ClientInterface client = new ClientEndPoint(gui);
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
                            app = new ServerStub("localhost", 1234);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == SocketButton) {

        } else if (e.getSource() == RMIButton) {

        }
    }
}
*/