package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.LobbyController;
import distibuted.interfaces.ClientInterface;
import model.*;
import model.abstractModel.*;
import model.exceptions.*;
import modelView.*;
import util.Observer;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.BiConsumer;

public class StandardGameController implements GameController, LobbyController {
    private final Game game;
    private final Map<ClientInterface, Player> playerAssociation;

    /**
     * @param game game to be managed
     */
    public StandardGameController(Game game) {
        this.game = game;
        this.playerAssociation = new HashMap<>();
    }


    ///LOBBY CONTROLLER/////////////////////
    /**
     * Add player to the game with all necessary preparations
     * @param newClient client that wants to join the game
     * @param playerId id of the player that wants to join the game
     * @throws GameAccessDeniedException if the game is already ended or the player id already exists or the matchmaking is closed
     */
    public synchronized void joinPlayer(ClientInterface newClient, String playerId) throws GameAccessDeniedException {
        try {
            game.addPlayer(playerId);

            Player newPlayer = game.getPlayer(playerId);

            addObservers(newClient, newPlayer);

            /* Put newClient into known client */
            playerAssociation.put(newClient, newPlayer);



            /* If game is ready to be started we force the first */
            if (game.getGameStatus().equals(Game.GameStatus.STARTED)) {
                game.getLivingRoom().refillBoard();
                game.updatePlayersTurn();
            }

        } catch (GameEndedException e) {
            throw new GameAccessDeniedException("Game already ended");
        } catch (PlayerAlreadyExistsException | PlayerNotExistsException e) {
            throw new GameAccessDeniedException("Player id already exists");
        } catch (MatchmakingClosedException e) {
            throw new GameAccessDeniedException("Game matchmaking closed");
        }
    }


    //should we rename newClient to client?
    /**
     * Add observers to all needed objects
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private void addObservers(ClientInterface newClient, Player newPlayer) {
        /* Add Player status observer */
        addPlayerObserver(newClient, newPlayer);

        /* Add Game status observer */
        addGameObserver(newClient);

        /* Add CommonGoal status observers */
        addCommonGoalsObservers(newClient);

        /* Add LivingRoom status observer */
        addLivingRoomObserver(newClient);

        /* Add PlayerChat status observer */
        addPlayerChatObserver(newClient, newPlayer);

        /* Add PersonalGoals status observer */
        addPersonalGoalsObservers(newClient, newPlayer);

        /* Add Shelf status observer */
        addPlayerShelfObserver(newClient, newPlayer);
        /* Add Shelf status observer of new player to all already joined players */
        /* Add Shelf status observer of all already joined players to new player */
        playerAssociation.forEach((joinedClient, joinedPlayer) -> {
            addPlayerShelfObserver(joinedClient,newPlayer);
            addPlayerShelfObserver(newClient, joinedPlayer);
        });
    }

    /**
     * Add observer to the player Shelf
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private static void addPlayerShelfObserver(ClientInterface newClient, Player newPlayer) {
        newPlayer.getShelf().addObserver(
            (Observer<Shelf, Shelf.Event>) (o, arg) -> {
                try {
                    newClient.update(new ShelfInfo(newPlayer.getId(), o.getTiles()), arg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    /**
     * Add observer to the personal goals of the new player
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private static void addPersonalGoalsObservers(ClientInterface newClient, Player newPlayer) {
        for (PersonalGoal personalGoal : newPlayer.getPersonalGoals()) {
            personalGoal.addObserver(
                (Observer<PersonalGoal, PersonalGoal.Event>) (o, arg) -> {
                    try {
                        newClient.update(new PersonalGoalInfo(o.isAchieved(), o.getDescription()), arg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
        }
    }

    /**
     * Add observer to the new player's PlayerChat
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private static void addPlayerChatObserver(ClientInterface newClient, Player newPlayer) {
        newPlayer.getPlayerChat().addObserver(
            (Observer<PlayerChat, PlayerChat.Event>) (o, arg) -> {
                try {
                    newClient.update(new PlayerChatInfo(o.getMessages()), arg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    /**
     * Add observer to the Player status
     * @param newClient new player's ClientInterface
     */
    private void addLivingRoomObserver(ClientInterface newClient) {
        game.getLivingRoom().addObserver(
            (Observer<LivingRoom, LivingRoom.Event>) (o, arg) -> {
                try {
                    newClient.update(new LivingRoomInfo(o.getBoard()), arg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    /**
     * Add observer to the CommonGoals of the game
     * @param newClient new player's ClientInterface
     */
    private void addCommonGoalsObservers(ClientInterface newClient) {
        for (CommonGoal goal : game.getCommonGoals()) {
            goal.addObserver(
                (Observer<CommonGoal, CommonGoal.Event>) (o, arg) -> {
                    try {
                        newClient.update(new CommonGoalInfo(o.getEvaluator().getDescription(), o.getTopToken()), arg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
        }
    }

    /**
     * Add observer to the Game status
     * @param newClient new player's ClientInterface
     */
    private void addGameObserver(ClientInterface newClient) {
        game.addObserver(
            (Observer<Game, Game.Event>) (o, arg) -> {
                try {
                    newClient.update(new GameInfo(o.getGameStatus(), o.isLastTurn(), o.getTurnPlayerId()), arg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    /**
     * Add observer to the Player Info
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private static void addPlayerObserver(ClientInterface newClient, Player newPlayer) {
        newPlayer.addObserver(
            (Observer<Player, Player.Event>) (o, arg) -> {
                try {
                    newClient.update(new PlayerInfo(o.getReportedError(), new HashMap<>(o.getAchievedCommonGoals())), arg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    /**
     * Execute a player move
     * @param client new player's ClientInterface
     * @param playerMove player move to execute
     */
    public synchronized void doPlayerMove(ClientInterface client, PlayerMoveInfo playerMove) {
        try {
            /* If we receive a request from an invalid client we discard it */
            if (!playerAssociation.containsKey(client))
                throw new IllegalArgumentException("User not allowed");

            /* If game is not started we discard the request*/
            if (!game.getGameStatus().equals(Game.GameStatus.STARTED))
                throw new GameNotStartedException();

            /* Get player information associated with his client */
            Player player = playerAssociation.get(client);

            /* If it isn't player turn we discard the request*/
            if (!player.getId().equals(game.getTurnPlayerId()))
                throw new IllegalArgumentException("Not player's turn");

            /* Get board and player shelf status */
            Tile[][] shelf = player.getShelf().getTiles();
            Tile[][] board = game.getLivingRoom().getBoard();

            /* Do some checks on "move" object, if malformed we discard it */
            if (playerMove == null || playerMove.pickedTiles() == null || playerMove.pickedTiles().size() == 0)
                throw new IllegalArgumentException("Malformed move object");

            if (playerMove.columnToInsert() < 0 || playerMove.columnToInsert() > shelf[0].length - 1)
                throw new IllegalArgumentException("Column index out of bounds");

            if (!areTilesDifferent(playerMove.pickedTiles()))
                throw new IllegalArgumentException("Tiles are not different");

            //TODO controllo che le tile scelte siano in linea

            for (PickedTile tile : playerMove.pickedTiles())
                if (!isTilePickable(tile.row(), tile.col(), board))
                    throw new IllegalArgumentException("Tile not pickable");

            if (playerMove.pickedTiles().size() > freeShelfColumnSpaces(playerMove.columnToInsert(), shelf))
                throw new IllegalArgumentException("Not enough space to insert tiles in shelf");

            /* Foreach tile we pick it from board and put it on the shelf */
            for (PickedTile tile : playerMove.pickedTiles()) {
                Tile picked = board[tile.row()][tile.col()];
                board[tile.row()][tile.col()] = Tile.EMPTY;
                insertTileInShelf(playerMove.columnToInsert(), shelf, picked);
            }

            /* Update board and player shelf status*/
            game.getLivingRoom().setBoard(board);
            game.getLivingRoom().refillBoard();
            player.getShelf().setTiles(shelf);

            /* If shelf has been filled we signal that this will be the last round of turns */
            if (evaluateFullShelf(shelf)) {
                /* If nobody else has already completed the shelf we assign a "GAME_END" token*/
                if (!game.isLastTurn())
                    player.addAchievedCommonGoal("First player that have completed the shelf", Token.TOKEN_GAME_END);

                game.setLastTurn();
            }
            /* Check if player has achieved common goals */
            for (CommonGoal commonGoal : game.getCommonGoals())
                if (!player.getAchievedCommonGoals().containsKey(commonGoal.getEvaluator().getDescription()))
                    if (commonGoal.getEvaluator().evaluate(shelf))
                        player.addAchievedCommonGoal(commonGoal.getEvaluator().getDescription(), commonGoal.popToken());

            /* Pass turn to next player */
            game.updatePlayersTurn();

        } catch (IllegalArgumentException | GameNotStartedException | GameEndedException e) {
            playerAssociation.get(client).reportError(e.getMessage());
        }
    }

    /**
     * @param client new player's ClientInterface
     * @param newMessage message to send
     */
    public synchronized void sendMessage(ClientInterface client, Message newMessage) {
        try {
            if (!playerAssociation.containsKey(client))
                throw new IllegalArgumentException("User not allowed");

            for (Player p : playerAssociation.values()) {
                if (newMessage.getSubject().isEmpty() || newMessage.getSubject().equals(p.getId()))
                    p.getPlayerChat().addMessage(newMessage);
            }

        } catch (IllegalArgumentException e) {
            playerAssociation.get(client).reportError(e.getMessage());
        }
    }

    /**
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(List<PickedTile> pickedTiles) {
        for (int i = 0; i < pickedTiles.size() - 2; i++) {
            for (int j = i + 1; j < pickedTiles.size() - 1; j++) {
                if (pickedTiles.get(i).row() == pickedTiles.get(j).row())
                    if (pickedTiles.get(i).col() == pickedTiles.get(j).col())
                        return false;
            }
        }
        return true;
    }

    /**
     * @param column column to check
     * @param shelf player shelf
     * @return number of free spaces in the column
     */
    private int freeShelfColumnSpaces(int column, Tile[][] shelf) {
        int spaces = 0;
        for (Tile[] row : shelf) {
            if (row[column] == null)// || row[column].equals(Tile.EMPTY))//TODO decidersi se null o EMPTY
                spaces++;
        }
        return spaces;
    }

    /**
     * @param column column to check
     * @param shelf player shelf
     * @param tile tile to insert
     */
    private void insertTileInShelf(int column, Tile[][] shelf, Tile tile) {
        int row = shelf.length - 1;
        while (shelf[row][column] != null)// || !shelf[row][column].equals(Tile.EMPTY)) //TODO null or EMPTY
            row--;

        shelf[row][column] = tile;
    }

    /**
     * @param row row of the tile
     * @param column column of the tile
     * @param board board to check
     * @return true if tile is pickable, false otherwise
     */
    private boolean isTilePickable(int row, int column, Tile[][] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column].equals(Tile.EMPTY) || board[row][column] == null)
            return false;

        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        return board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY
                || board[row - 1][column] == null //TODO decidere se lasciare null o EMPTY
                || board[row + 1][column] == null
                || board[row][column - 1] == null
                || board[row][column + 1] == null;
    }

    /**
     * @param shelf shelf to check
     * @return true if shelf is full, false otherwise
     */
    private boolean evaluateFullShelf(Tile[][] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile == Tile.EMPTY)//tile.equals(Tile.EMPTY))//TODO decidere null or EMPTY
                    return false;

        return true;
    }
}
