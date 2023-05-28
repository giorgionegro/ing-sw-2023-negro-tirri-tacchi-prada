package controller;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import model.exceptions.GameNotExistsException;
import modelView.*;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardServerControllerTest {


    /**
     * Method under test: {@link StandardServerController#connect(ClientInterface)}
     */
    @Test
    void testConnect() throws RemoteException {
        // Arrange
        // TODO: Populate arranged inputs
        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
                throw new RemoteException("This method must be called.");
            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
                throw new AssertionError("This method must be called.");

            }
        };


        assertThrows(AssertionError.class, () -> {
            ServerInterface actualConnectResult = standardServerController.connect(client);
        });
        //test detaching observer

        var client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
                throw new RemoteException("This method must be called.");
            }
        };
        standardServerController.connect(client2);


    }

    /**
     * Method under test: {@link StandardServerController#disconnect(ClientInterface)}
     */
    @Test
    void testDisconnect() throws RemoteException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {

            }
        };


        standardServerController.connect(client);
        assertThrows(RemoteException.class, () -> standardServerController.disconnect(client));

        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        standardServerController.joinGame(client, new LoginInfo("1", "test", 2));
        standardServerController.disconnect(client);

        // Assert

    }

    /**
     * Method under test: {@link StandardServerController#joinGame(ClientInterface, LoginInfo)}
     */
    @Test
    void testJoinGame() throws RemoteException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {

            }
        };
        LoginInfo info = new LoginInfo("1", "test", 2);

        standardServerController.joinGame(client, info);
        standardServerController.connect(client);

        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));

        standardServerController.joinGame(client, info);
        standardServerController.joinGame(client, info);

    }

    /**
     * Method under test: {@link StandardServerController#leaveGame(ClientInterface)}
     */
    @Test
    void testLeaveGame() throws RemoteException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {

            }
        };

        // Act
        standardServerController.connect(client);
        // Assert
        assertThrows(RemoteException.class, () -> standardServerController.leaveGame(client));

    }

    /**
     * Method under test: {@link StandardServerController#createGame(ClientInterface, NewGameInfo)}
     */
    @Test
    void testCreateGame() throws RemoteException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {

            }
        };
        LoginInfo info = new LoginInfo("1", "test", 2);
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        standardServerController.connect(client);
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));


        // Assert
    }


    /**
     * Method under test: {@link StandardServerController#getGame(String)}
     */
    @Test
    void testGetGame() throws GameNotExistsException, RemoteException {

        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {

            }
        };
        LoginInfo info = new LoginInfo("1", "test", 2);
        standardServerController.connect(client);
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));


        assertDoesNotThrow(() -> standardServerController.getGame("test"));
        assertThrows(GameNotExistsException.class, () -> standardServerController.getGame("test2"));

    }


}

