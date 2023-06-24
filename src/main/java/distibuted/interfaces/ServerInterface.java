package distibuted.interfaces;

import controller.interfaces.GameController;
import controller.interfaces.ServerController;
import java.rmi.Remote;

/**
 * This interface is a collection of interfaces that contains all the methods a client can call on server
 */
public interface ServerInterface extends Remote, ServerController, GameController{}