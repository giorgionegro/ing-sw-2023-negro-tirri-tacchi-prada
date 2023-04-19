package controller;

import controller.exceptions.ClientNotAllowedException;
import controller.exceptions.MalformedPlayerMoveException;
import controller.interfaces.GameController;
import distibuted.interfaces.ClientInterface;
import distibuted.networkObservers.*;
import model.*;
import model.abstractModel.*;
import model.exceptions.*;
import modelView.*;
import java.util.*;

public class StandardGameController implements GameController {
    private final Game game;
    private final Map<ClientInterface, Player> playerAssociation;

    public StandardGameController(Game game) {
        this.game = game;
        this.playerAssociation = new HashMap<>();
    }

    public synchronized GameController.Event join(ClientInterface newClient, String playerId) {
        try {
            game.addPlayer(playerId);

            Player newPlayer = game.getPlayer(playerId);

            newPlayer.addObserver(new PlayerNetworkObserver(newClient));

            /* Add Game status observer */
            game.addObserver(new GameNetworkObserver(newClient));

            /* Add CommonGoal status observers */
            for (CommonGoal goal : game.getCommonGoals()) {
                goal.addObserver(new CommonGoalNetworkObserver(newClient));
            }

            /* Add LivingRoom status observer */
            game.getLivingRoom().addObserver(new LivingRoomNetworkObserver(newClient));

            /* Add PlayerChat status observer */
            newPlayer.getPlayerChat().addObserver(new PlayerChatNetworkObserver(newClient));

            /* Add PersonalGoals status observer */
            for (PersonalGoal personalGoal : newPlayer.getPersonalGoals()) {
                personalGoal.addObserver(new PersonalGoalNetworkObserver(newClient));
            }

            /* Add Shelf status observer */
            newPlayer.getShelf().addObserver(new ShelfNetworkObserver(newClient, playerId));

            /* Add Shelf status observer of new player to all already joined players */
            /* Add Shelf status observer of all already joined players to new player */
            playerAssociation.forEach((joinedClient, joinedPlayer) -> {
                joinedPlayer.getShelf().addObserver(new ShelfNetworkObserver(newClient, joinedPlayer.getId()));
                newPlayer.getShelf().addObserver(new ShelfNetworkObserver(joinedClient, playerId));
            });

            /* Put newClient into known client */
            playerAssociation.put(newClient, newPlayer);

            /* If game is ready to be started we force the first */
            if (game.getGameStatus().equals(Game.GameStatus.STARTED)) {
                game.getLivingRoom().refillBoard();
                game.updatePlayersTurn();
            }

            return Event.JOINED;
        } catch (PlayerAlreadyExistsException | PlayerNotExistsException | MatchmakingClosedException e) {
            return Event.NOT_JOINED;
        } catch (GameEndedException e) {
            throw new RuntimeException(e); //TODO routine di fine partita
        }
    }

    public synchronized void doPlayerMove(ClientInterface client, PlayerMoveInfo move) {
        try {
            /* If we receive a request from an invalid client we discard it */
            if (!playerAssociation.containsKey(client))
                throw new ClientNotAllowedException();

            /* If game is not started we discard the request*/
            if (!game.getGameStatus().equals(Game.GameStatus.STARTED))
                throw new GameNotStartedException();

            /* Get player information associated with his client */
            Player player = playerAssociation.get(client);

            /* If it isn't player turn we discard the request*/
            if (!player.getId().equals(game.getTurnPlayerId()))
                throw new ClientNotAllowedException();

            /* Get board and player shelf status */
            Tile[][] shelf = player.getShelf().getShelf();
            Tile[][] board = game.getLivingRoom().getBoard();

            /* Do some checks on "move" object, if malformed we discard it */
            if (move == null || move.getPickedTiles() == null || move.getPickedTiles().size() == 0)
                throw new MalformedPlayerMoveException("Malformed move object");

            if (move.getColumnToInsert() < 0 || move.getColumnToInsert() > shelf.length - 1)
                throw new MalformedPlayerMoveException("Column index out of bounds");

            if(!areTilesDifferent(move.getPickedTiles()))
                throw new MalformedPlayerMoveException("Tiles are not different");

            //TODO controllo che le tile scelte siano in linea

            for (PickedTile tile : move.getPickedTiles())
                if (!isTilePickable(tile.getRow(), tile.getCol(), board))
                    throw new MalformedPlayerMoveException("Tile not pickable");

            if (move.getPickedTiles().size() > freeShelfColumnSpaces(move.getColumnToInsert(), shelf))
                throw new MalformedPlayerMoveException("Not enough space to insert tiles in shelf");

            /* Foreach tile we pick it from board and put it on the shelf */
            for (PickedTile tile : move.getPickedTiles()) {
                Tile picked = board[tile.getRow()][tile.getCol()];
                board[tile.getRow()][tile.getCol()] = Tile.EMPTY;
                insertTileInShelf(move.getColumnToInsert(), shelf, picked);
            }

            /* Update board and player shelf status*/
            game.getLivingRoom().setBoard(board);
            player.getShelf().setShelf(shelf);

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

        } catch (MalformedPlayerMoveException e) {
            playerAssociation.get(client).notifyObservers(Player.PlayerEvent.MALFORMED_MOVE);
        } catch (ClientNotAllowedException e) {
            playerAssociation.get(client).notifyObservers(Player.PlayerEvent.NOT_ALLOWED);
        } catch (GameNotStartedException e) {
            playerAssociation.get(client).notifyObservers(Player.PlayerEvent.GAME_NOT_STARTED);
        } catch (GameEndedException e) {
            throw new RuntimeException(e);//TODO
        }
    }

    public synchronized void sendMessage(ClientInterface client, Message newMessage) {
        try {
            if (!playerAssociation.containsKey(client))
                throw new ClientNotAllowedException();

            for (Player p : playerAssociation.values()) {
                if (newMessage.getSubject().isEmpty() || newMessage.getSubject().equals(p.getId()))
                    p.getPlayerChat().addMessage(newMessage);
            }

        } catch (ClientNotAllowedException e) {
            playerAssociation.get(client).notifyObservers(Player.PlayerEvent.NOT_ALLOWED);
        }
    }

    private boolean areTilesDifferent(List<PickedTile> pickedTiles) {
        for(int i=0; i<pickedTiles.size()-2; i++){
            for(int j=i+1; j<pickedTiles.size()-1; j++){
                if(pickedTiles.get(i).getRow()==pickedTiles.get(j).getRow())
                    if(pickedTiles.get(i).getCol()==pickedTiles.get(j).getCol())
                        return false;
            }
        }
        return true;
    }

    private int freeShelfColumnSpaces(int column, Tile[][] shelf) {
        int spaces = 0;
        for (Tile[] row : shelf) {
            if (row[column]== null)// || row[column].equals(Tile.EMPTY))//TODO decidersi se null o EMPTY
                spaces++;
        }
        return spaces;
    }

    private void insertTileInShelf(int column, Tile[][] shelf, Tile tile) {
        int row = shelf.length - 1;
        while (shelf[row][column]!=null)// || !shelf[row][column].equals(Tile.EMPTY)) //TODO null or EMPTY
            row--;

        shelf[row][column] = tile;
    }

    private boolean isTilePickable(int row, int column, Tile[][] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column].equals(Tile.EMPTY) || board[row][column] == null)
            return false;

        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        if (board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY
                || board[row - 1][column] == null //TODO decidere se lasciare null o EMPTY
                || board[row + 1][column] == null
                || board[row][column - 1] == null
                || board[row][column + 1] == null)
            return true;

        return false;
    }

    private boolean evaluateFullShelf(Tile[][] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile == null)//tile.equals(Tile.EMPTY))//TODO decidere null or EMPTY
                    return false;

        return true;
    }
}
