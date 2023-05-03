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
     * Observer to update the PlayerInfo
     * @param newClient client to be added
     * @return Player's Observer to be added
     */
    private static Observer<Player, Player.Event> getPlayerObserver(ClientInterface newClient) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }


    //should we rename newClient to client?

    /**
     * Observer to update the GameStatus
     * @param newClient client to be added
     * @return Observer of the GameStatus to be added
     */

    private static Observer<Game, Game.Event> getGameObserver(ClientInterface newClient) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Observer to update the CommonGoal
     * @param newClient client to be added
     * @return Observer of the CommonGoal to be added
     */

    private static Observer<CommonGoal, CommonGoal.Event> getCommonGoalObserver(ClientInterface newClient) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Observer to update the LivingRoom
     * @param newClient client to be added
     * @return Observer of the LivingRoom to be added
     */

    private static Observer<LivingRoom, LivingRoom.Event> getLivingRoomObserver(ClientInterface newClient) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Observer to update the PlayerChat
     * @param newClient client to be added
     * @return Observer of the PlayerChat to be added
     */

    private static Observer<PlayerChat, PlayerChat.Event> getPlayerChatObserver(ClientInterface newClient) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Observer to update the PersonalGoal
     * @param newClient client to be added
     * @return Observer of the PersonalGoal to be added
     */

    private static Observer<PersonalGoal, PersonalGoal.Event> getPersonalGoalObserver(ClientInterface newClient) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Observer to update the Shelf
     * @param newClient client to be added
     * @return Observer of the Shelf to be added
     */

    private static Observer<Shelf, Shelf.Event> getShelfObserver(ClientInterface newClient, Player joinedPlayer) {
        return (o, arg) -> {
            try {
                newClient.update(o.getInfo(joinedPlayer.getId()), arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Add player to the game with all necessary preparations
     *
     * @param newClient client that wants to join the game
     * @param playerId  id of the player that wants to join the game
     * @throws GameAccessDeniedException if the game is already ended or the player id already exists or the matchmaking is closed
     */
    public synchronized void joinPlayer(ClientInterface newClient, String playerId) throws GameAccessDeniedException {
        //Should we just Throw the different exceptions instead of catching them?
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
            throw new GameAccessDeniedException("Game already ended");//Should we throw a GameEndedException? it would be easier to handle in the client
        } catch (PlayerAlreadyExistsException | PlayerNotExistsException e) {
            throw new GameAccessDeniedException("Player id already exists");//Same as above
        } catch (MatchmakingClosedException e) {
            throw new GameAccessDeniedException("Game matchmaking closed"); //Same as above
        }
    }

    /**
     * Add observers to all needed objects
     *
     * @param newClient new player's ClientInterface
     * @param newPlayer new player's Player object
     */
    private void addObservers(ClientInterface newClient, Player newPlayer) {
        /* Add Player status observer */
        newPlayer.addObserver(getPlayerObserver(newClient));

        /* Add Game status observer */
        game.addObserver(getGameObserver(newClient));

        /* Add CommonGoal status observers */
        game.getCommonGoals().forEach(goal -> goal.addObserver(getCommonGoalObserver(newClient)));

        /* Add LivingRoom status observer */
        game.getLivingRoom().addObserver(getLivingRoomObserver(newClient));

        /* Add PlayerChat status observer */
        newPlayer.getPlayerChat().addObserver(getPlayerChatObserver(newClient));

        /* Add PersonalGoals status observer */
        newPlayer.getPersonalGoals().forEach(personalGoal -> personalGoal.addObserver(getPersonalGoalObserver(newClient)));

        /* Add Shelf status observer */
        newPlayer.getShelf().addObserver(getShelfObserver(newClient, newPlayer));
        /* Add Shelf status observer of new player to all already joined players */
        /* Add Shelf status observer of all already joined players to new player */
        playerAssociation.forEach((joinedClient, joinedPlayer) -> {
            newPlayer.getShelf().addObserver(getShelfObserver(joinedClient, newPlayer));
            joinedPlayer.getShelf().addObserver(getShelfObserver(newClient, joinedPlayer));
        });
    }

    /**
     * Execute a player move
     *
     * @param client     new player's ClientInterface
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

            if(!areTilesAligned(playerMove.pickedTiles()))
                throw new IllegalArgumentException("Tile are not aligned");

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


            /* Check if player has achieved personal goals*/
            for(PersonalGoal personalGoal : player.getPersonalGoals()){
                if(!personalGoal.isAchieved())
                    if(personalGoal.evaluate(shelf))
                        personalGoal.setAchieved();
            }


            //TODO salvare il gioco

            /* Pass turn to next player */
            game.updatePlayersTurn();

        } catch (IllegalArgumentException | GameNotStartedException e) {
            playerAssociation.get(client).reportError(e.getMessage());
        } catch (GameEndedException e){

        }
    }

    /**
     * Send a message to players included in the message subject
     * @param client     new player's ClientInterface
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


    //do we need javadoc for private methods?
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


    //Same as above
    /**
     * @param column column to check
     * @param shelf  player shelf
     * @return number of free spaces in the column
     */
    private int freeShelfColumnSpaces(int column, Tile[][] shelf) {
        int spaces = 0;
        for (Tile[] row : shelf) {
            if (row[column] != Tile.EMPTY)
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
    private void insertTileInShelf(int column, Tile[][] shelf, Tile tile) {
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
    private boolean isTilePickable(int row, int column, Tile[][] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column].equals(Tile.EMPTY) || board[row][column] == null)
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
    private boolean evaluateFullShelf(Tile[][] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile == Tile.EMPTY)
                    return false;

        return true;
    }

    private boolean areTilesAligned(List<PickedTile> pickedTiles){

        boolean rowAligned = true;
        boolean colAligned = true;
        for(int i = 1; i<pickedTiles.size(); i++){
            rowAligned = rowAligned && (pickedTiles.get(i-1).row() == pickedTiles.get(i).row());
            colAligned = colAligned && (pickedTiles.get(i-1).col() == pickedTiles.get(i).col());
        }

        return rowAligned || colAligned;
    }
}
