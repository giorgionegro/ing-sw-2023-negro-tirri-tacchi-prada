package distibuted.interfaces;

import modelView.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void updateGameStatus(GameView o, Object arg) throws RemoteException;
    void updateCommonGoal(CommonGoalView o, Object arg) throws RemoteException;
    void updateShelf(ShelfView o, Object arg) throws RemoteException;
    void updateLivingRoom(LivingRoomView o, Object arg) throws RemoteException;
    void updatePlayerChat(PlayerChatView o, Object arg) throws RemoteException;
    void updatePersonalGoal(PersonalGoalView o, Object arg) throws RemoteException;
    void signalError(String error) throws RemoteException;
}
