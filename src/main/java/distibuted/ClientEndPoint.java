package distibuted;

import distibuted.interfaces.ClientInterface;
import model.abstractModel.*;
import modelView.*;
import view.CLI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientEndPoint extends UnicastRemoteObject implements ClientInterface {

    CLI cli;
    public ClientEndPoint(CLI cli) throws RemoteException {
        super();
        this.cli = cli;
    }

    @Override
    public void update(GamesManagerInfo o, GamesManager.GamesManagerEvent evt) {

    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.LivingRoomEvent evt) {
        cli.updateLivingRoom(o);
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.PersonalGoalEvent evt) {
        System.out.println("PersonalGoalView:: "+o.isAchieved());
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.PlayerChatEvent evt) {
        cli.updatePlayerChat(o);
    }

    @Override
    public void update(PlayerInfo o, Player.PlayerEvent evt) {
        System.out.println(o.getMessage());
    }

    @Override
    public void update(ShelfInfo o, Shelf.ShelfEvent evt) {
        cli.updateShelf(o);
    }

    @Override
    public void update(GameInfo o, Game.GameEvent evt) {
        cli.updateGameState(o);
        //if(evt!=null && evt.equals(Game.GameEvent.NEXT_TURN))
          //  cli.updateCLI();
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.CommonGoalEvent evt) {
        cli.updateCommonGoal(o);
        System.out.println("CommonGoalView:: "+o.getDescription()+" "+o.getTokenState());
    }
}
