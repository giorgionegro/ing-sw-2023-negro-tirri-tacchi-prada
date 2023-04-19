package distibuted;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import modelView.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientEndPoint extends UnicastRemoteObject implements ClientInterface{

    public ClientEndPoint(ServerInterface server) throws RemoteException {
        super();
    }
    @Override
    public synchronized void updateGameStatus(GameView o, Object arg) {
        System.out.println("GameView:: "+o.getStatus()+" "+o.getPlayerOnTurn()+" "+o.isLastTurn());
    }

    @Override
    public synchronized void updateCommonGoal(CommonGoalView o, Object arg) {
        System.out.println("CommonGoalView:: "+o.getDescription()+" "+o.getTokenState());
    }

    @Override
    public synchronized void updateShelf(ShelfView o, Object arg) {
        System.out.println("ShelfView:: "+o.getPlayerId());
    }

    @Override
    public synchronized void updateLivingRoom(LivingRoomView o, Object arg) {
        System.out.println("LivingRoomView:: ");
    }

    @Override
    public synchronized void updatePlayerChat(PlayerChatView o, Object arg) {
        System.out.println("PlayerChatView:: "+o.getMessages().size()+" messages");
    }

    @Override
    public synchronized void updatePersonalGoal(PersonalGoalView  o, Object arg) {
        System.out.println("PersonalGoalView:: "+o.isAchieved());
    }

    @Override
    public synchronized void signalError(String error) throws RemoteException {
        System.out.println(error);
    }
}
