package view.interfaces;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;

import java.rmi.Remote;

public interface UI extends Remote, ViewCollection{
    String askRMIorSocket();
    void showError(String error);
    void run(ServerInterface server, ClientInterface client);
}
