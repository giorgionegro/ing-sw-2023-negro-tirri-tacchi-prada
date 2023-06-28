package model;

import model.abstractModel.*;
import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import modelView.GameInfo;

import java.util.*;


/**
 * This class is an implementation of {@link Game}
 */
public class StandardGame extends Game {

    /**
     * Players that are playing in this game (map of playerId -> Player)
     */
    private final Map<String, Player> players;
    /**
     * Common goals associated to this game (list of CommonGoal)
     */
    private final List<CommonGoal> commonGoals;
    /**
     * Living room associated to this game
     */
    private final LivingRoom livingRoom;

    /**
     * Round player turn sequence of the game
     * <p>
     * It is used as a "circular queue", every time turn is given to the next
     * player the Player on the bottom of the list (element 0) is put on the top of the list (last element)
     * <p>
     * Top Player of the list (last element) is assumed to be the player that currently has the turn
     */
    private final List<String> playerTurnQueue;
    /**
     * List of player objects that aren't associated with a connected player
     * <p>
     * It is used as available player on Matchmaking and as list of disconnected player when a player exit game during Gameplay
     */
    private final List<Player> availablePlayers;
    /**
     * First player that ever had the turn, it is assumed to be also the first player of every round
     */
    private String firstPlayer = "";
    /**
     * Signal of last round of turns
     */
    private boolean lastTurn;
    /**
     * Current status of the game
     */
    private GameStatus status;

    /**
     * Construct an {@link StandardGame} instance with given players information, living room information and common goals information
     *
     * @param players     players information
     * @param livingRoom  living room information
     * @param commonGoals common goals information
     */
    protected StandardGame(List<Player> players, LivingRoom livingRoom, List<CommonGoal> commonGoals) {
        super();
        this.availablePlayers = new ArrayList<>(players);
        this.livingRoom = livingRoom;
        this.players = new HashMap<>();
        this.commonGoals = new ArrayList<>(commonGoals);
        this.playerTurnQueue = new ArrayList<>();
        this.status = GameStatus.MATCHMAKING;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerId the new id that has to be associated to the new Player
     * @throws PlayerAlreadyExistsException if {@link #players} key-set already contains playerId
     * @throws MatchmakingClosedException   if {@link #status} is not matchmaking
     */
    @Override
    public void addPlayer(String playerId) throws PlayerAlreadyExistsException, MatchmakingClosedException {
        if (this.status == GameStatus.MATCHMAKING) {
            if (this.players.containsKey(playerId))
                throw new PlayerAlreadyExistsException();

            /* Get Player model */
            Player newPlayer = this.availablePlayers.remove(0);

            /* Associate Player with playerId */
            this.players.put(playerId, newPlayer);

            /* Add Player to turnQueue */
            this.playerTurnQueue.add(playerId);

            /* Notify that a new player has joined the game*/
            this.setChanged();
            this.notifyObservers(Event.PLAYER_JOINED);

            /* If we have now reached the max playerNumber we set game ready to be started */
            if (this.availablePlayers.isEmpty()) {
                /* Update turn sequence and firstPlayer */
                Collections.shuffle(this.playerTurnQueue);
                this.firstPlayer = this.playerTurnQueue.get(0);

                this.status = GameStatus.STARTED;
                this.setChanged();
                this.notifyObservers(Event.GAME_STARTED);
            }
        } else if (this.status == GameStatus.STARTED || this.status == GameStatus.SUSPENDED) {
            /*If a player is trying to reconnect then...*/

            /* If it's trying to reconnect with a different player id then discard request */
            if (!this.players.containsKey(playerId))
                throw new MatchmakingClosedException();

            Player requestedPlayer = this.players.get(playerId);

            /* If player referred by playerId is available for reconnection */
            if (this.availablePlayers.contains(requestedPlayer)) {
                /* Remove player from available */
                this.availablePlayers.remove(requestedPlayer);
            } else {
                throw new PlayerAlreadyExistsException();
            }

            this.setChanged();
            this.notifyObservers(Event.PLAYER_JOINED);

            if (this.status == GameStatus.SUSPENDED) {
                this.status = GameStatus.STARTED;
                this.setChanged();
                this.notifyObservers(Event.GAME_RESUMED);
            }

        } else if (this.status == GameStatus.ENDED) {
            throw new MatchmakingClosedException();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param playerId the id of the player that needs to be removed
     * @throws PlayerNotExistsException when no player is associated with playerId
     */
    @Override
    public void removePlayer(String playerId) throws PlayerNotExistsException {
        if (!this.players.containsKey(playerId))
            throw new PlayerNotExistsException();

        this.availablePlayers.add(this.players.get(playerId));

        /* If firstPlayer then the next one player became the new firstPlayer */
        if (this.firstPlayer.equals(playerId)) {
            int playerTurnCurrentIndex = this.playerTurnQueue.indexOf(playerId);

            if (playerTurnCurrentIndex == this.playerTurnQueue.size() - 1)
                playerTurnCurrentIndex = -1;

            this.firstPlayer = this.playerTurnQueue.get(playerTurnCurrentIndex + 1);
        }

        /* If the number of currently connected player is 1 then suspend the game */
        int online = this.players.size() - this.availablePlayers.size();

        if (online == 1) {
            this.setChanged();
            this.status = GameStatus.SUSPENDED;
            this.notifyObservers(Event.GAME_SUSPENDED);
        } else if (online == 0) {
            this.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param playerId the id of the Player requested
     * @return Player of {@link #players} associated to playerId
     * @throws PlayerNotExistsException if {@link #players} key-set does not contain playerId
     */
    @Override
    public Player getPlayer(String playerId) throws PlayerNotExistsException {
        if (!this.players.containsKey(playerId))
            throw new PlayerNotExistsException();

        return this.players.get(playerId);
    }

    /**
     * {@inheritDoc}
     *
     * @return a copy of {@link #commonGoals}
     */
    @Override
    public List<CommonGoal> getCommonGoals() {
        return new ArrayList<>(this.commonGoals);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #livingRoom}
     */
    @Override
    public LivingRoom getLivingRoom() {
        return this.livingRoom;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Set {@link #lastTurn} true
     */
    @Override
    public void setLastTurn() {
        this.lastTurn = true;
        this.setChanged();
        this.notifyObservers(Event.LAST_TURN);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #lastTurn}
     */
    @Override
    public boolean isLastTurn() {
        return this.lastTurn;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #playerTurnQueue} last element {@link Player} id
     */
    @Override
    public String getTurnPlayerId() {
        return this.playerTurnQueue.get(this.playerTurnQueue.size() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.status = GameStatus.ENDED;
        this.setChanged();
        this.notifyObservers(Event.GAME_ENDED);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #status}
     */
    @Override
    public GameStatus getGameStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePlayersTurn() throws GameEndedException {
        if (this.status == GameStatus.ENDED)
            throw new GameEndedException();

        String p = this.playerTurnQueue.remove(0);
        this.playerTurnQueue.add(p);
        while (this.availablePlayers.contains(this.players.get(p))) {
            p = this.playerTurnQueue.remove(0);
            this.playerTurnQueue.add(p);
        }

        this.setChanged();
        this.notifyObservers(Event.NEXT_TURN);

        if (p.equals(this.firstPlayer) && this.lastTurn)
            throw new GameEndedException();
    }

    /**
     * {@inheritDoc}
     *
     * @return An {@link GameInfo} representing this object instance
     */
    @Override
    public GameInfo getInfo() {
        Map<String, Integer> points = new HashMap<>();
        this.players.forEach((s, player) -> {
            /* If a player is disconnected does not send points */
            if (this.availablePlayers.contains(player))
                return;

            /*  Points earned by each player are the sum of points earned by
                achieving common goals and by forming groups of tiles       */
            int playerPoints = this.getCommonGoalPoints(player.getAchievedCommonGoals().values().stream().toList())
                    + this.getShelfTilesGroupsPoints(player.getShelf().getTiles());


            /* Show points earned from personal goals only at game end */
            if (this.status == GameStatus.ENDED)
                playerPoints += this.getPersonalGoalPoints(player.getPersonalGoals());

            points.put(s, playerPoints);
        });
        return new GameInfo(this.status, this.lastTurn, this.firstPlayer, this.getTurnPlayerId(), points);
    }

    /**
     * This method returns an amount of points based on the number of personalGoals achieved
     *
     * @param personalGoals list of all personal goals
     * @return the amount of points earned by personal goals
     */
    private int getPersonalGoalPoints(List<? extends PersonalGoal> personalGoals) {
        int achieved = 0;
        for (PersonalGoal p : personalGoals)
            if (p.isAchieved())
                achieved++;

        return switch (achieved) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 9;
            default -> 12;
        };
    }


    /**
     * This method returns an amount of points equivalent to the sum of provided tokens
     *
     * @param tokens the list of earned tokens
     * @return the amount of points equivalent to the sum of provided tokens
     */
    private int getCommonGoalPoints(List<Token> tokens) {
        int ris = 0;

        for (Token t : tokens)
            ris += t.getPoints();

        return ris;
    }

    /**
     * This method returns an amount of points based on evaluation of how many and how big are groups of tiles in a shelf
     *
     * @param shelf the shelf to be evaluated
     * @return the amount of points the shelf is worth
     */
    private int getShelfTilesGroupsPoints(Tile[][] shelf) {
        int ris = 0;

        boolean[][] checked = new boolean[shelf.length][shelf[0].length];

        for (int i = 0; i < shelf.length; i++)
            for (int j = 0; j < shelf[0].length; j++)
                checked[i][j] = false;

        for (int i = 0; i < shelf.length; i++)
            for (int j = 0; j < shelf[0].length; j++)
                if (shelf[i][j] != Tile.EMPTY) {
                    int groupSize = this.depthSearch(i, j, shelf, checked, shelf[i][j].getColor());

                    if (groupSize >= 6)
                        ris += 8;
                    else if (groupSize == 5)
                        ris += 6;
                    else if (groupSize == 4)
                        ris += 3;
                    else if (groupSize == 3)
                        ris += 2;
                }

        return ris;
    }

    /**
     * This function provides support for {@code getShelfTilesGroupPoints()} and provide a recursive depth search on tile groups
     * @param i         row index
     * @param j        column index
     * @param shelf    the shelf to be evaluated
     * @param checked  the matrix of checked tiles
     * @param tileColor the color of the tile group
     * @return the size of the tile group
     */
    private int depthSearch(int i, int j, Tile[][] shelf, boolean[][] checked, String tileColor) {
        if (i < 0 || i >= checked.length || j < 0 || j >= checked[0].length)
            return 0;

        if (shelf[i][j] == Tile.EMPTY)
            return 0;

        if (checked[i][j])
            return 0;

        if (!shelf[i][j].getColor().equals(tileColor))
            return 0;

        checked[i][j] = true;

        return 1
                + this.depthSearch(i - 1, j, shelf, checked, tileColor)
                + this.depthSearch(i + 1, j, shelf, checked, tileColor)
                + this.depthSearch(i, j + 1, shelf, checked, tileColor)
                + this.depthSearch(i, j - 1, shelf, checked, tileColor);
    }


}
