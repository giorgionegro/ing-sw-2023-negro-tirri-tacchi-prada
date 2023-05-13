package distibuted.socket.middleware;

import distibuted.socket.middleware.interfaces.SocketObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;

public abstract class SocketHandler<Interface>{
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private String ip = null;
    private int port = 0;
    private Socket socket = null;

    public SocketHandler(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public SocketHandler(Socket socket){
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

    public void waitForReceive(Interface receiver){
        try {
            SocketObject no = (SocketObject) ois.readObject();
            no.update(this, receiver);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);//TODO send error to client
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);//TODO send error to client
        }
    }

    protected synchronized void send(SocketObject o) throws IOException {
        oos.writeObject(o);
        oos.flush();
        oos.reset();
    }

}
