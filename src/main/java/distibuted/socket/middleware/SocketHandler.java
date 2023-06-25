package distibuted.socket.middleware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * This class is used to manage socket connections
 * <p>
 * This class only send and receives SocketObjects,
 * when it receives a {@link SocketObject} it tries to act as the sender of the interaction
 * @param <ReceiverType> type of the interaction-receiver object
 */
public abstract class SocketHandler<ReceiverType> {
    /**
     * The socket output stream
     */
    private ObjectOutputStream oos;

    /**
     * The socket input stream
     */
    private ObjectInputStream ois;

    /**
     * Ip address of the socket
     */
    private String ip = null;

    /**
     * Port of the socket
     */
    private int port = 0;

    /**
     * The socket this class manages
     */
    private Socket socket = null;

    /**
     * Class constructor of socket
     * @param ip the ip address to be assigned to the socket
     * @param port the port to be assigned to the socket
     */
    public SocketHandler(String ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
    }

    /**
     * Class constructor of socket
     * @param socket an already existing socket
     */
    public SocketHandler(Socket socket) {
        super();
        this.socket = socket;
    }

    /**
     * This method opens a socket connection
     * @throws RemoteException if the connection fails to open
     */
    public void open() throws RemoteException {
        try {
            /* If there is no currently managed socket then creates a new one */
            if (this.socket == null) {
                this.socket = new Socket(this.ip, this.port);
            }

            /* If managed socket is not connected then open a new connection */
            if (!this.socket.isConnected()) {
                this.socket.connect(new InetSocketAddress(this.ip, this.port), 1000);
            }

            /* Creates a new input and output stream on connected socket */
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Unable to open the socket connection");
        }
    }

    /**
     * This method closes a socket connection
     * @throws RemoteException if the connection fails to close
     */
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

    /**
     * This method waits for a SocketObject sent on the input stream and call interaction on it, acting as the sender
     * @param receiver the {@link SocketObject} receiver object
     * @throws RemoteException when an input/output exception occurs
     */
    protected void waitForReceive(ReceiverType receiver) throws RemoteException {
        try {
            /* Wait to receive a SocketObject on input stream */
            SocketObject so = (SocketObject) this.ois.readObject();
            /* Call interaction on SocketObject */
            so.interact(this, receiver);
        } catch (IOException e) {
            throw new RemoteException("Unable to communicate with socket");
        } catch (ClassNotFoundException e) {
            System.err.println("Received not a SocketObject");
        }
    }

    /**
     * This method sends a SocketObject on the output stream
     * @param o SocketObject to send
     * @throws IOException when an input/output error occurs
     */
    protected synchronized void send(SocketObject o) throws IOException {
        this.oos.writeObject(o);
        this.oos.flush();
        this.oos.reset();
    }

}
