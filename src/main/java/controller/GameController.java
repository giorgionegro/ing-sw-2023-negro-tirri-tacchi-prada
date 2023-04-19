package controller;

import controller.exceptions.ClientNotAllowedException;
import controller.exceptions.MalformedPlayerMoveException;
import distibuted.interfaces.ClientInterface;
import model.*;
import model.abstractModel.*;
import model.exceptions.*;
import modelView.*;

import java.rmi.RemoteException;
import java.util.*;

public class GameController {
    private final Game game;
    private final Map<ClientInterface, Player> playerAssociation;

    public GameController(Game game) {
        this.game = game;
        this.playerAssociation = new HashMap<>();
    }

    public synchronized void join(ClientInterface newClient, String playerId) {
        try {
            game.addPlayer(playerId);

            Player newPlayer = game.getPlayer(playerId);

            /* Add Game status observer */
            game.addObserver((o, arg) -> {
                Game g = (Game) o;
                try {
                    newClient.updateGameStatus(new GameView(g.getGameStatus(), g.isLastTurn(), g.getTurnPlayerId()), arg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

            /* Add CommonGoal status observers */
            for (CommonGoal goal : game.getCommonGoals()) {
                goal.addObserver((o, arg) -> {
                    CommonGoal g = (CommonGoal) o;
                    try {
                        if (arg == null) {
                            newClient.updateCommonGoal(new CommonGoalView(g.getEvaluator().getDescription(), g.getTopToken()), arg);
                        } else {
                            switch (arg) {
                                case TOKEN_PICKED ->
                                        newClient.updateCommonGoal(new CommonGoalView(g.getEvaluator().getDescription(), g.getTopToken()), arg);
                                default ->
                                        newClient.updateCommonGoal(new CommonGoalView(g.getEvaluator().getDescription(), g.getTopToken()), arg);
                            }
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            /* Add LivingRoom status observer */
            game.getLivingRoom().addObserver((o, arg) -> {
                LivingRoom lR = (LivingRoom) o;
                try {
                    if (arg == null) {
                        newClient.updateLivingRoom(new LivingRoomView(lR.getBoard()), arg);
                    } else {
                        switch (arg) {
                            case BOARD_MODIFIED -> newClient.updateLivingRoom(new LivingRoomView(lR.getBoard()), arg);
                        }
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

            /* Add PlayerChat status observer */
            newPlayer.getPlayerChat().addObserver((o, arg) -> {
                PlayerChat pC = (PlayerChat) o;
                try {
                    newClient.updatePlayerChat(new PlayerChatView(pC), arg); //TODO switch with event
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

            /* Add PersonalGoals status observer */
            for (PersonalGoal personalGoal : newPlayer.getPersonalGoals()) {
                personalGoal.addObserver((o, arg) -> {
                    PersonalGoal pG = (PersonalGoal) o;
                    try {
                        newClient.updatePersonalGoal(new PersonalGoalView(pG.isAchieved(), pG.getDescription()), arg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            /* Add Shelf status observer */
            newPlayer.addObserver((o, arg) -> {
                Player p = (Player) o;
                try {
                    if (arg == null) {
                        newClient.updateShelf(new ShelfView(p.getId(), p.getShelf()), arg);
                    } else if (arg.equals(Player.PlayerEvent.SHELF_MODIFIED)) {
                        newClient.updateShelf(new ShelfView(p.getId(), p.getShelf()), arg);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

            /* Add Shelf status observer of new player to all already joined players */
            /* Add Shelf status observer of all already joined players to new player */
            playerAssociation.forEach((joinedClient, joinedPlayer) -> {
                joinedPlayer.addObserver((o, arg) -> {
                    Player p = (Player) o;
                    try {
                        if (arg == null) {
                            newClient.updateShelf(new ShelfView(p.getId(), p.getShelf()), arg);
                        } else if (arg.equals(Player.PlayerEvent.SHELF_MODIFIED)) {
                            newClient.updateShelf(new ShelfView(p.getId(), p.getShelf()), arg);
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                newPlayer.addObserver((o, arg) -> {
                    Player p = (Player) o;
                    try {
                        if (arg == null) {
                            joinedClient.updateShelf(new ShelfView(p.getId(), p.getShelf()), arg);
                        }else if (arg.equals(Player.PlayerEvent.SHELF_MODIFIED)) {
                            joinedClient.updateShelf(new ShelfView(p.getId(), p.getShelf()), arg);
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });

            /* Put newClient into known client */
            playerAssociation.put(newClient, newPlayer);

            /* If game is ready to be started we force the first */
            if (game.getGameStatus().equals(Game.GameStatus.STARTED))
                game.updatePlayersTurn();

        } catch (PlayerAlreadyExistsException e) {
            try {
                newClient.signalError("PLAYER ID ALREADY EXISTS");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //TODO send error to client
        } catch (PlayerNotExistsException e) {
            try {
                newClient.signalError("PLAYER ID DOES NOT EXISTS");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //TODO send error to client
        } catch (MatchmakingClosedException e) {
            try {
                newClient.signalError("MATCHMAKING CLOSED");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //TODO send error to client
        } catch (GameEndedException e) {
            throw new RuntimeException(e); //TODO routine di fine partita
        }
    }

    public synchronized void doPlayerMove(ClientInterface client, PlayerMove move) {
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
            Tile[][] shelf = player.getShelf();
            Tile[][] board = game.getLivingRoom().getBoard();

            /* Do some checks on "move" object, if malformed we discard it */
            if (move == null || move.getPickedTiles() != null || move.getPickedTiles().size() > 0)
                throw new MalformedPlayerMoveException("Malformed move object");

            if (move.getColumnToInsert() < 0 || move.getColumnToInsert() > shelf.length - 1)
                throw new MalformedPlayerMoveException("Column index out of bounds");

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
            player.setShelf(shelf);

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
            //TODO send error to client
        } catch (ClientNotAllowedException e) {
            //TODO send error to client
        } catch (GameNotStartedException e) {
            //TODO send error to client
        } catch (GameEndedException e) {
            throw new RuntimeException(e);
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
            //TODO send error to client
        }
    }

    private int freeShelfColumnSpaces(int column, Tile[][] shelf) {
        int spaces = 0;
        for (Tile[] row : shelf) {
            if (row[column].equals(Tile.EMPTY))
                spaces++;
        }
        return spaces;
    }

    private void insertTileInShelf(int column, Tile[][] shelf, Tile tile) {
        int row = shelf.length - 1;
        while (!shelf[row][column].equals(Tile.EMPTY))
            row--;

        shelf[row][column] = tile;
    }

    private boolean isTilePickable(int row, int column, Tile[][] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column].equals(Tile.EMPTY))
            return false;

        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        if (board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY)
            return true;

        return false;
    }

    private boolean evaluateFullShelf(Tile[][] shelf) {
        for (Tile[] row : shelf)
            for (Tile tile : row)
                if (tile.equals(Tile.EMPTY))
                    return false;

        return true;
    }
}
