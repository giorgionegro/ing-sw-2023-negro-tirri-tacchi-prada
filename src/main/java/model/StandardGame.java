package model;

import model.abstractModel.*;
import model.exceptions.GameEndedException;
import model.exceptions.MatchmakingClosedException;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;
import model.goalEvaluators.*;
import model.instances.StandardCommonGoalInstance;
import model.instances.StandardGameInstance;
import model.instances.StandardLivingRoomInstance;
import model.instances.StandardPlayerInstance;
import modelView.GameInfo;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;


/**
 * This class is an implementation of {@link Game}
 */
public class StandardGame extends Game {

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
     * Id of this game
     */
    private final String gameId;
    /**
     * Round player turn sequence of the game
     * <p>
     * It is used as a "circular queue", every time turn is given to the next
     * player the Player on the bottom of the list (element 0) is put on the top of the list (last element)
     * <p>
     * Top Player of the list (last element) is assumed to be the player that currently has the turn
     */
    private final @NotNull List<Player> playerTurnQueue;
    /**
     * Max number of player that can join the game and that has to join the game in order to be started
     */
    private final int maxPlayerNumber;
    private final @NotNull Stack<List<PersonalGoal>> personalGoals;
    private int joinedPlayer;
    /**
     * First player that ever had the turn, it is assumed to be also the first player of every round
     */
    private Player firstPlayer = null;
    /**
     * Signal of last round of turns
     */
    private boolean lastTurn;
    /**
     * Current status of the game
     */
    private GameStatus status;

    /**
     * Construct an {@link StandardGame} instance with given id and player number
     * <p>
     * The instance is initialized with empty player list, two randomly chosen common goals, an {@link StandardLivingRoom} instance
     * and on matchmaking status.
     * <p>
     * Player number must be between 2 and 4
     *
     * @param gameId       id of the game
     * @param playerNumber max number of player that can join the game
     */
    public StandardGame(String gameId, int playerNumber) {
        if (playerNumber < 2 || playerNumber > 4)
            throw new IllegalArgumentException("Number of players must be between 2 and 4.");

        this.joinedPlayer = 0;
        this.maxPlayerNumber = playerNumber;
        this.gameId = gameId;
        this.commonGoals = this.setCommonGoals(playerNumber);
        this.livingRoom = new StandardLivingRoom(playerNumber);
        this.players = new HashMap<>();
        this.playerTurnQueue = new ArrayList<>();
        this.lastTurn = false;
        this.status = GameStatus.MATCHMAKING;
        this.personalGoals = this.setPersonalGoals();
    }

    /**
     * Construct a {@link StandardGame} using the given instance
     *
     * @param instance the {@link StandardGame} instance
     */
    public StandardGame(@NotNull StandardGameInstance instance) {
        this.joinedPlayer = 0;
        this.players = new HashMap<>();
        instance.players().forEach((s, standardPlayerInstance) -> this.players.put(s, new StandardPlayer((StandardPlayerInstance) standardPlayerInstance)));
        this.maxPlayerNumber = instance.maxPlayerNumber();
        this.gameId = instance.gameId();
        this.commonGoals = new ArrayList<>();
        instance.commonGoals().forEach(standardCommonGoalInstance -> this.commonGoals.add(new StandardCommonGoal((StandardCommonGoalInstance) standardCommonGoalInstance)));
        this.livingRoom = new StandardLivingRoom((StandardLivingRoomInstance) instance.livingRoom());
        this.playerTurnQueue = new ArrayList<>();
        instance.playersTurnQueue().forEach(s -> this.playerTurnQueue.add(this.players.get(s)));
        this.firstPlayer = this.players.get(instance.firstPlayer());
        this.status = GameStatus.RESTARTING;
        this.lastTurn = instance.lastTurn();
        //re-initialize the personal goals is not needed
        this.personalGoals = new Stack<>();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #gameId}
     */
    @Override
    public String getGameId() {
        return this.gameId;
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

        /* If matchmaking is closed (reached max number of connected player) we discard the request */
        if (!(this.status == GameStatus.MATCHMAKING || this.status == GameStatus.RESTARTING))
            throw new MatchmakingClosedException();

        /* If trying to add an already existing playerId we discard the request */
        if (this.status == GameStatus.MATCHMAKING) {
            if (this.players.containsKey(playerId))
                throw new PlayerAlreadyExistsException();

            /* Initialize Player model */
            List<PersonalGoal> pGoals = this.personalGoals.pop();
            Player newPlayer = new StandardPlayer(playerId, pGoals);

            /* Associate Player with playerId */
            this.players.put(playerId, newPlayer);

            /* Add Player to turnQueue */
            this.playerTurnQueue.add(newPlayer);

            this.joinedPlayer++;

            this.setChanged();
            this.notifyObservers(Event.PLAYER_JOINED);
        } else {
            if (!this.players.containsKey(playerId))
                throw new MatchmakingClosedException();

            //TODO problema che due giocatori possono riconnettersi allo stesso giocatore
            this.joinedPlayer++;
            this.setChanged();
            this.notifyObservers(Event.PLAYER_REJOINED);
        }
        /* If we have now reached the max playerNumber we set game ready to be started */
        if (this.joinedPlayer == this.maxPlayerNumber) {

            if (this.status == GameStatus.MATCHMAKING) {
                /* Update turn sequence and firstPlayer */
                Collections.shuffle(this.playerTurnQueue);
                this.firstPlayer = this.playerTurnQueue.get(0);
            }

            this.status = GameStatus.STARTED;
            this.setChanged();
            this.notifyObservers(Event.GAME_STARTED);
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
        return this.playerTurnQueue.get(this.playerTurnQueue.size() - 1).getId();
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
        Player p = this.playerTurnQueue.remove(0);
        this.playerTurnQueue.add(p);
        this.setChanged();
        this.notifyObservers(Event.NEXT_TURN);

        if (p.equals(this.firstPlayer) && this.lastTurn)
            throw new GameEndedException();
    }

    /**
     * {@inheritDoc}
     *
     * @return A {@link StandardGameInstance} constructed using instance values
     */
    @Override
    public @NotNull Serializable getInstance() {
        Map<String, Serializable> playersInstance = new HashMap<>();
        this.players.forEach((s, player) -> playersInstance.put(s, player.getInstance()));

        List<Serializable> commonGoalsInstance = new ArrayList<>();
        this.commonGoals.forEach(commonGoal -> commonGoalsInstance.add(commonGoal.getInstance()));

        Serializable livingRoomInstance = this.livingRoom.getInstance();

        List<String> playerTurnQueueInstance = new ArrayList<>();
        this.playerTurnQueue.forEach(player -> playerTurnQueueInstance.add(player.getId()));

        String firstPlayerInstance = Objects.requireNonNull(this.firstPlayer).getId();

        return new StandardGameInstance(
                playersInstance,
                commonGoalsInstance,
                livingRoomInstance,
                this.gameId,
                playerTurnQueueInstance,
                firstPlayerInstance,
                this.maxPlayerNumber,
                this.lastTurn
        );
    }

    /**
     * This method returns a stack containing one of each type of common goal for the number of players in the game.
     *
     * @param nPlayers number of players in the game
     * @return stack containing one of each type of common goal
     */
    private @NotNull ArrayList<CommonGoal> setCommonGoals(int nPlayers) {
        Stack<StandardCommonGoal> allGoals = new Stack<>();

        allGoals.add(new StandardCommonGoal(nPlayers, new Standard2ColumnsRowOfDifferentTiles(false)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard2ColumnsRowOfDifferentTiles(true)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard3or4ColumnsRowMax3Types(false)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard3or4ColumnsRowMax3Types(true)));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard4Groups4Tiles()));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard5TileDiagonal()));
        allGoals.add(new StandardCommonGoal(nPlayers, new Standard8TilesSameType()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardCorners()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardSixGroup2Tiles()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardStairs()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardTwoSquares()));
        allGoals.add(new StandardCommonGoal(nPlayers, new StandardXOfDifferentTiles()));

        Collections.shuffle(allGoals);

        ArrayList<CommonGoal> result = new ArrayList<>();

        result.add(allGoals.pop());
        result.add(allGoals.pop());

        return result;
    }

    /**
     * this method return stack containing lists of all the standard personal goals
     *
     * @return stack containing lists of all the standard personal goals
     */
    private @NotNull Stack<List<PersonalGoal>> setPersonalGoals() {
        Stack<List<PersonalGoal>> result = new Stack<>();
        result.add(this.createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.FRAMES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.TROPHIES_1}, new int[]{0, 0, 1, 2, 3, 5}, new int[]{0, 2, 4, 3, 1, 2}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.CATS_1, Tile.GAMES_1, Tile.BOOKS_1, Tile.TROPHIES_1, Tile.FRAMES_1}, new int[]{1, 2, 2, 3, 4, 5}, new int[]{1, 0, 2, 4, 3, 4}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.FRAMES_1, Tile.GAMES_1, Tile.PLANTS_1, Tile.CATS_1, Tile.TROPHIES_1, Tile.BOOKS_1}, new int[]{1, 1, 2, 3, 3, 4}, new int[]{0, 3, 2, 1, 4, 0}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.GAMES_1, Tile.TROPHIES_1, Tile.FRAMES_1, Tile.PLANTS_1, Tile.BOOKS_1, Tile.CATS_1}, new int[]{0, 2, 2, 3, 4, 4}, new int[]{4, 0, 2, 3, 1, 2}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.FRAMES_1, Tile.BOOKS_1, Tile.PLANTS_1, Tile.GAMES_1, Tile.CATS_1}, new int[]{1, 3, 3, 4, 5, 5}, new int[]{1, 1, 2, 4, 0, 3}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.FRAMES_1, Tile.PLANTS_1}, new int[]{0, 0, 2, 4, 4, 5}, new int[]{2, 4, 3, 1, 3, 0}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.CATS_1, Tile.FRAMES_1, Tile.PLANTS_1, Tile.TROPHIES_1, Tile.GAMES_1, Tile.BOOKS_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{0, 3, 1, 0, 4, 2}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.FRAMES_1, Tile.CATS_1, Tile.TROPHIES_1, Tile.PLANTS_1, Tile.BOOKS_1, Tile.GAMES_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{4, 1, 2, 0, 3, 3}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.GAMES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.TROPHIES_1, Tile.PLANTS_1, Tile.FRAMES_1}, new int[]{0, 2, 3, 4, 4, 5}, new int[]{2, 2, 4, 1, 4, 0}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.GAMES_1, Tile.BOOKS_1, Tile.CATS_1, Tile.FRAMES_1, Tile.PLANTS_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{4, 1, 0, 3, 1, 3}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.FRAMES_1, Tile.CATS_1, Tile.TROPHIES_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{2, 1, 0, 2, 4, 3}));
        result.add(this.createPersonalGoal(new Tile[]{Tile.BOOKS_1, Tile.PLANTS_1, Tile.FRAMES_1, Tile.TROPHIES_1, Tile.GAMES_1, Tile.CATS_1}, new int[]{0, 1, 2, 3, 4, 5}, new int[]{2, 1, 2, 3, 4, 0}));
        Collections.shuffle(result);
        return result;
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
