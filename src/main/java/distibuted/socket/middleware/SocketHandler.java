package distibuted.socket.middleware;

import distibuted.socket.middleware.interfaces.SocketObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;

public abstract class SocketHandler<Interface> {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    private String ip = null;
    private int port = 0;

    private Socket socket = null;

    public SocketHandler(String ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
    }

    public SocketHandler(Socket socket) {
        super();
        this.socket = socket;
    }

    public void open() throws RemoteException {
        try {
            if (this.socket == null) {
                this.socket = new Socket(this.ip, this.port);
            }

            if (!this.socket.isConnected()) {
                //Could ip be null?
                this.socket.connect(new InetSocketAddress(this.ip, this.port), 1000);
            }

            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Unable to open the socket connection");
        }
    }

    public void close() throws RemoteException {
        try {
            if (this.socket != null) {
                this.socket.close();
            } else {
                throw new IOException("Socket is non been initialized");
            }
        } catch (IOException e) {
            throw new RemoteException("Unable to close the socket connection");
        }
    }

    protected void waitForReceive(Interface receiver) throws RemoteException {
        try {
            SocketObject no = (SocketObject) this.ois.readObject();
            no.update(this, receiver);
        } catch (IOException e) {
            throw new RemoteException("Unable to communicate with socket");
        } catch (ClassNotFoundException e) {
            System.err.println("Received not a SocketObject");
        }
    }

    protected synchronized void send(SocketObject o) throws IOException {
        this.oos.writeObject(o);
        this.oos.flush();
        this.oos.reset();
    }

}
