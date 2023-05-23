package distibuted.socket.middleware;

import distibuted.socket.middleware.interfaces.SocketObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;

public abstract class SocketHandler<Interface>{
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private @Nullable String ip = null;
    private int port = 0;
    private @Nullable Socket socket = null;

    public SocketHandler(@NotNull String   ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public SocketHandler( @NotNull Socket socket){
        this.socket = socket;
    }

    public void open() throws RemoteException{
        try {
            if (socket == null) {
                socket = new Socket(ip, port);
            }

            if(!socket.isConnected())
                socket.connect(new InetSocketAddress(ip,port),1000);

            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Unable to open the socket connection");
        }
    }

    public void close() throws RemoteException{
        try {
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Unable to close the socket connection");
        }
    }

    protected void waitForReceive(Interface receiver) throws RemoteException {
        try {
            SocketObject no = (SocketObject) ois.readObject();
            no.update(this, receiver);
        } catch (IOException e) {
            throw new RemoteException("Unable to communicate with socket");
        } catch (ClassNotFoundException e) {
            System.err.println("Received not a SocketObject");
        }
    }

    protected synchronized void send(SocketObject o) throws IOException {
        oos.writeObject(o);
        oos.flush();
        oos.reset();
    }

}
