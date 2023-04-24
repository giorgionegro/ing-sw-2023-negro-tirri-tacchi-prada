package distibuted.interfaces;

import controller.interfaces.GameController;
import controller.interfaces.ServerController;
import java.rmi.Remote;

public interface ServerInterface extends Remote, ServerController, GameController{}