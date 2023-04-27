package distibuted;

import distibuted.interfaces.ClientInterface;
import model.User;
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
    public void update(GamesManagerInfo o, GamesManager.Event evt) {
        cli.updateGamesManager(o,evt);

    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) {
        cli.updateLivingRoom(o);
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) {
        cli.updatePersonalGoal(o);
        System.out.println("PersonalGoalView:: "+o.achieved());

    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) {
        cli.updatePlayerChat(o);
    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) {
        cli.updatePlayerInfo(o,evt);
    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) {
        cli.updateShelf(o);
    }

    @Override
    public void update(GameInfo o, Game.Event evt) {
        cli.updateGameState(o);
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) {
        cli.updateCommonGoal(o);
        System.out.println("CommonGoalView:: "+o.description()+" "+o.tokenState());
    }

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        cli.updateUserInfo(o,evt);
    }
}
