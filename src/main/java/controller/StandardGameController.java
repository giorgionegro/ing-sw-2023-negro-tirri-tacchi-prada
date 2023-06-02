package controller;

import controller.exceptions.GameAccessDeniedException;
import controller.interfaces.GameController;
import controller.interfaces.LobbyController;
import distibuted.interfaces.ClientInterface;
import model.*;
import model.abstractModel.*;
import model.exceptions.*;
import modelView.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Observable;
import util.Observer;

import java.rmi.RemoteException;
import java.util.*;

public class StandardGameController implements GameController, LobbyController {
    private final Game game;
    private final @NotNull Map<ClientInterface, User> userAssociation;
    private final @NotNull Map<User,String> playerAssociation;

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
     * @param newClient client to be added
     * @return Observer of the Shelf to be added
     */

    private @NotNull Observer<Shelf, Shelf.Event> getShelfObserver(@NotNull ClientInterface newClient, @NotNull String joinedPlayerId) {
        return new Observer<>() {
            @Override
            public void update(@NotNull Shelf o, Shelf.Event arg) {
                try {
                    newClient.update(o.getInfo(joinedPlayerId), arg);
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
     * @param info  login info of the player
     * @throws GameAccessDeniedException if the game is already ended or the player id already exists or the matchmaking is closed
     */
    @Override
    public synchronized void joinPlayer(@NotNull ClientInterface newClient, @NotNull User newUser, @NotNull LoginInfo info) throws GameAccessDeniedException {
        //Should we just Throw the different exceptions instead of catching them?
        try {
            game.addPlayer(info.playerId());

            userAssociation.put(newClient,newUser);

            /* Put newClient into known client */
            playerAssociation.put(newUser, info.playerId());

            newUser.reportEvent(User.Status.JOINED, "Joined game: "+info.gameId()+", you are:"+info.playerId(), info.time(), User.Event.GAME_JOINED);

            addObservers(newClient, info.playerId());

            /* If game is ready to be started we force the first */
            if (game.getGameStatus().equals(Game.GameStatus.STARTED)) {
                game.getLivingRoom().refillBoard();
                game.updatePlayersTurn();
            }
        } catch (PlayerAlreadyExistsException e) {
            throw new GameAccessDeniedException("Player id already exists");//Same as above
        } catch (PlayerNotExistsException e){
            throw new RuntimeException("Model is not working as intended");
        } catch (MatchmakingClosedException | GameEndedException e) {
            throw new GameAccessDeniedException("Game matchmaking closed"); //Same as above
        }
    }

    private void closeTheGame(String message){
        long eventTime = System.currentTimeMillis();
        for(User u : userAssociation.values()){
            u.reportEvent(User.Status.NOT_JOINED,message,eventTime, User.Event.GAME_LEAVED);
        }

        game.close();
        game.deleteObservers();
        game.getCommonGoals().forEach(Observable::deleteObservers);
        game.getLivingRoom().deleteObservers();
        for(String playerId : playerAssociation.values()){
            try {
                Player p = game.getPlayer(playerId);
                p.deleteObservers();
                p.getPlayerChat().deleteObservers();
                p.getPersonalGoals().forEach(Observable::deleteObservers);
                p.getShelf().deleteObservers();
            }catch (PlayerNotExistsException e) {
                throw new RuntimeException(e); //TODO
            }
        }
    }

    @Override
    public synchronized void leavePlayer(ClientInterface client) throws GameAccessDeniedException{
        if(!userAssociation.containsKey(client))
            throw new GameAccessDeniedException("Client not connected to this game");

        String leavedPlayer = playerAssociation.get(userAssociation.get(client));
        String eventMessage = leavedPlayer +" has leaved the game";
        closeTheGame(eventMessage);

        System.err.println("PLAYER USCITO");
    }

    /**
     * Add observers to all needed objects
     *
     * @param newClient new player's ClientInterface
     * @param newPlayerId new player's id
     */
    private void addObservers(@NotNull ClientInterface newClient, @NotNull String newPlayerId) throws PlayerNotExistsException {

        Player newPlayer = game.getPlayer(newPlayerId);
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
        newPlayer.getShelf().addObserver(getShelfObserver(newClient, newPlayerId));
        /* Add Shelf status observer of new player to all already joined players */
        /* Add Shelf status observer of all already joined players to new player */
        for(Map.Entry<ClientInterface,User> association : userAssociation.entrySet()){
            newPlayer.getShelf().addObserver(getShelfObserver(association.getKey(), newPlayerId));

            String joinedPlayerId = playerAssociation.get(association.getValue());
            game.getPlayer(joinedPlayerId).getShelf().addObserver(getShelfObserver(newClient, joinedPlayerId));
        }
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
            if (!userAssociation.containsKey(client))
                throw new GameAccessDeniedException("User not allowed");
            /* If game is not started we discard the request*/
            if (!game.getGameStatus().equals(Game.GameStatus.STARTED))
                throw new GameNotStartedException();
            /* Get player information associated with his client */
            String playerId = playerAssociation.get(userAssociation.get(client));
            Player player = game.getPlayer(playerId);
            /* If it isn't player turn we discard the request*/
            if (!playerId.equals(game.getTurnPlayerId()))
                throw new IllegalArgumentException("Not player's turn");
            /* Get board and player shelf status */

            Tile[][] shelf = player.getShelf().getTiles();
            Tile[][] board = game.getLivingRoom().getBoard();

            /* Do some checks on "move" object, if malformed we discard it */
            if (playerMove == null || playerMove.pickedTiles() == null || playerMove.pickedTiles().size() == 0)
                throw new IllegalArgumentException("Malformed move object");

            if (playerMove.columnToInsert() < 0 || playerMove.columnToInsert() > shelf[0].length - 1)
                throw new IllegalArgumentException("Column index out of bounds");

            if (!areTilesDifferent(new ArrayList<>(playerMove.pickedTiles())))
                throw new IllegalArgumentException("Tiles are not different");

            if(!areTilesAligned(new ArrayList<>(playerMove.pickedTiles())))
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
                if (!player.getAchievedCommonGoals().containsKey(commonGoal.getEvaluator().getId()))
                    if (commonGoal.getEvaluator().evaluate(shelf)) {
                        player.addAchievedCommonGoal(commonGoal.getEvaluator().getId(), commonGoal.popToken());
                    }


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
            try{
                String playerId = playerAssociation.get(userAssociation.get(client));
                game.getPlayer(playerId).reportError(e.getMessage());
            } catch (PlayerNotExistsException ex) {
                throw new RuntimeException("Model is not working as intended");//TODO severe error
            }
        } catch (GameEndedException e){
            e.printStackTrace();
        } catch (GameAccessDeniedException | PlayerNotExistsException e) {
            System.err.println("COMMAND FROM UNAUTHENTICATED USER");
        }
    }

    /**
     * Send a message to players included in the message subject
     * @param client     new player's ClientInterface
     * @param newMessage message to send
     */
    public synchronized void sendMessage(ClientInterface client, @NotNull Message newMessage) {
        try {
            if (!userAssociation.containsKey(client))
                throw new GameAccessDeniedException("User not allowed");

            if(!(newMessage.getSubject().isEmpty() || playerAssociation.containsValue(newMessage.getSubject())))
                throw new IllegalArgumentException("Subject of the message does not exists");

            for (String playerId : playerAssociation.values()) {
                if (newMessage.getSubject().isEmpty() || newMessage.getSubject().equals(playerId) || newMessage.getSender().equals(playerId))
                    game.getPlayer(playerId).getPlayerChat().addMessage(newMessage);
            }

        } catch (IllegalArgumentException e) {
            try{
                String playerId = playerAssociation.get(userAssociation.get(client));
                game.getPlayer(playerId).reportError(e.getMessage());
            } catch (PlayerNotExistsException ex) {
                throw new RuntimeException("Model is not working as intended");//TODO severe error
            }
        } catch (GameAccessDeniedException e) {
            System.err.println("COMMAND FROM UNAUTHENTICATED USER");
        } catch (PlayerNotExistsException e) {
            throw new RuntimeException("Model is not working as instended");
        }
    }


    //do we need javadoc for private methods?
    /**
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(@NotNull List<PickedTile> pickedTiles) {
        for (int i = 0; i < pickedTiles.size() ; i++) {
            for (int j = i + 1; j < pickedTiles.size() ; j++) {
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
    private boolean evaluateFullShelf(Tile[] @NotNull [] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile == Tile.EMPTY)
                    return false;

        return true;
    }

    private boolean areTilesAligned(@NotNull List<PickedTile> pickedTiles){
        boolean rowAligned = true;
        boolean colAligned = true;

        for(int i = 1; i<pickedTiles.size(); i++){
            rowAligned = rowAligned && (pickedTiles.get(i-1).row() == pickedTiles.get(i).row());
            colAligned = colAligned && (pickedTiles.get(i-1).col() == pickedTiles.get(i).col());
        }
        if(rowAligned){
            pickedTiles.sort(Comparator.comparingInt(PickedTile::col));
            for(int i=0; i< pickedTiles.size()-1; i++)
                if(pickedTiles.get(i).col()+1!=pickedTiles.get(i+1).col())
                    return false;
        }
        if(colAligned){
            pickedTiles.sort(Comparator.comparingInt(PickedTile::row));
            for(int i=0; i< pickedTiles.size()-1; i++)
                if(pickedTiles.get(i).row()+1!=pickedTiles.get(i+1).row())
                    return false;
        }


        return rowAligned || colAligned;
    }
}
