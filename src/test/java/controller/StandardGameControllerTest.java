package controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import controller.exceptions.GameAccessDeniedException;
import distibuted.ClientEndPoint;
import distibuted.interfaces.ClientInterface;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import distibuted.interfaces.ServerInterface;
import distibuted.socket.middleware.ServerSocketHandler;
import model.StandardGame;
import model.Tile;
import model.User;
import model.abstractModel.*;
import model.exceptions.PlayerNotExistsException;
import modelView.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import view.CLI;

class StandardGameControllerTest {


    /**
     * Method under test: {@link StandardGameController#joinPlayer(ClientInterface, User, LoginInfo)}
     */
    @Test
    void testJoinPlayer9() throws GameAccessDeniedException {
        assertThrows(IllegalArgumentException.class,
                () -> (new StandardGameController(new StandardGame("42", 2))).joinPlayer(null, null, null));
    }

    @Test
    void joinPlayer() throws GameAccessDeniedException, RemoteException {
        Game game = new StandardGame("42", 2);
        StandardGameController standardGameController = new StandardGameController(game);

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1","42",1);
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
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }
        catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }

    }


     // tesat join completing the lobby

    /**
     *  Method under test: {@link StandardGameController#joinPlayer(ClientInterface, User, LoginInfo)}
     */
    @Test
    void joinPlayerCompleteLobby()
    {
        Game game = new StandardGame("42", 2);
        StandardGameController standardGameController = new StandardGameController(game);

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1","42",1);
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
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //join the second player
        user = new User();
        loginInfo = new LoginInfo("2","42",1);
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }
        catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }


    }

    //test join with a full lobby, leaving the game and trying to join again
    /**
     *  Methods under test:
     *
     *  <ul>
     *      <li>{@link StandardGameController#joinPlayer(ClientInterface, User, LoginInfo)}</li>
     *      <li>{@link StandardGameController#leavePlayer(ClientInterface)}</li>
     *  </ul>
     */
    @Test
    void rejoinEndedGame()
    {
        Game game = new StandardGame("42", 2);
        StandardGameController standardGameController = new StandardGameController(game);

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1","42",1);
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
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //join the second player
        user = new User();
        loginInfo = new LoginInfo("2","42",1);
        client = new ClientInterface() {
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

        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }
        catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //leave the game
        try {
            standardGameController.leavePlayer(client);
        }
        catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //try to join again

            ClientInterface finalClient = client;
            User finalUser = user;
            LoginInfo finalLoginInfo = loginInfo;
            assertThrows(GameAccessDeniedException.class,()-> standardGameController.joinPlayer(finalClient, finalUser, finalLoginInfo));



    }


    //join game with same id
    /**
     *  Method under test: {@link StandardGameController#joinPlayer(ClientInterface, User, LoginInfo)}
     */

    @Test
    void joinWithExistingId()
    {

        Game game = new StandardGame("42", 2);
        StandardGameController standardGameController = new StandardGameController(game);

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1","42",1);
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
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //join the second player
        user = new User();
        loginInfo = new LoginInfo("1","42",1);

        User finalUser = user;
        LoginInfo finalLoginInfo = loginInfo;
        assertThrows(GameAccessDeniedException.class, ()->{

           standardGameController.joinPlayer(client, finalUser, finalLoginInfo);
       }) ;





    }

    //leave with non existing client
    /**
     *  Method under test: {@link StandardGameController#leavePlayer(ClientInterface)}
     */
    @Test
    void leaveWithNonExistingClient()
    {

        Game game = new StandardGame("42", 2);
        StandardGameController standardGameController = new StandardGameController(game);

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1","42",1);
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
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //join the second player
        user = new User();
        loginInfo = new LoginInfo("1","42",1);

        User finalUser = user;
        LoginInfo finalLoginInfo = loginInfo;
        client = new ClientInterface() {
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
        ClientInterface finalClient = client;
        assertThrows(GameAccessDeniedException.class, ()->{

            standardGameController.leavePlayer(finalClient);
        }) ;

    }
    //test observers throwing exceptions in joinPlayer
    /**
     *  Methods under test:
     *  {@link StandardGameController#joinPlayer(ClientInterface, User, LoginInfo)}
     */
    @Test
    void observersErrorTest()
    {
        Game game = new StandardGame("42", 2);
        StandardGameController standardGameController = new StandardGameController(game);

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1","42",1);
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
        try {
            standardGameController.joinPlayer(client, user, loginInfo);
        }catch (Exception e){
            fail("Exception thrown" + e.getMessage());
        }
        //join the second player
        user = new User();
        loginInfo = new LoginInfo("2","42",1);

        User finalUser = user;
        LoginInfo finalLoginInfo = loginInfo;
        client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                throw new RemoteException();

            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {
                throw new RemoteException();

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                throw new RemoteException();

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
                throw new RemoteException();

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
                throw new RemoteException();

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                throw new RemoteException();

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
                throw new RemoteException();
            }
        };
        ClientInterface finalClient = client;
        try {
            standardGameController.joinPlayer(finalClient, finalUser, finalLoginInfo);
        }
        catch (Exception e)
        {
            fail("Exception thrown" + e.getMessage());
        }
    }



    @Test
    void leavePlayer() {
    }


    //test doPlayerMove
    /**
     *  Methods under test:
     *  {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo )}
     */
    @Test
    void doPlayerMove() throws PlayerNotExistsException {
        var game = new StandardGame("42", 2);
        var standardGameController = new StandardGameController(game);
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1,1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles,1);
            standardGameController.doPlayerMove(new ClientInterface() {
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
            }, playerMoveInfo);

            var client = new ClientInterface(){

                @Override
                public void update(UserInfo o, User.Event evt) throws RemoteException {

                }

                @Override
                public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

                }

                @Override
                public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

                }

                @Override
                public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

                }

                @Override
                public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

                }

                @Override
                public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

                }

                @Override
                public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

                }

                @Override
                public void update(GameInfo o, Game.Event evt) throws RemoteException {

                }

                @Override
                public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

                }

                @Override
                public void bind(ServerInterface server) throws RemoteException {

                }
            };
        try {
            standardGameController.joinPlayer(client, new User(), new LoginInfo("1", "42", 1));
        } catch (GameAccessDeniedException e) {
            fail("Exception thrown" + e.getMessage());
        }
        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        var client2 = new ClientInterface(){
            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {

            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {

            }

            @Override
            public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }
        };
        try {
            standardGameController.joinPlayer(client2, new User(), new LoginInfo("2", "42", 1));
        } catch (GameAccessDeniedException e) {
            fail("Exception thrown" + e.getMessage());
        }
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //with the first player we test a move after the game starts
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we will do some legal moves like picking 4,1
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(4,1));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //with the second player we test a move after the game starts
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //malformed move
        pickedTiles = new ArrayList<>();
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);

        //column out of bounds
        pickedTiles.add(new PickedTile(4,1));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,-1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);

        //tile are not different
        pickedTiles.add(new PickedTile(4,1));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        Tile[][] tiles = new Tile[6][5];
        //fill with Tile.EMPTY
        Arrays.stream(tiles).forEach(a -> Arrays.fill(a, Tile.TROPHIES_1));
        try {
            game.getPlayer("2").getShelf().setTiles( tiles);
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException(e);
        }
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1,3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1,4));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        game = new StandardGame("32",2);
        standardGameController = new StandardGameController(game);
        try {
            standardGameController.joinPlayer(client, new User(), new LoginInfo("1", "32", 1));
        } catch (GameAccessDeniedException e) {
            fail("Exception thrown" + e.getMessage());
        }
        try {
            standardGameController.joinPlayer(client2, new User(), new LoginInfo("2", "32", 1));
        } catch (GameAccessDeniedException e) {
            fail("Exception thrown" + e.getMessage());
        }
        tiles[0][0] = Tile.EMPTY;

        game.getPlayer("1").getShelf().setTiles( tiles);
        game.getPlayer("2").getShelf().setTiles( tiles);
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1,3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles,0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);

    }

    @Test
    void sendMessage() {
    }
}

