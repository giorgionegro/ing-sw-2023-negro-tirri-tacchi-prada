package controller;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import model.exceptions.GameNotExistsException;
import modelView.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("BlockingMethodInNonBlockingContext")
class StandardServerControllerTest {


    /**
     * Method under test: {@link StandardServerController#connect(ClientInterface)}
     */
    @Test
    void testConnect() throws RemoteException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
                throw new RemoteException("This method must be called.");
            }

            @Override
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

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


        assertThrows(AssertionError.class, () -> standardServerController.connect(client));
        //test detaching observer

        var client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
                throw new RemoteException("This method must be called.");

            }

            @Override
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

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
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
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
        standardServerController.disconnect(client);

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
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
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

        assertDoesNotThrow(()->standardServerController.joinGame(client, info));
        assertDoesNotThrow(()->standardServerController.joinGame(client, info));

    }


    /**
     * Method under test: {@link StandardServerController#joinGame(ClientInterface, LoginInfo)}
     * trying to join a game that does not exist
     */
    @Test
    void testJoinNonExistingGame() throws  RemoteException{
        StandardServerController standardServerController = new StandardServerController();
        final UserInfo[] user = {new UserInfo(User.Status.JOINED, "test", 0)};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
            }

            @Override
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
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
                user[0] =o;

            }
        };
        standardServerController.connect(client);
        standardServerController.joinGame(client, new LoginInfo("1", "test", 2));
        assertEquals(User.Status.NOT_JOINED, user[0].status());
    }


    /**
     * Method under test: {@link StandardServerController#leaveGame(ClientInterface)}
     */
    @Test
    void testLeaveGame() throws RemoteException, InterruptedException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();

        UserInfo[] userInfos = new UserInfo[]{null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

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
                userInfos[0]=o;
            }
        };

        // Act
        standardServerController.connect(client);
        // Assert
        standardServerController.leaveGame(client);
        Thread.sleep(100);
        assert userInfos[0]!=null && userInfos[0].status() == User.Status.NOT_JOINED && userInfos[0].eventMessage().equals("Client is not connected to any game");
    }

    /**
     * Method under test: {@link StandardServerController#createGame(ClientInterface, NewGameInfo)}
     */
    @Test
    void testCreateGame() throws RemoteException, InterruptedException {
        // Arrange
        StandardServerController standardServerController = new StandardServerController();

        final UserInfo[] userInfo = {null};

        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
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
                userInfo[0] = o;
            }
        };

        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        //wait 100ms
        Thread.sleep(100);
        assert userInfo[0] == null;
        standardServerController.connect(client);
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        //wait 100ms
        Thread.sleep(100);
        assert userInfo[0] != null;
        assert userInfo[0].eventMessage().equals("Game created");



        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        //wait 100ms
        Thread.sleep(100);
        assert !userInfo[0].eventMessage().equals("Game created");


        // Assert
    }


    /**
     * Method under test: {@link StandardServerController#getGame(String)}
     */
    @Test
    void testGetGame() throws RemoteException {

        StandardServerController standardServerController = new StandardServerController();
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {
            }

            @Override
            public void ping() throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
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
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));
        standardServerController.createGame(client, new NewGameInfo("test", "STANDARD", 2, 2));


        assertDoesNotThrow(() -> standardServerController.getGame("test"));
        assertThrows(GameNotExistsException.class, () -> standardServerController.getGame("test2"));

    }


}

