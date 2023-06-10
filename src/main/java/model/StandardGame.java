package model;

import model.abstractModel.*;
import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import modelView.GameInfo;
import org.jetbrains.annotations.NotNull;

import java.util.*;


/**
 * This class is an implementation of {@link Game}
 */
public class StandardGame extends Game{

    /**
     * Players that are playing in this game (map of playerId -> Player)
     */
    private final @NotNull Map<String, Player> players;
    /**
     * Common goals associated to this game (list of CommonGoal)
     */
    private final @NotNull List<CommonGoal> commonGoals;
    /**
     * Living room associated to this game
     */
    private final @NotNull LivingRoom livingRoom;

    /**
     * Round player turn sequence of the game
     * <p>
     * It is used as a "circular queue", every time turn is given to the next
     * player the Player on the bottom of the list (element 0) is put on the top of the list (last element)
     * <p>
     * Top Player of the list (last element) is assumed to be the player that currently has the turn
     */
    private final @NotNull List<String> playerTurnQueue;

    /**
     * First player that ever had the turn, it is assumed to be also the first player of every round
     */
    private String firstPlayer = null;
    /**
     * Signal of last round of turns
     */
    private boolean lastTurn;
    /**
     * Current status of the game
     */
    private GameStatus status;

    private final List<Player> availablePlayers;

    /**
     * Construct an {@link StandardGame} instance with given id and player number
     * <p>
     * The instance is initialized with empty player list, two randomly chosen common goals, an {@link StandardLivingRoom} instance
     * and on matchmaking status.
     * <p>
     * Player number must be between 2 and 4
     *
     * @param gameId       id of the game
     * @param playerNumber max number of player that can join the game //TODO sistemare
     */
    public StandardGame(List<Player> availablePlayers, LivingRoom livingRoom, List<CommonGoal> commonGoals){
        this.availablePlayers = new ArrayList<>(availablePlayers);
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
        if(status == GameStatus.MATCHMAKING){
            if (players.containsKey(playerId))
                throw new PlayerAlreadyExistsException();

            /* Initialize Player model */
            Player newPlayer = availablePlayers.remove(0);

            /* Associate Player with playerId */
            players.put(playerId, newPlayer);

            /* Add Player to turnQueue */
            playerTurnQueue.add(playerId);

            setChanged();
            notifyObservers(Event.PLAYER_JOINED);

            /* If we have now reached the max playerNumber we set game ready to be started */
            if (availablePlayers.isEmpty()) {
                /* Update turn sequence and firstPlayer */
                Collections.shuffle(playerTurnQueue);
                firstPlayer = playerTurnQueue.get(0);

                this.status = GameStatus.STARTED;
                setChanged();
                notifyObservers(Event.GAME_STARTED);
            }
        }else if(status == GameStatus.STARTED || status == GameStatus.SUSPENDED){
            if(!players.containsKey(playerId))
                throw new MatchmakingClosedException();

            Player requestedPlayer = players.get(playerId);

            /* If player referred by playerId is available for reconnection */
            if(availablePlayers.contains(requestedPlayer)){
                /* Remove player from available */
                availablePlayers.remove(requestedPlayer);

                setChanged();
                notifyObservers(Event.PLAYER_JOINED);

            }else{
                throw new PlayerAlreadyExistsException();
            }

            status = GameStatus.STARTED;
            setChanged();
            notifyObservers(Event.PLAYER_REJOINED);
        } else if (status == GameStatus.ENDED) {
            throw new MatchmakingClosedException();
        }
    }

    @Override
    public void removePlayer(String playerId) throws PlayerNotExistsException {
        if(!players.containsKey(playerId))
            throw new PlayerNotExistsException();

        availablePlayers.add(players.get(playerId));

        /* If firstPlayer then the next one player became the new firstPlayer */
        if(firstPlayer.equals(playerId)) {
            int playerTurnCurrentIndex = playerTurnQueue.indexOf(playerId);

            if(playerTurnCurrentIndex == playerTurnQueue.size()-1)
                playerTurnCurrentIndex = -1;

            firstPlayer = playerTurnQueue.get(playerTurnCurrentIndex+1);
        }

        int online = players.size()-availablePlayers.size();
        if(online==1)
            this.status = GameStatus.SUSPENDED;
        else if (online==0)
            this.status = GameStatus.ENDED;
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
    public @NotNull List<CommonGoal> getCommonGoals() {
        return new ArrayList<>(this.commonGoals);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #livingRoom}
     */
    @Override
    public @NotNull LivingRoom getLivingRoom() {
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
        return playerTurnQueue.get(playerTurnQueue.size() - 1);
    }

    @Override
    public void close() {
        this.status = GameStatus.ENDED;
        //save state
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
        String p = playerTurnQueue.remove(0);
        playerTurnQueue.add(p);
        while(availablePlayers.contains(players.get(p))){
            p = playerTurnQueue.remove(0);
            playerTurnQueue.add(p);
        }

        setChanged();
        notifyObservers(Event.NEXT_TURN);

        if (p.equals(this.firstPlayer) && this.lastTurn)
            throw new GameEndedException();
    }

    /**
     * {@inheritDoc}
     *
     * @return An {@link GameInfo} representing this object instance
     */
    @Override
    public @NotNull GameInfo getInfo() {
        Map<String, Integer> points = new HashMap<>();
        this.players.forEach((s, player) -> {
            /*  Points earned by each player are the sum of points earned by
                achieving common goals and by forming groups of tiles       */
            int playerPoints = this.getCommonGoalPoints(player.getAchievedCommonGoals().values().stream().toList())
                    + this.getShelfTilesGroupsPoints(player.getShelf().getTiles());


            /* Show points earned from personal goals only at game end */
            if (this.status == GameStatus.ENDED)
                playerPoints += this.getPersonalGoalPoints(player.getPersonalGoals());

            points.put(s, playerPoints);
        });
        return new GameInfo(this.status, this.lastTurn, this.getTurnPlayerId(), points);
    }


    private int getPersonalGoalPoints(@NotNull List<PersonalGoal> personalGoals) {
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

    private int getCommonGoalPoints(@NotNull List<Token> tokens) {
        int ris = 0;

        for (Token t : tokens)
            ris += t.getPoints();

        return ris;
    }

    private int getShelfTilesGroupsPoints(Tile[] @NotNull [] shelf) {
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

    private int depthSearch(int i, int j, Tile[][] shelf, boolean[] @NotNull [] checked, String tileColor) {
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
