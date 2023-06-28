package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.LobbyController;
import distibuted.interfaces.ClientInterface;
import model.Tile;
import model.Token;
import model.User;
import model.abstractModel.*;
import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import modelView.PickedTile;
import modelView.PlayerMoveInfo;
import util.Observable;
import util.Observer;
import util.TimedLock;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Consumer;

/**
 * This is a combined implementation of a {@link LobbyController} and a {@link GameController}
 * <p>
 * The game rules implemented follow the standard rules
 */
@SuppressWarnings("rawtypes")
public class StandardGameController implements GameController, LobbyController {
    /**
     * The model of the game this controller is interacting with
     */
    private final Game game;

    /**
     * Map of association between a client and its user reference on the server
     */
    private final Map<ClientInterface, User> userAssociation;

    /**
     * Map of association between a user reference on the server and its playerID into game model
     */
    private final Map<User, String> playerAssociation;

    /**
     * Map of association between a client and all the observers it is attached to, observers are also associated with
     * the observable they are attached to.
     * This object is mainly used when a client needs to be detached from the game
     */
    @SuppressWarnings("rawtypes")
    private final Map<ClientInterface, Map<Observable, Observer>> observerAssociation;

    /**
     * Consumer called after the game has been definitely closed. This allows to notify who generated this instance that
     * this game controller is no longer useful
     */
    private final Consumer<? super LobbyController> gameClosedCallback;

    /**
     * The timed lock used by lobby controller to control leave and join flow
     */
    private final TimedLock<Boolean> lobbyLock = new TimedLock<>(false);

    /**
     * This constructor builds an instance that uses the given game model and gameClosed callback,
     * it is initialized with empty user, player and observer association
     *
     * @param game               The game model that the game controller need to use
     * @param gameClosedCallback The gameClosed callback
     */
    public StandardGameController(Game game, Consumer<? super LobbyController> gameClosedCallback) {
        super();
        this.game = game;
        this.gameClosedCallback = gameClosedCallback;
        this.userAssociation = new HashMap<>();
        this.playerAssociation = new HashMap<>();
        this.observerAssociation = new HashMap<>();
    }

    ///LOBBY CONTROLLER/////////////////////

    /**
     * {@inheritDoc}
     *
     * @param newClient The client that needs to be attached to the game
     * @param newUser   The user reference of the client into the server
     * @param playerId  The new player ID
     * @throws GameAccessDeniedException if the game is already ended or the player id already exists or the matchmaking is closed
     */
    @Override
    public void joinPlayer(ClientInterface newClient, User newUser, String playerId) throws GameAccessDeniedException {
        synchronized (this.lobbyLock) {
            try {
                /* Get model status before the player joins*/
                Game.GameStatus previousStatus = this.game.getGameStatus();

                /* Try to join the player */
                this.game.addPlayer(playerId);

                /* Attach all the observers to the client */
                try {
                    this.addObservers(newClient, playerId);
                } catch (PlayerNotExistsException e) {
                    this.printModelError("Player that should exists does not exists, warning due to possible malfunctions");
                }

                /* Authorize the client to use this controller*/
                this.userAssociation.put(newClient, newUser);

                /* Put newUser into known users */
                this.playerAssociation.put(newUser, playerId);

                /* Get model status after the player has joined */
                Game.GameStatus newStatus = this.game.getGameStatus();

                if (previousStatus == Game.GameStatus.SUSPENDED) {
                    /* If the model was in suspended status then notify to resume the game */
                    this.lobbyLock.unlock(true);

                } else if (previousStatus == Game.GameStatus.MATCHMAKING && newStatus == Game.GameStatus.STARTED) {
                    /* If game is ready to be started we force the first turn*/
                    this.game.getLivingRoom().refillBoard();
                    this.game.updatePlayersTurn();
                }

            } catch (PlayerAlreadyExistsException e) {
                throw new GameAccessDeniedException("Player id already exists");
            } catch (MatchmakingClosedException | GameEndedException e) {
                throw new GameAccessDeniedException("Game matchmaking closed");
            }
        }
    }

    /**
     * Add observers to all needed observable model objects
     *
     * @param newClient   new player's ClientInterface
     * @param newPlayerId new player's id
     * @throws PlayerNotExistsException if the player does not exists
     */
    @SuppressWarnings("rawtypes")
    private void addObservers(ClientInterface newClient, String newPlayerId) throws PlayerNotExistsException {
        /* Creates a map to associate each observer to his observable */
        Map<Observable, Observer> newObserverAssociation = new HashMap<>();

        Player newPlayer = this.game.getPlayer(newPlayerId);

        /* Add Player status observer */
        Observer<Player, Player.Event> playerEventObservable = this.getPlayerObserver(newClient);
        newObserverAssociation.put(newPlayer, playerEventObservable);
        newPlayer.addObserver(playerEventObservable);

        /* Add Game status observer */
        Observer<Game, Game.Event> gameEventObserver = this.getGameObserver(newClient);
        newObserverAssociation.put(this.game, gameEventObserver);
        this.game.addObserver(gameEventObserver);

        /* Add CommonGoal status observers */
        this.game.getCommonGoals().forEach(goal -> {
                    Observer<CommonGoal, CommonGoal.Event> commonGoalEventObserver = this.getCommonGoalObserver(newClient);
                    newObserverAssociation.put(goal, commonGoalEventObserver);
                    goal.addObserver(commonGoalEventObserver);
                }
        );

        /* Add LivingRoom status observer */
        LivingRoom livingRoom = this.game.getLivingRoom();
        Observer<LivingRoom, LivingRoom.Event> livingRoomEventObserver = this.getLivingRoomObserver(newClient);
        newObserverAssociation.put(livingRoom, livingRoomEventObserver);
        livingRoom.addObserver(livingRoomEventObserver);

        /* Add PlayerChat status observer */
        PlayerChat playerChat = newPlayer.getPlayerChat();
        Observer<PlayerChat, PlayerChat.Event> playerChatEventObserver = this.getPlayerChatObserver(newClient);
        newObserverAssociation.put(playerChat, playerChatEventObserver);
        playerChat.addObserver(playerChatEventObserver);

        /* Add PersonalGoals status observer */
        newPlayer.getPersonalGoals().forEach(personalGoal -> {
            Observer<PersonalGoal, PersonalGoal.Event> personalGoalEventObserver = this.getPersonalGoalObserver(newClient);
            newObserverAssociation.put(personalGoal, personalGoalEventObserver);
            personalGoal.addObserver(personalGoalEventObserver);
        });

        /* Add Shelf status observer */
        Shelf newPlayerShelf = newPlayer.getShelf();
        Observer<Shelf, Shelf.Event> shelfEventObserver = this.getShelfObserver(newClient, newPlayerId);
        newObserverAssociation.put(newPlayerShelf, shelfEventObserver);
        newPlayerShelf.addObserver(this.getShelfObserver(newClient, newPlayerId));

        /* Add Shelf status observer of new player to all already joined players */
        /* Add Shelf status observer of all already joined players to new player */
        for (Map.Entry<ClientInterface, User> association : this.userAssociation.entrySet()) {
            Observer<Shelf, Shelf.Event> newPlayerShelfEventObserver = this.getShelfObserver(association.getKey(), newPlayerId);
            newPlayerShelf.addObserver(newPlayerShelfEventObserver);
            this.observerAssociation.get(association.getKey()).put(newPlayerShelf,newPlayerShelfEventObserver);

            String joinedPlayerId = this.playerAssociation.get(association.getValue());
            Shelf joinedPlayerShelf = this.game.getPlayer(joinedPlayerId).getShelf();

            Observer<Shelf, Shelf.Event> joinedPlayerShelfEventObserver = this.getShelfObserver(newClient, joinedPlayerId);
            newObserverAssociation.put(joinedPlayerShelf, joinedPlayerShelfEventObserver);
            joinedPlayerShelf.addObserver(joinedPlayerShelfEventObserver);
        }

        /* Associate the observers to the client they are updating */
        this.observerAssociation.put(newClient, newObserverAssociation);
    }

    /**
     * Observer to update the PlayerInfo
     *
     * @param newClient client to be added
     * @return Player's Observer to be added
     */
    private Observer<Player, Player.Event> getPlayerObserver(ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(Player o, Player.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
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
    private Observer<Game, Game.Event> getGameObserver(ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(Game o, Game.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
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

    private Observer<CommonGoal, CommonGoal.Event> getCommonGoalObserver(ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(CommonGoal o, CommonGoal.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer -> detaching commonGoal observer");
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

    private Observer<LivingRoom, LivingRoom.Event> getLivingRoomObserver(ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(LivingRoom o, LivingRoom.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer -> detaching livingRoom observer");
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

    private Observer<PlayerChat, PlayerChat.Event> getPlayerChatObserver(ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(PlayerChat o, PlayerChat.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer -> detaching playerChat observer");
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

    private Observer<PersonalGoal, PersonalGoal.Event> getPersonalGoalObserver(ClientInterface newClient) {
        return new Observer<>() {
            @Override
            public void update(PersonalGoal o, PersonalGoal.Event arg) {
                try {
                    newClient.update(o.getInfo(), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer -> detaching personalGoal observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * Observer to update the Shelf
     *
     * @param newClient client to be added
     * @param joinedPlayerId id of the player to be added
     * @return Observer of the Shelf to be added
     */

    private Observer<Shelf, Shelf.Event> getShelfObserver(ClientInterface newClient, String joinedPlayerId) {
        return new Observer<>() {
            @Override
            public void update(Shelf o, Shelf.Event arg) {
                try {
                    newClient.update(o.getInfo(joinedPlayerId), arg);
                } catch (RemoteException e) {
                    System.err.println("Distribution: unable to update observer -> detaching shelf observer");
                    o.deleteObserver(this);
                }
            }
        };
    }

    /**
     * {@inheritDoc}
     *
     * @param client {@inheritDoc}
     * @throws GameAccessDeniedException if client has not joined this game
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void leavePlayer(ClientInterface client) throws GameAccessDeniedException {
        /* Check if client is allowed */
        if (!this.userAssociation.containsKey(client))
            throw new GameAccessDeniedException("Unknown client");

        /* Take lobbyLock */
        synchronized (this.lobbyLock) {
            /* remove User to allowed */
            User leavedUser = this.userAssociation.remove(client);

            /* remove all observer associated with this user */
            Map<Observable, Observer> playerObserverAssociation = this.observerAssociation.get(client);

            playerObserverAssociation.forEach(Observable::deleteObserver);

            /* Reports to the user it has leaved the game */
            leavedUser.reportEvent(User.Status.NOT_JOINED, "Player leaved", User.Event.GAME_LEAVED);

            if (this.game.getGameStatus() == Game.GameStatus.MATCHMAKING)
                /* If the game is Matchmaking then directly close the game */
                this.closeTheGame("Game closed due to disconnection while matchmaking");

            else if (this.game.getGameStatus() == Game.GameStatus.SUSPENDED) {
                /* If the game was suspended then notify to close the game */
                this.lobbyLock.unlock(false);

            } else if (this.game.getGameStatus() == Game.GameStatus.STARTED) {
                /* If the game is running then remove the player */
                try {
                    /* Get the playerID */
                    String leavedPlayer = this.playerAssociation.remove(leavedUser);

                    /* Evaluate if game needs a turn-skip (is player who leaved has the turn) */
                    boolean skipNeeded = this.game.getTurnPlayerId().equals(leavedPlayer);

                    /* Remove player on model side */
                    this.game.removePlayer(leavedPlayer);

                    /* If model signal SUSPENDED status then run a timer */
                    if (this.game.getGameStatus() == Game.GameStatus.SUSPENDED) {
                        new Thread(() -> {
                            /* Timer wait 6 seconds for a player to rejoin */
                            this.lobbyLock.reset(false);
                            if (!this.lobbyLock.hasBeenUnlocked())
                                try {
                                    this.lobbyLock.lock(6000);
                                } catch (InterruptedException e) {
                                    System.err.println("GameController: Timer is not working as intended");
                                }

                            /* If nobody has rejoined then close the game */
                            if (!this.lobbyLock.getValue())
                                this.closeTheGame("Game closed due to reconnection timeout");
                            else
                            {
                                if (skipNeeded) {
                                    try {
                                        this.game.updatePlayersTurn();
                                    } catch (GameEndedException e) {
                                        /* If skipping the turn makes game end, then close the game */
                                        this.closeTheGame("Game Ended");
                                    }
                                }
                            }
                        }).start();
                    }
                    /* If a skip was needed then skip the turn */
                    else if (skipNeeded) {
                        try {
                            this.game.updatePlayersTurn();
                        } catch (GameEndedException e) {
                            /* If skipping the turn makes game end, then close the game */
                            this.closeTheGame("Game Ended");
                        }
                    }
                } catch (PlayerNotExistsException e) {
                    this.printModelError("Player should exists but does not exists, skipping player deletion...");
                }
            }
        }
    }

    /**
     * This method permanently closes the game, detaches all the clients and calls the gameClosed callback, sends the given message to all connected users
     *
     * @param message The message that needs to be sent to the connected users
     */
    private void closeTheGame(String message) {
        /* Reports to all connected user they left the game */
        for (User u : this.userAssociation.values()) {
            u.reportEvent(User.Status.NOT_JOINED, message, User.Event.GAME_LEAVED);
        }

        /* Remove all allowed user */
        this.userAssociation.clear();

        /* Signal the model to definitely end the game */
        this.game.close();
        /* Detach all the observers from the model */
        this.game.deleteObservers();
        this.game.getCommonGoals().forEach(Observable::deleteObservers);
        this.game.getLivingRoom().deleteObservers();
        for (String playerId : this.playerAssociation.values()) {
            try {
                Player p = this.game.getPlayer(playerId);
                p.deleteObservers();
                p.getPlayerChat().deleteObservers();
                p.getPersonalGoals().forEach(Observable::deleteObservers);
                p.getShelf().deleteObservers();
            } catch (PlayerNotExistsException e) {
                this.printModelError("Player should exists but does not exists, skipping observer deletion....");
            }
        }

        /* Calls gameClosedCallback to signal this controller is no longer useful */
        this.gameClosedCallback.accept(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param client     {@inheritDoc}
     * @param playerMove {@inheritDoc}
     */
    public synchronized void doPlayerMove(ClientInterface client, PlayerMoveInfo playerMove) {
        /* Check if client is allowed */
        if (!this.userAssociation.containsKey(client))
            System.err.println("GameController: Command from unauthenticated client");
        else {
            /* get id associated with allowed client */
            String playerId = this.playerAssociation.get(this.userAssociation.get(client));

            try {
                /* Get player information associated with his client */
                Player player = this.game.getPlayer(playerId);

                if (this.game.getGameStatus() != Game.GameStatus.STARTED)
                    /* If game is not started we discard the request */
                    player.reportError("Game not started yet");

                else if (!playerId.equals(this.game.getTurnPlayerId()))
                    /* If it isn't player turn we discard the request*/
                    player.reportError("Not your turn");

                else {
                    /* Get board and player shelf status */
                    Tile[][] shelf = player.getShelf().getTiles();
                    Tile[][] board = this.game.getLivingRoom().getBoard();

                    try {
                        /* Check if move object is well-formed and contains coherent info */
                        this.checkWellFormedMove(playerMove, shelf, board);

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

                        /* If player shelf has been filled we signal that this will be the last round of turns */
                        if (this.evaluateFullShelf(shelf)) {
                            /* If nobody else has already completed the shelf we assign a "GAME_END" token */
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

                        /* Pass turn to next player */
                        try {
                            this.game.updatePlayersTurn();
                        } catch (GameEndedException e) {
                            /* If skipping the turn makes game end, then close the game */
                            this.closeTheGame("Game Ended");
                        }

                    } catch (IllegalArgumentException e) {
                        player.reportError("Malformed move: " + e.getMessage());
                    }
                }
            } catch (PlayerNotExistsException ex) {
                this.printModelError("Player should exists but does not exists, skipping player interaction...");
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param client     {@inheritDoc}
     * @param newMessage {@inheritDoc}
     */
    public synchronized void sendMessage(ClientInterface client, Message newMessage) {
        /* Check if client is allowed */
        if (!this.userAssociation.containsKey(client))
            System.err.println("GameController: Command from unauthenticated client");
        else {
            /* get playerId associated with allowed client */
            String senderId = this.playerAssociation.get(this.userAssociation.get(client));
            try {
                /* get player object associated with senderId */
                Player sender = this.game.getPlayer(senderId);

                /* Checks if client and message sender info are coherent*/
                if (!newMessage.getSender().equals(senderId))
                    sender.reportError("Sender of the message is not the same of message info");

                    /* If subject is not a player of this game then send an error */
                else if (!(newMessage.getReceiver().isEmpty() || this.playerAssociation.containsValue(newMessage.getReceiver())))
                    sender.reportError("Subject of the message does not exists");

                    /* If subject is a player or all players send message */
                else {
                    for (String playerId : this.playerAssociation.values()) {
                        /* Send the message to all if receiver is empty or only to sender or receiver if specified */
                        if (newMessage.getReceiver().isEmpty() || newMessage.getReceiver().equals(playerId) || newMessage.getSender().equals(playerId))
                            this.game.getPlayer(playerId).getPlayerChat().addMessage(newMessage);
                    }
                }
            } catch (PlayerNotExistsException e) {
                this.printModelError("Player should exists but does not exists, skipping message sending");
            }
        }
    }

    /**
     * This method checks if the given move is legal or illegal
     *
     * @param playerMove The move that needs to be checked
     * @param shelf      The player shelf the move is referred
     * @param board      The living room board the move is referred
     * @throws IllegalArgumentException If move is illegal
     */
    private void checkWellFormedMove(PlayerMoveInfo playerMove, Tile[][] shelf, Tile[][] board) throws IllegalArgumentException {
        /* Do some checks on "move" object, if malformed we discard it */
        if (playerMove == null || playerMove.pickedTiles() == null || playerMove.pickedTiles().size() == 0)
            throw new IllegalArgumentException("Malformed move object");

        if (playerMove.columnToInsert() < 0 || playerMove.columnToInsert() > shelf[0].length - 1)
            throw new IllegalArgumentException("Column index out of bounds");

        if (!this.areTilesDifferent(new ArrayList<>(playerMove.pickedTiles())))
            throw new IllegalArgumentException("Tiles are not different");

        if (!this.areTilesAligned(new ArrayList<>(playerMove.pickedTiles())))
            throw new IllegalArgumentException("Tiles are not aligned");

        for (PickedTile tile : playerMove.pickedTiles())
            if (!this.isTilePickable(tile.row(), tile.col(), board))
                throw new IllegalArgumentException("Tile not pickable");

        if (playerMove.pickedTiles().size() > this.freeShelfColumnSpaces(playerMove.columnToInsert(), shelf))
            throw new IllegalArgumentException("Not enough space to insert tiles in shelf");
    }


    /**
     * This method checks whether the picked tiles are the same or not
     *
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(List<PickedTile> pickedTiles) {
        for (int i = 0; i < pickedTiles.size(); i++) {
            for (int j = i + 1; j < pickedTiles.size(); j++) {
                if (pickedTiles.get(i).row() == pickedTiles.get(j).row())
                    if (pickedTiles.get(i).col() == pickedTiles.get(j).col())
                        return false;
            }
        }
        return true;
    }


    /**
     * Given a column of a player shelf this method counts the number of free spaces
     *
     * @param column column to check
     * @param shelf  player shelf
     * @return number of free spaces in the column
     */
    private int freeShelfColumnSpaces(int column, Tile[][] shelf) {
        int spaces = 0;
        for (Tile[] row : shelf) {
            if (row[column] == Tile.EMPTY)
                spaces++;
        }
        return spaces;
    }

    /**
     * This method inserts the picked tiles in the shelf
     *
     * @param column column to check
     * @param shelf  player shelf
     * @param tile   tile to insert
     */
    private void insertTileInShelf(int column, Tile[][] shelf, Tile tile) {
        int row = shelf.length - 1;
        while (shelf[row][column] != Tile.EMPTY)
            row--;

        shelf[row][column] = tile;
    }

    /**
     * This method checks whether the selected tile is pickable or not
     *
     * @param row    row of the tile
     * @param column column of the tile
     * @param board  board to check
     * @return true if tile is pickable, false otherwise
     */
    private boolean isTilePickable(int row, int column, Tile[][] board) {
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
     * This method checks whether the shelf is full or not
     *
     * @param shelf shelf to check
     * @return true if shelf is full, false otherwise
     */
    private boolean evaluateFullShelf(Tile[][] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile == Tile.EMPTY)
                    return false;

        return true;
    }

    /**
     * This method checks whether the picked tiles are aligned or not
     *
     * @param pickedTiles Tiles picked from board
     * @return true if picked tiles are aligned ad adjacent one another
     */
    private boolean areTilesAligned(List<PickedTile> pickedTiles) {
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

    /**
     * This method prints an error on server console if an error occurred on model manipulation
     *
     * @param message The message that needs to be print
     */
    private void printModelError(String message) {
        System.err.println("GameController: ModelError: " + message);
    }
}
