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

    private List<Player> avaiablePlayers;

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
    public StandardGame(List<Player> avaiablePlayers, LivingRoom livingRoom, List<CommonGoal> commonGoals){
        this.avaiablePlayers = new ArrayList<>(avaiablePlayers);
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
        //TODO al momento della chiusura rimetto tutti i valori di players in avaiable players mantenendo players immacolato
        //TODO alla riconnessione se il player corrispondente all'id è all'interno della lista allora avviene la riconnessione
        /* If matchmaking is closed (reached max number of connected player) we discard the request */
        if (!(status.equals(GameStatus.MATCHMAKING) || status.equals(GameStatus.RESTARTING)))
            throw new MatchmakingClosedException();

        /* If trying to add an already existing playerId we discard the request */
        if (status.equals(GameStatus.MATCHMAKING)) {
            if (players.containsKey(playerId))
                throw new PlayerAlreadyExistsException();

            /* Initialize Player model */
           Player newPlayer = avaiablePlayers.remove(0);

            /* Associate Player with playerId */
            players.put(playerId, newPlayer);

            /* Add Player to turnQueue */
            playerTurnQueue.add(playerId);

            setChanged();
            notifyObservers(Event.PLAYER_JOINED);
        } else {
            if (!players.containsKey(playerId))
                throw new MatchmakingClosedException();

            //TODO problema che due giocatori possono riconnettersi allo stesso giocatore
            Player rejoined = players.get(playerId);
            avaiablePlayers.remove(rejoined);

            setChanged();
            notifyObservers(Event.PLAYER_REJOINED);
        }
        /* If we have now reached the max playerNumber we set game ready to be started */
        if (avaiablePlayers.isEmpty()) {

            if (status == GameStatus.MATCHMAKING) {
                /* Update turn sequence and firstPlayer */
                Collections.shuffle(playerTurnQueue);
                firstPlayer = playerTurnQueue.get(0);
            }

            this.status = GameStatus.STARTED;
            setChanged();
            notifyObservers(Event.GAME_STARTED);
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
        if (!players.containsKey(playerId))
            throw new PlayerNotExistsException();

        return players.get(playerId);
    }

    /**
     * {@inheritDoc}
     *
     * @return a copy of {@link #commonGoals}
     */
    @Override
    public @NotNull List<CommonGoal> getCommonGoals() {
        return new ArrayList<>(commonGoals);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #livingRoom}
     */
    @Override
    public @NotNull LivingRoom getLivingRoom() {
        return livingRoom;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Set {@link #lastTurn} true
     */
    @Override
    public void setLastTurn() {
        this.lastTurn = true;
        setChanged();
        notifyObservers(Event.LAST_TURN);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #lastTurn}
     */
    @Override
    public boolean isLastTurn() {
        return lastTurn;
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
        setChanged();
        notifyObservers(Event.GAME_ENDED);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #status}
     */
    @Override
    public GameStatus getGameStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePlayersTurn() throws GameEndedException {
        String p = playerTurnQueue.remove(0);
        playerTurnQueue.add(p);
        setChanged();
        notifyObservers(Event.NEXT_TURN);

        if (p.equals(firstPlayer) && lastTurn)
            throw new GameEndedException();
    }

    /**
     * this method returns a list of single Tile positions representing a personal goal
     *
     * @param tiles array of Tiles of the personal goal
     * @param rows  array of row position of each Tile
     * @param cols  array of column position of each Tile
     * @return ArrayList representing a standard personal goal
     */
    private @NotNull ArrayList<PersonalGoal> createPersonalGoal(Tile @NotNull [] tiles, int[] rows, int[] cols) {
        ArrayList<PersonalGoal> personalGoal = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            personalGoal.add(new StandardPersonalGoal(tiles[i], rows[i], cols[i]));
        }
        return personalGoal;
    }

    /**
     * {@inheritDoc}
     *
     * @return An {@link GameInfo} representing this object instance
     */
    @Override
    public @NotNull GameInfo getInfo() {
        Map<String, Integer> points = new HashMap<>();
        players.forEach((s, player) -> {
            /*  Points earned by each player are the sum of points earned by
                achieving common goals and by forming groups of tiles       */
            int playerPoints = getCommonGoalPoints(player.getAchievedCommonGoals().values().stream().toList())
                    + getShelfTilesGroupsPoints(player.getShelf().getTiles());


            /* Show points earned from personal goals only at game end */
            if (status == GameStatus.ENDED)
                playerPoints += getPersonalGoalPoints(player.getPersonalGoals());

            points.put(s, playerPoints);
        });
        return new GameInfo(status, lastTurn, getTurnPlayerId(), points);
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
                    int groupSize = depthSearch(i, j, shelf, checked, shelf[i][j].getColor());

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
                + depthSearch(i - 1, j, shelf, checked, tileColor)
                + depthSearch(i + 1, j, shelf, checked, tileColor)
                + depthSearch(i, j + 1, shelf, checked, tileColor)
                + depthSearch(i, j - 1, shelf, checked, tileColor);
    }


}
