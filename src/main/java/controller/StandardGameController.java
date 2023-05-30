package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.LobbyController;
import distibuted.interfaces.ClientInterface;
import model.Tile;
import model.Token;
import model.User;
import model.abstractModel.*;
import model.exceptions.*;
import modelView.LoginInfo;
import modelView.PickedTile;
import modelView.PlayerMoveInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Observable;
import util.Observer;

import java.rmi.RemoteException;
import java.util.*;

public class StandardGameController implements GameController, LobbyController {
    private final Game game;
    private final @NotNull Map<ClientInterface, User> userAssociation;

    private final @NotNull Map<User, Player> playerAssociation;

    /**
     * @param game game to be managed
     */
    public StandardGameController(Game game) {
        this.game = game;
        this.userAssociation = new HashMap<>();
        this.playerAssociation = new HashMap<>();
    }

    ///LOBBY CONTROLLER/////////////////////

    /**
     * Observer to update the PlayerInfo
     *
     * @param newClient client to be added
     * @return Player's Observer to be added
     */
    private @NotNull Observer<Player, Player.Event> getPlayerObserver(@NotNull ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(@NotNull Player o, Player.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching player observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the GameStatus
     *
     * @param newClient client to be added
     * @return Observer of the GameStatus to be added
     */
    private @NotNull Observer<Game, Game.Event> getGameObserver(@NotNull ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(@NotNull Game o, Game.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching game observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the CommonGoal
     *
     * @param newClient client to be added
     * @return Observer of the CommonGoal to be added
     */

    private @NotNull Observer<CommonGoal, CommonGoal.Event> getCommonGoalObserver(@NotNull ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(@NotNull CommonGoal o, CommonGoal.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching commonGoal observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the LivingRoom
     *
     * @param newClient client to be added
     * @return Observer of the LivingRoom to be added
     */

    private @NotNull Observer<LivingRoom, LivingRoom.Event> getLivingRoomObserver(@NotNull ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(@NotNull LivingRoom o, LivingRoom.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching livingRoom observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the PlayerChat
     *
     * @param newClient client to be added
     * @return Observer of the PlayerChat to be added
     */

    private @NotNull Observer<PlayerChat, PlayerChat.Event> getPlayerChatObserver(@NotNull ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(@NotNull PlayerChat o, PlayerChat.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching playerChat observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the PersonalGoal
     *
     * @param newClient client to be added
     * @return Observer of the PersonalGoal to be added
     */

    private @NotNull Observer<PersonalGoal, PersonalGoal.Event> getPersonalGoalObserver(@NotNull ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(@NotNull PersonalGoal o, PersonalGoal.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching personalGoal observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the Shelf
     *
     * @param newClient client to be added
     * @return Observer of the Shelf to be added
     */

    private @NotNull Observer<Shelf, Shelf.Event> getShelfObserver(@NotNull ClientInterface newClient, @NotNull Player joinedPlayer) {
        return new Observer<>() {
            @Override
            public void update(@NotNull Shelf o, Shelf.Event arg) {
                try {
                    newClient.update(o.getInfo(joinedPlayer.getId()), arg);
                } catch (RemoteException e) {
                    System.err.println("...detaching shelf observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Add player to the game with all necessary preparations
     *
     * @param newClient client that wants to join the game
     * @param info      login info of the player
     * @throws GameAccessDeniedException if the game is already ended or the player id already exists or the matchmaking is closed
     */
    @Override
    public synchronized void joinPlayer(@NotNull ClientInterface newClient, @NotNull User newUser, @NotNull LoginInfo info) throws GameAccessDeniedException {
        //Should we just Throw the different exceptions instead of catching them?
        try {
            this.game.addPlayer(info.playerId());

            Player newPlayer = this.game.getPlayer(info.playerId());

            this.userAssociation.put(newClient, newUser);

            /* Put newClient into known client */
            this.playerAssociation.put(newUser, newPlayer);

            newUser.reportEvent(User.Status.JOINED, "Joined game: " + this.game.getGameId() + ", you are:" + info.playerId(), info.time(), User.Event.GAME_JOINED);

            this.addObservers(newClient, newPlayer);

            /* If game is ready to be started we force the first */
            if (this.game.getGameStatus() == Game.GameStatus.STARTED) {
                this.game.getLivingRoom().refillBoard();
                this.game.updatePlayersTurn();
            }
        } catch (PlayerAlreadyExistsException | PlayerNotExistsException e) {
            throw new GameAccessDeniedException("Player id already exists");//Same as above
        } catch (MatchmakingClosedException | GameEndedException e) {
            throw new GameAccessDeniedException("Game matchmaking closed"); //Same as above
        }
    }

    private void closeTheGame(String message) {
        long eventTime = System.currentTimeMillis();
        for (User u : this.userAssociation.values()) {
            u.reportEvent(User.Status.NOT_JOINED, message, eventTime, User.Event.GAME_LEAVED);
        }

        this.game.close();
        this.game.deleteObservers();
        this.game.getCommonGoals().forEach(Observable::deleteObservers);
        this.game.getLivingRoom().deleteObservers();
        for (Player p : this.playerAssociation.values()) {
            p.deleteObservers();
            p.getPlayerChat().deleteObservers();
            p.getPersonalGoals().forEach(Observable::deleteObservers);
            p.getShelf().deleteObservers();
        }
    }

    @Override
    public synchronized void leavePlayer(ClientInterface client) throws GameAccessDeniedException {
        if (!this.userAssociation.containsKey(client))
            throw new GameAccessDeniedException("Client not connected to this game");
        Player leavedPlayer = this.playerAssociation.get(this.userAssociation.get(client));
        String eventMessage = leavedPlayer.getId() + " has leaved the game";
        this.closeTheGame(eventMessage);

        System.err.println("PLAYER USCITO");
    }

    /**
     * Add observers to all needed objects
     *
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private void addObservers(@NotNull ClientInterface newClient, @NotNull Player newPlayer) {
        /* Add Player status observer */
        newPlayer.addObserver(this.getPlayerObserver(newClient));

        /* Add Game status observer */
        this.game.addObserver(this.getGameObserver(newClient));

        /* Add CommonGoal status observers */
        this.game.getCommonGoals().forEach(goal -> goal.addObserver(this.getCommonGoalObserver(newClient)));

        /* Add LivingRoom status observer */
        this.game.getLivingRoom().addObserver(this.getLivingRoomObserver(newClient));

        /* Add PlayerChat status observer */
        newPlayer.getPlayerChat().addObserver(this.getPlayerChatObserver(newClient));

        /* Add PersonalGoals status observer */
        newPlayer.getPersonalGoals().forEach(personalGoal -> personalGoal.addObserver(this.getPersonalGoalObserver(newClient)));

        /* Add Shelf status observer */
        newPlayer.getShelf().addObserver(this.getShelfObserver(newClient, newPlayer));
        /* Add Shelf status observer of new player to all already joined players */
        /* Add Shelf status observer of all already joined players to new player */
        this.userAssociation.forEach((joinedClient, joinedUser) -> {
            newPlayer.getShelf().addObserver(this.getShelfObserver(joinedClient, newPlayer));
            Player joinedPlayer = this.playerAssociation.get(joinedUser);
            joinedPlayer.getShelf().addObserver(this.getShelfObserver(newClient, joinedPlayer));
        });
    }

    /**
     * Execute a player move
     *
     * @param client     new player's ClientInterface
     * @param playerMove player move to execute
     */
    public synchronized void doPlayerMove(ClientInterface client, @Nullable PlayerMoveInfo playerMove) {
        try {
            /* If we receive a request from an invalid client we discard it */
            if (!this.userAssociation.containsKey(client))
                throw new IllegalArgumentException("User not allowed");
            /* If game is not started we discard the request*/
            if (game.getGameStatus() != Game.GameStatus.STARTED)
                throw new GameNotStartedException();
            /* Get player information associated with his client */
            Player player = this.playerAssociation.get(this.userAssociation.get(client));
            /* If it isn't player turn we discard the request*/
            if (!player.getId().equals(this.game.getTurnPlayerId()))
                throw new IllegalArgumentException("Not player's turn");
            /* Get board and player shelf status */
            Tile[][] shelf = player.getShelf().getTiles();
            Tile[][] board = this.game.getLivingRoom().getBoard();

            /* Do some checks on "move" object, if malformed we discard it */
            if (playerMove == null || playerMove.pickedTiles() == null || playerMove.pickedTiles().size() == 0)
                throw new IllegalArgumentException("Malformed move object");

            if (playerMove.columnToInsert() < 0 || playerMove.columnToInsert() > shelf[0].length - 1)
                throw new IllegalArgumentException("Column index out of bounds");

            if (!this.areTilesDifferent(new ArrayList<>(playerMove.pickedTiles())))
                throw new IllegalArgumentException("Tiles are not different");

            if (!this.areTilesAligned(new ArrayList<>(playerMove.pickedTiles())))
                throw new IllegalArgumentException("Tile are not aligned");

            for (PickedTile tile : playerMove.pickedTiles())
                if (!this.isTilePickable(tile.row(), tile.col(), board))
                    throw new IllegalArgumentException("Tile not pickable");

            if (playerMove.pickedTiles().size() > this.freeShelfColumnSpaces(playerMove.columnToInsert(), shelf))
                throw new IllegalArgumentException("Not enough space to insert tiles in shelf");

            /* Foreach tile we pick it from board and put it on the shelf */
            for (PickedTile tile : playerMove.pickedTiles()) {
                Tile picked = board[tile.row()][tile.col()];
                board[tile.row()][tile.col()] = Tile.EMPTY;
                this.insertTileInShelf(playerMove.columnToInsert(), shelf, picked);
            }

            /* Update board and player shelf status*/
            this.game.getLivingRoom().setBoard(board);
            this.game.getLivingRoom().refillBoard();
            player.getShelf().setTiles(shelf);

            /* If shelf has been filled we signal that this will be the last round of turns */
            if (this.evaluateFullShelf(shelf)) {
                /* If nobody else has already completed the shelf we assign a "GAME_END" token*/
                if (!this.game.isLastTurn())
                    player.addAchievedCommonGoal("First player that have completed the shelf", Token.TOKEN_GAME_END);

                this.game.setLastTurn();
            }

            /* Check if player has achieved common goals */
            for (CommonGoal commonGoal : this.game.getCommonGoals())
                if (!player.getAchievedCommonGoals().containsKey(commonGoal.getEvaluator().getId()))
                    if (commonGoal.getEvaluator().evaluate(shelf)) {
                        player.addAchievedCommonGoal(commonGoal.getEvaluator().getId(), commonGoal.popToken());
                    }


            /* Check if player has achieved personal goals*/
            for (PersonalGoal personalGoal : player.getPersonalGoals()) {
                if (!personalGoal.isAchieved())
                    if (personalGoal.evaluate(shelf))
                        personalGoal.setAchieved();
            }

            //TODO salvare il gioco

            /* Pass turn to next player */
            this.game.updatePlayersTurn();

        } catch (IllegalArgumentException | GameNotStartedException e) {
            if (this.userAssociation.containsKey(client) && this.playerAssociation.containsKey(this.userAssociation.get(client))) {
                this.playerAssociation.get(this.userAssociation.get(client)).reportError(e.getMessage());
            } else {
                e.printStackTrace();
            }
        } catch (GameEndedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to players included in the message receiver
     *
     * @param client     new player's ClientInterface
     * @param newMessage message to send
     */
    public synchronized void sendMessage(ClientInterface client, @NotNull Message newMessage) {
        try {
            if (!this.userAssociation.containsKey(client))
                throw new IllegalArgumentException("User not allowed");

            boolean receiverFound = false;
            for (Player p : this.playerAssociation.values()) {
                if (p.getId().equals(newMessage.getReceiver())) {
                    receiverFound = true;
                    break;
                }
            }

            if (!(newMessage.getReceiver().isEmpty() || receiverFound))
                throw new IllegalArgumentException("Receiver of the message does not exists");

            //TODO sender is not a player?

            for (Player p : this.playerAssociation.values()) {
                if (newMessage.getReceiver().isEmpty() || newMessage.getReceiver().equals(p.getId()) || newMessage.getSender().equals(p.getId()))
                    p.getPlayerChat().addMessage(newMessage);
            }

        } catch (IllegalArgumentException e) {
            if (this.userAssociation.containsKey(client) && this.playerAssociation.containsKey(this.userAssociation.get(client))) {
                this.playerAssociation.get(this.userAssociation.get(client)).reportError(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }


    //do we need javadoc for private methods?

    /**
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(@NotNull List<PickedTile> pickedTiles) {
        for (int i = 0; i < pickedTiles.size(); i++) {
            for (int j = i + 1; j < pickedTiles.size(); j++) {
                if (pickedTiles.get(i).row() == pickedTiles.get(j).row())
                    if (pickedTiles.get(i).col() == pickedTiles.get(j).col())
                        return false;
            }
        }
        return true;
    }


    //Same as above

    /**
     * @param column column to check
     * @param shelf  player shelf
     * @return number of free spaces in the column
     */
    private int freeShelfColumnSpaces(int column, Tile[] @NotNull [] shelf) {
        int spaces = 0;
        for (Tile[] row : shelf) {
            if (row[column] == Tile.EMPTY)
                spaces++;
        }
        return spaces;
    }

    //Same as above

    /**
     * @param column column to check
     * @param shelf  player shelf
     * @param tile   tile to insert
     */
    private void insertTileInShelf(int column, Tile[] @NotNull [] shelf, Tile tile) {
        int row = shelf.length - 1;
        while (shelf[row][column] != Tile.EMPTY)
            row--;

        shelf[row][column] = tile;
    }

    /**
     * @param row    row of the tile
     * @param column column of the tile
     * @param board  board to check
     * @return true if tile is pickable, false otherwise
     */
    private boolean isTilePickable(int row, int column, Tile[] @NotNull [] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column] == Tile.EMPTY || board[row][column] == null)
            return false;

        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        return board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY;
    }

    /**
     * @param shelf shelf to check
     * @return true if shelf is full, false otherwise
     */
    private boolean evaluateFullShelf(Tile[] @NotNull [] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile == Tile.EMPTY)
                    return false;

        return true;
    }

    private boolean areTilesAligned(@NotNull List<PickedTile> pickedTiles) {
        boolean rowAligned = true;
        boolean colAligned = true;

        for (int i = 1; i < pickedTiles.size(); i++) {
            rowAligned = rowAligned && (pickedTiles.get(i - 1).row() == pickedTiles.get(i).row());
            colAligned = colAligned && (pickedTiles.get(i - 1).col() == pickedTiles.get(i).col());
        }
        if (rowAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::col));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).col() + 1 != pickedTiles.get(i + 1).col())
                    return false;
        }
        if (colAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::row));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).row() + 1 != pickedTiles.get(i + 1).row())
                    return false;
        }


        return rowAligned || colAligned;
    }
}
