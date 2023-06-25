package controller;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.GameBuilder;
import model.StandardMessage;
import model.Tile;
import model.User;
import model.abstractModel.*;
import model.exceptions.PlayerNotExistsException;
import modelView.*;
import org.junit.jupiter.api.Test;
import util.TimedLock;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"MismatchedReadAndWriteOfArray", "unchecked"})
class StandardGameControllerTest {


    /**
     * Method under test: {@link StandardGameController#joinPlayer(ClientInterface, User, String)}
     * Testing a normal join
     */
    @Test
    void joinPlayer() {
        Game game = GameBuilder.build(new NewGameInfo("42", "STANDARD", 2, System.currentTimeMillis()));
        StandardGameController standardGameController = new StandardGameController(game, lobbyController -> {
        });

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1", "42", 1);
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

        assertDoesNotThrow(() -> {
            standardGameController.joinPlayer(client, user, loginInfo.playerId());
            Thread.sleep(400);
        });
    }


    // test join completing the lobby

    /**
     * Method under test: {@link StandardGameController#joinPlayer(ClientInterface, User, String)}
     * Testing completing a game lobby
     */
    @Test
    void joinPlayerCompleteLobby() {
        Game game = GameBuilder.build(new NewGameInfo("42", "STANDARD", 2, System.currentTimeMillis()));
        StandardGameController standardGameController = new StandardGameController(game, lobbyController -> {
        });

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1", "42", 1);
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

        User finalUser = user;
        LoginInfo finalLoginInfo = loginInfo;
        assertDoesNotThrow(() -> standardGameController.joinPlayer(client, finalUser, finalLoginInfo.playerId()));

        //join the second player
        user = new User();
        loginInfo = new LoginInfo("2", "42", 1);

        User finalUser1 = user;
        LoginInfo finalLoginInfo1 = loginInfo;
        assertDoesNotThrow(() -> standardGameController.joinPlayer(client, finalUser1, finalLoginInfo1.playerId()));


    }

    /**
     * Method under test: {@link StandardGameController#leavePlayer(ClientInterface)}
     * Testing player leave
     */
    @Test
    void testLeavePlayer() {

        Game game = GameBuilder.build(new NewGameInfo("42", "STANDARD", 2, System.currentTimeMillis()));
        assertDoesNotThrow(() -> {
            // Arrange
            StandardGameController standardGameController = new StandardGameController(game, mock(Consumer.class));
            ClientInterface clientInterface = new ClientInterface() {
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

            standardGameController.joinPlayer(clientInterface, new User(), "1");
            // Act
            standardGameController.leavePlayer(clientInterface);
        });
    }

    /**
     * Method under test: {@link StandardGameController#leavePlayer(ClientInterface)}
     * Test leave player with non-connected client
     */
    @Test
    void testLeavePlayer2() {
        assertThrows(GameAccessDeniedException.class,
                () -> (new StandardGameController(null, mock(Consumer.class))).leavePlayer(null));
    }

    //test join with a full lobby, leaving the game and trying to join again

    /**
     * Methods under test:
     *
     * <ul>
     *     <li>{@link StandardGameController#joinPlayer(ClientInterface, User, String)}</li>
     *     <li>{@link StandardGameController#leavePlayer(ClientInterface)}</li>
     * </ul>
     * Test rejoin ended game
     */
    @Test
    void rejoinEndedGame() throws GameAccessDeniedException, InterruptedException {
        Game game = GameBuilder.build(new NewGameInfo("42", "STANDARD", 2, System.currentTimeMillis()));
        StandardGameController standardGameController = new StandardGameController(game, lobbyController -> {
        });

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1", "42", 1);
        ClientInterface client = mock(ClientInterface.class);

        User finalUser1 = user;
        LoginInfo finalLoginInfo1 = loginInfo;

        standardGameController.joinPlayer(client, user, finalLoginInfo1.playerId());


        //join the second player
        user = new User();
        loginInfo = new LoginInfo("2", "42", 1);
        var fclient = mock(ClientInterface.class);

        LoginInfo finalLoginInfo2 = loginInfo;

        standardGameController.joinPlayer(fclient, user, finalLoginInfo2.playerId());


        //leave the game


        try {
            standardGameController.leavePlayer(fclient);
        } catch (GameAccessDeniedException e) {
            throw new RuntimeException(e);
        }

        //try to join again in 6 seconds


        User finalUser = user;

        Thread.sleep(400);
        assertDoesNotThrow(() -> standardGameController.joinPlayer(fclient, finalUser, finalLoginInfo2.playerId()));

        //now try to leave again and join after 6 seconds
        assertDoesNotThrow(() -> standardGameController.leavePlayer(fclient));
        Thread.sleep(7000);
        assertDoesNotThrow(() -> standardGameController.joinPlayer(fclient, finalUser1, finalLoginInfo2.playerId()));
        //network test can really check result without using a real network


    }


    //join game with same id

    /**
     * Method under test: {@link StandardGameController#joinPlayer(ClientInterface, User, String)}
     * Testing joining a game with two client with same id
     */

    @Test
    void joinWithExistingId() throws GameAccessDeniedException {

        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        StandardGameController standardGameController = new StandardGameController(game, lobbyController -> {
        });

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1", "42", 1);
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

        standardGameController.joinPlayer(client, user, loginInfo.playerId());

        //join the second player
        user = new User();
        loginInfo = new LoginInfo("1", "42", 1);

        User finalUser = user;
        LoginInfo finalLoginInfo = loginInfo;
        assertThrows(GameAccessDeniedException.class, () -> standardGameController.joinPlayer(client, finalUser, finalLoginInfo.playerId()));
    }

    //leave with non-existing client

    /**
     * Method under test: {@link StandardGameController#leavePlayer(ClientInterface)}
     * Test leave with non-connected client
     */
    @Test
    void leaveWithNonExistingClient() throws GameAccessDeniedException {

        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        StandardGameController standardGameController = new StandardGameController(game, lobbyController -> {
        });

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1", "42", 1);
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

        standardGameController.joinPlayer(client, user, loginInfo.playerId());


        //join the second player
        client = mock(ClientInterface.class);
        ClientInterface finalClient = client;
        assertThrows(GameAccessDeniedException.class, () -> standardGameController.leavePlayer(finalClient));

    }
    //test observers throwing exceptions in joinPlayer

    /**
     * Methods under test:
     * {@link StandardGameController#joinPlayer(ClientInterface, User, String)}
     * Test {@link util.Observer} disconnection on {@link ClientInterface} throwing {@link RemoteException}
     */
    @Test
    void observersErrorTest() throws GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        StandardGameController standardGameController = new StandardGameController(game, lobbyController -> {
        });

        User user = new User();
        LoginInfo loginInfo = new LoginInfo("1", "42", 1);
        ClientInterface client = mock(ClientInterface.class);

        standardGameController.joinPlayer(client, user, loginInfo.playerId());

        //join the second player
        user = new User();
        loginInfo = new LoginInfo("2", "42", 1);

        User finalUser = user;
        LoginInfo finalLoginInfo = loginInfo;
        client = mock(ClientInterface.class);
        ClientInterface finalClient = client;

        standardGameController.joinPlayer(finalClient, finalUser, finalLoginInfo.playerId());

    }


    //test doPlayerMove

    /**
     * Methods under test:
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing legal move
     */
    @Test
    void doPlayerMoveLegalMove() {
        assertDoesNotThrow(() -> {
            Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
            var standardGameController = new StandardGameController(game, lobbyController -> {
            });
            //first we test a move before the game starts and player is not in the game
            List<PickedTile> pickedTiles = new ArrayList<>();
            pickedTiles.add(new PickedTile(1, 1));

            var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
            standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

            final GameInfo[] gameInfo1 = {null};
            final PlayerInfo[] playerInfo1 = {null};
            ClientInterface client = new ClientInterface() {
                @Override
                public void bind(ServerInterface server) throws RemoteException {

                }

                @Override
                public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


                }

                @Override
                public void update(GameInfo o, Game.Event evt) throws RemoteException {
                    gameInfo1[0] = o;
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
                    playerInfo1[0] = o;
                }

                @Override
                public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
                }

                @Override
                public void update(UserInfo o, User.Event evt) throws RemoteException {
                }
            };

            standardGameController.joinPlayer(client, new User(), "1");

            //now we test a move after before the game starts and player is in the game
            standardGameController.doPlayerMove(client, playerMoveInfo);
            //now we add a second player and start the game
            final GameInfo[] gameInfo = {null};
            final LivingRoomInfo[] livingRoomInfo = {null};
            final PlayerInfo[] playerInfo = {null};

            ClientInterface client2 = new ClientInterface() {
                @Override
                public void bind(ServerInterface server) throws RemoteException {

                }

                @Override
                public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

                }

                @Override
                public void update(GameInfo o, Game.Event evt) throws RemoteException {
                    gameInfo[0] = o;
                }


                @Override
                public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                    livingRoomInfo[0] = o;
                }

                @Override
                public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
                }

                @Override
                public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
                }

                @Override
                public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                    playerInfo[0] = o;
                }

                @Override
                public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

                }

                @Override
                public void update(UserInfo o, User.Event evt) throws RemoteException {
                }
            };

            standardGameController.joinPlayer(client2, new User(), "2");


            //now we will do some legal moves like picking 4,1
            pickedTiles = new ArrayList<>();
            pickedTiles.add(new PickedTile(4, 1));
            playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
            standardGameController.doPlayerMove(client, playerMoveInfo);
            //with the second player we test a move after the game starts
            standardGameController.doPlayerMove(client2, playerMoveInfo);
            //wait 100ms and check living room at 4,1
            Thread.sleep(400);
            assert livingRoomInfo[0] != null && livingRoomInfo[0].board()[4][1] == Tile.EMPTY;
        });
    }


    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveMalformedMove() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };
        standardGameController.joinPlayer(client2, new User(), "2");


        //malformed move
        pickedTiles = new ArrayList<>();
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);

        //wait 100ms and check living room at 4,1
        Thread.sleep(400);
        assert (playerInfo[0] != null && playerInfo[0].errorMessage().contains("Malformed move")) || (playerInfo1[0] != null && playerInfo1[0].errorMessage().contains("Malformed move"));
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveColumnOutOfBounds() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        //column out of bounds
        pickedTiles.add(new PickedTile(2, 3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, -1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check living room at 2,3
        Thread.sleep(400);
        assert livingRoomInfo[0] != null && livingRoomInfo[0].board()[2][3] != Tile.EMPTY;
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveNonDifferentTiles() throws PlayerNotExistsException, InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };
        standardGameController.joinPlayer(client2, new User(), "2");


        //tile are not different
        pickedTiles.add(new PickedTile(4, 1));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        Tile[][] tiles = new Tile[6][5];
        //fill with Tile.TROPHIES_1
        Arrays.stream(tiles).forEach(a -> Arrays.fill(a, Tile.TROPHIES_1));

        game.getPlayer("2").getShelf().setTiles(tiles);
        game.getPlayer("1").getShelf().setTiles(tiles);

        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check playerInfos, one of them should have Malformed move: Not enough space to insert tiles in shelf error
        Thread.sleep(400);
        assert playerInfo[0] != null && playerInfo1[0] != null && (playerInfo[0].errorMessage().equals("Malformed move: Not enough space to insert tiles in shelf") || playerInfo1[0].errorMessage().equals("Malformed move: Not enough space to insert tiles in shelf"));
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveLegal() throws PlayerNotExistsException, InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");

        Tile[][] tiles = new Tile[6][5];

        //fill with Tile.EMPTY
        Arrays.stream(tiles).forEach(a -> Arrays.fill(a, Tile.EMPTY));

        game.getPlayer("2").getShelf().setTiles(tiles);
        game.getPlayer("1").getShelf().setTiles(tiles);

        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 4));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);

        //wait 100ms and check living room at 1,4
        Thread.sleep(400);
        assert livingRoomInfo[0] != null && livingRoomInfo[0].board()[1][4] == Tile.EMPTY;


    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Legal move
     */
    @Test
    void doPlayerMoveLegal3() throws PlayerNotExistsException, InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });

        standardGameController.joinPlayer(client, new User(), "1");

        standardGameController.joinPlayer(client2, new User(), "2");
        Tile[][] tiles = new Tile[6][5];
        Arrays.stream(tiles).forEach(a -> Arrays.fill(a, Tile.TROPHIES_1));

        tiles[0][0] = Tile.EMPTY;
        //normal move
        game.getPlayer("1").getShelf().setTiles(tiles);
        game.getPlayer("2").getShelf().setTiles(tiles);
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check living room at 1,3
        Thread.sleep(400);
        assert livingRoomInfo[0] != null && livingRoomInfo[0].board()[1][3] == Tile.EMPTY;


    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveNonAllignedTiles() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });
        standardGameController.joinPlayer(client, new User(), "1");
        standardGameController.joinPlayer(client2, new User(), "2");

        //non aligned tiles
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        pickedTiles.add(new PickedTile(4, 1));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check playerInfos, one of them should have Malformed move: Tiles are not aligned error

        Thread.sleep(300);
        assert playerInfo[0] != null && playerInfo1[0] != null && (playerInfo[0].errorMessage().equals("Malformed move: Tiles are not aligned") || playerInfo1[0].errorMessage().equals("Malformed move: Tiles are not aligned"));
        //check that living room at 1,3 and 4,1 are not empty
        assert livingRoomInfo[0] != null && livingRoomInfo[0].board()[1][3] != Tile.EMPTY && livingRoomInfo[0].board()[4][1] != Tile.EMPTY;

    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing move after game ended
     */
    @Test
    void doPlayerMoveEndGame() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });
        standardGameController.joinPlayer(client, new User(), "1");
        standardGameController.joinPlayer(client2, new User(), "2");

        //game ended exception
        game.setLastTurn();
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check playerInfos, one of them should have Game ended error


        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 4));

        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(4, 1));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check gameInfo, status should be ENDED
        Thread.sleep(300);
        assert gameInfo[0] != null && gameInfo[0].status() == Game.GameStatus.ENDED;
        assert gameInfo1[0] != null && gameInfo1[0].status() == Game.GameStatus.ENDED;
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing legal move
     */
    @Test
    void doPlayerMoveLegal4() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });
        standardGameController.joinPlayer(client, new User(), "1");
        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });

        standardGameController.joinPlayer(client, new User(), "1");

        standardGameController.joinPlayer(client2, new User(), "2");


        pickedTiles = new ArrayList<>();

        pickedTiles.add(new PickedTile(0, 0));
        var board = game.getLivingRoom().getBoard();
        board[0][0] = Tile.TROPHIES_1;
        game.getLivingRoom().setBoard(board);
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);

        Thread.sleep(400);
        assert game.getLivingRoom().getBoard()[0][0] == Tile.EMPTY;

    }


    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveNonAdjacentTilesRow() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));
        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);
        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };
        standardGameController.joinPlayer(client, new User(), "1");
        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};
        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };
        standardGameController.joinPlayer(client2, new User(), "2");
        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });
        standardGameController.joinPlayer(client, new User(), "1");
        standardGameController.joinPlayer(client2, new User(), "2");
        //non adjacent tiles
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        pickedTiles.add(new PickedTile(1, 5));
        //fill a board with empty tiles except for 1,3 and 1,5
        Tile[][] board;
        board = new Tile[9][9];
        Arrays.stream(board).forEach(a -> Arrays.fill(a, Tile.EMPTY));
        board[1][3] = Tile.TROPHIES_1;
        board[1][5] = Tile.TROPHIES_1;
        game.getLivingRoom().setBoard(board);
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait 100ms and check living room at 1,3 and 1,5, and check playerInfos, one of them should have Malformed move: Tiles are not aligned error
        Thread.sleep(500);
        assert livingRoomInfo[0] != null && game.getLivingRoom().getBoard()[1][3] != Tile.EMPTY && game.getLivingRoom().getBoard()[1][5] != Tile.EMPTY;
        assert playerInfo[0] != null && playerInfo1[0] != null && (playerInfo[0].errorMessage().equals("Malformed move: Tiles are not aligned") || playerInfo1[0].errorMessage().equals("Malformed move: Tiles are not aligned"));
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveNonAdjacentTilesColumn() throws InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};

        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });
        standardGameController.joinPlayer(client, new User(), "1");
        standardGameController.joinPlayer(client2, new User(), "2");


        Tile[][] board;

        //non adjacent tiles but same column
        board = new Tile[9][9];
        Arrays.stream(board).forEach(a -> Arrays.fill(a, Tile.EMPTY));
        board[1][3] = Tile.TROPHIES_1;
        board[3][3] = Tile.TROPHIES_1;
        game.getLivingRoom().setBoard(board);
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        pickedTiles.add(new PickedTile(3, 3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait
        Thread.sleep(400);
        assert livingRoomInfo[0] != null && game.getLivingRoom().getBoard()[1][3] != Tile.EMPTY && game.getLivingRoom().getBoard()[3][3] != Tile.EMPTY;
        assert playerInfo[0] != null && playerInfo1[0] != null && (playerInfo[0].errorMessage().equals("Malformed move: Tiles are not aligned") || playerInfo1[0].errorMessage().equals("Malformed move: Tiles are not aligned"));
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing Malformed move
     */
    @Test
    void doPlayerMoveLastTurn() throws PlayerNotExistsException, InterruptedException, GameAccessDeniedException {
        Game game = GameBuilder.build(new NewGameInfo("gameId", "STANDARD", 2, System.currentTimeMillis()));
        var standardGameController = new StandardGameController(game, lobbyController -> {
        });
        //first we test a move before the game starts and player is not in the game
        List<PickedTile> pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 1));

        var playerMoveInfo = new PlayerMoveInfo(pickedTiles, 1);
        standardGameController.doPlayerMove(mock(ClientInterface.class), playerMoveInfo);

        final GameInfo[] gameInfo1 = {null};
        final PlayerInfo[] playerInfo1 = {null};
        ClientInterface client = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {


            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo1[0] = o;
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
                playerInfo1[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client, new User(), "1");

        //now we test a move after before the game starts and player is in the game
        standardGameController.doPlayerMove(client, playerMoveInfo);
        //now we add a second player and start the game
        final GameInfo[] gameInfo = {null};
        final LivingRoomInfo[] livingRoomInfo = {null};
        final PlayerInfo[] playerInfo = {null};
        TimedLock<Boolean> lock = new TimedLock<>(false);
        ClientInterface client2 = new ClientInterface() {
            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                gameInfo[0] = o;
                lock.unlock(true);
            }


            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                livingRoomInfo[0] = o;
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                playerInfo[0] = o;
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

            }

            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
            }
        };

        standardGameController.joinPlayer(client2, new User(), "2");


        game = GameBuilder.build(new NewGameInfo("32", "STANDARD", 2, System.currentTimeMillis()));
        standardGameController = new StandardGameController(game, lobbyController -> {
        });
        standardGameController.joinPlayer(client, new User(), "1");
        standardGameController.joinPlayer(client2, new User(), "2");


        Tile[][] tiles = new Tile[6][5];


        //fill with Tile.EMPTY
        Arrays.stream(tiles).forEach(a -> Arrays.fill(a, Tile.EMPTY));
        //now we fill the shelves with TROPHIES_1
        Arrays.stream(tiles).forEach(a -> Arrays.fill(a, Tile.TROPHIES_1));
        //set one space in the shelf to EMPTY, column 0
        tiles[0][0] = Tile.EMPTY;
        game.getPlayer("2").getShelf().setTiles(tiles);
        game.getPlayer("1").getShelf().setTiles(tiles);
        //now we do one move and check if lastTurn is set
        pickedTiles = new ArrayList<>();
        pickedTiles.add(new PickedTile(1, 3));
        playerMoveInfo = new PlayerMoveInfo(pickedTiles, 0);
        standardGameController.doPlayerMove(client, playerMoveInfo);
        standardGameController.doPlayerMove(client2, playerMoveInfo);
        //wait until the move is done, gameinfo is updated
        lock.lock(3000);
        if (!lock.getValue()) {
            fail("GameInfo not updated");
        }
        Thread.sleep(400);
        assert gameInfo[0] != null && gameInfo[0].lastTurn();
    }

    /**
     * {@link StandardGameController#doPlayerMove(ClientInterface, PlayerMoveInfo)}
     * Testing send message thoroughly
     */
    @Test
    void sendMessage() throws PlayerNotExistsException, GameAccessDeniedException {

        var client2 = new ClientInterface() {
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
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }
        };
        var client = new ClientInterface() {
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
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }
        };

        var game = GameBuilder.build(new NewGameInfo("42", "STANDARD", 2, System.currentTimeMillis()));


        var standardGameController = new StandardGameController(game, lobbyController -> {
        });


        standardGameController.joinPlayer(client, new User(), "1");


        standardGameController.joinPlayer(client2, new User(), "2");

        standardGameController.sendMessage(client, new StandardMessage("1", "2", "test"));
        assertEquals(game.getPlayer("1").getPlayerChat().getMessages().get(0).getText(), "test");
        assertEquals(game.getPlayer("2").getPlayerChat().getMessages().get(0).getText(), "test");
        assertEquals(game.getPlayer("1").getPlayerChat().getMessages().get(0).getSender(), "1");
        assertEquals(game.getPlayer("2").getPlayerChat().getMessages().get(0).getSender(), "1");
        assertEquals(game.getPlayer("1").getPlayerChat().getMessages().get(0).getReceiver(), "2");
        assertEquals(game.getPlayer("2").getPlayerChat().getMessages().get(0).getReceiver(), "2");
        //now we test for a message from a non-existing client
        var client3 = new ClientInterface() {
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
            public void update(GameInfo o, Game.Event evt) throws RemoteException {

            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {

            }

            @Override
            public void bind(ServerInterface server) throws RemoteException {

            }
        };
        standardGameController.sendMessage(client3, new StandardMessage("1", "2", "test"));

        //subject non-existing
        standardGameController.sendMessage(client, new StandardMessage("1", "3", "test"));
    }

    /**
     * {@link StandardGameController#joinPlayer(ClientInterface, User, String)}
     * Testing {@link util.Observer} disconnection after {@link RemoteException} is thrown by {@link ClientInterface}
     */
    @Test
    void disconectObserver() throws GameAccessDeniedException {
        var client2 = new ClientInterface() {
            @Override
            public void update(UserInfo o, User.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(GameInfo o, Game.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
                throw new RemoteException();
            }

            @Override
            public void bind(ServerInterface server) throws RemoteException {
                throw new RemoteException();
            }
        };
        var game = GameBuilder.build(new NewGameInfo("42", "STANDARD", 2, System.currentTimeMillis()));


        var standardGameController = new StandardGameController(game, lobbyController -> {
        });


        standardGameController.joinPlayer(client2, new User(), "2");


    }
}

