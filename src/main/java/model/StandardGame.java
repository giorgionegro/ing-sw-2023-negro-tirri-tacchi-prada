package model;

import model.abstractModel.*;
import model.exceptions.*;
import model.goalEvaluators.*;
import model.instances.StandardCommonGoalInstance;
import model.instances.StandardGameInstance;
import model.instances.StandardLivingRoomInstance;
import model.instances.StandardPlayerInstance;
import modelView.GameInfo;

import java.io.Serializable;
import java.util.*;


/**
 * This class is an implementation of {@link Game}
 */
public class StandardGame extends Game {

    /**
     * Players that are playing in this game (map of playerId -> Player)
     */
    private final Map<String, Player> players;

    private int joinedPlayer;

    /**
     * Common goals associated to this game (list of CommonGoal)
     */
    private final List<CommonGoal> commonGoals;
    /**
     * Living room associated to this game
     */
    private final LivingRoom livingRoom;

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
    private final List<Player> playerTurnQueue;

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
     * Max number of player that can join the game and that has to join the game in order to be started
     */
    private final int maxPlayerNumber;

    private final Stack<List<PersonalGoal>> personalGoals;

    /**
     * Construct an StandardGame instance with given id and player number
     * <p>
     * The instance is initialized with empty player list, two randomly chosen common goals, an {@link StandardLivingRoom} instance
     * and on matchmaking status.
     * <p>
     * Player number must be between 2 and 4
     * @param gameId id of the game
     * @param playerNumber max number of player that can join the game
     */
    public StandardGame(String gameId, int playerNumber){
        if(playerNumber<2 || playerNumber>4)
            throw new IllegalArgumentException("Number of players must be between 2 and 4.");

        this.joinedPlayer = 0;
        this.maxPlayerNumber = playerNumber;
        this.gameId = gameId;
        this.commonGoals = setCommonGoals(playerNumber);
        this.livingRoom = new StandardLivingRoom(playerNumber);
        this.players = new HashMap<>();
        this.playerTurnQueue = new ArrayList<>();
        this.lastTurn = false;
        this.status = GameStatus.MATCHMAKING;
        this.personalGoals = setPersonalGoals();
    }

    public StandardGame(StandardGameInstance instance){
        this.joinedPlayer = 0;
        this.players = new HashMap<>();
        instance.players().forEach((s, standardPlayerInstance) -> players.put(s, new StandardPlayer((StandardPlayerInstance) standardPlayerInstance)));
        this.maxPlayerNumber = instance.maxPlayerNumber();
        this.gameId = instance.gameId();
        this.commonGoals = new ArrayList<>();
        instance.commonGoals().forEach(standardCommonGoalInstance -> commonGoals.add(new StandardCommonGoal((StandardCommonGoalInstance) standardCommonGoalInstance)));
        this.livingRoom = new StandardLivingRoom((StandardLivingRoomInstance) instance.livingRoom());
        this.playerTurnQueue = new ArrayList<>();
        instance.playersTurnQueue().forEach(s -> playerTurnQueue.add(players.get(s)));
        this.firstPlayer = players.get(instance.firstPlayer());
        this.status = GameStatus.RESTARTING;
        this.lastTurn = instance.lastTurn();
        //re-initialize the personal goals is not needed
        this.personalGoals = new Stack<>();
    }

    /**
     * {@inheritDoc}
     * @return {@link #gameId}
     */
    @Override
    public String getGameId() {
        return gameId;
    }

    /**
     * {@inheritDoc}
     * @param playerId the new id that has to be associated to the new Player
     * @throws PlayerAlreadyExistsException if {@link #players} key-set already contains playerId
     * @throws MatchmakingClosedException if {@link #status} is not matchmaking
     */
    @Override
    public void addPlayer(String playerId) throws PlayerAlreadyExistsException, MatchmakingClosedException {

        /* If matchmaking is closed (reached max number of connected player) we discard the request */
        if(!(status.equals(GameStatus.MATCHMAKING) || status.equals(GameStatus.RESTARTING)))
            throw new MatchmakingClosedException();

        /* If trying to add an already existing playerId we discard the request */
        if(status.equals(GameStatus.MATCHMAKING)) {
            if (players.containsKey(playerId))
                throw new PlayerAlreadyExistsException();

            /* Initialize Player model */
            List<PersonalGoal> pGoals = personalGoals.pop();
            Player newPlayer = new StandardPlayer(playerId, pGoals);

            /* Associate Player with playerId */
            players.put(playerId, newPlayer);

            /* Add Player to turnQueue */
            playerTurnQueue.add(newPlayer);

            joinedPlayer++;

            setChanged();
            notifyObservers(Event.PLAYER_JOINED);
        }else{
            if(!players.containsKey(playerId))
                throw new MatchmakingClosedException();

            joinedPlayer++;
            setChanged();
            notifyObservers(Event.PLAYER_REJOINED);
        }
        /* If we have now reached the max playerNumber we set game ready to be started */
        if(joinedPlayer==maxPlayerNumber) {

            if(status == GameStatus.MATCHMAKING){
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
     * @param playerId the id of the Player requested
     * @return Player of {@link #players} associated to playerId
     * @throws PlayerNotExistsException if {@link #players} key-set does not contain playerId
     */
    @Override
    public Player getPlayer(String playerId) throws PlayerNotExistsException {
        if(!players.containsKey(playerId))
            throw new PlayerNotExistsException();

        return players.get(playerId);
    }

    /**
     * {@inheritDoc}
     * @return a copy of {@link #commonGoals}
     */
    @Override
    public List<CommonGoal> getCommonGoals() {
        return new ArrayList<>(commonGoals);
    }

    /**
     * {@inheritDoc}
     * @return {@link #livingRoom}
     */
    @Override
    public LivingRoom getLivingRoom() {
        return livingRoom;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Set {@link #lastTurn} true
     */
    @Override
    public void setLastTurn(){
        this.lastTurn = true;
        setChanged();
        notifyObservers(Event.LAST_TURN);
    }

    /**
     * {@inheritDoc}
     * @return {@link #lastTurn}
     */
    @Override
    public boolean isLastTurn(){
        return lastTurn;
    }

    /**
     * {@inheritDoc}
     * @return {@link #playerTurnQueue} last element {@link Player} id
     */
    @Override
    public String getTurnPlayerId(){
        return playerTurnQueue.get(playerTurnQueue.size()-1).getId();
    }

    /**
     * {@inheritDoc}
     * @return {@link #status}
     */
    @Override
    public GameStatus getGameStatus(){
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePlayersTurn() throws GameEndedException{
        Player p = playerTurnQueue.remove(0);
        playerTurnQueue.add(p);
        setChanged();
        notifyObservers(Event.NEXT_TURN);

        if(p.equals(firstPlayer) && lastTurn){
            this.status = GameStatus.ENDED;

            setChanged();
            notifyObservers(Event.GAME_ENDED);

            throw new GameEndedException();
        }
    }

    @Override
    public Serializable getInstance(){
        Map<String, Serializable> playersInstance = new HashMap<>();
        players.forEach((s, player) -> playersInstance.put(s,player.getInstance()));

        List<Serializable> commonGoalsInstance = new ArrayList<>();
        commonGoals.forEach(commonGoal -> commonGoalsInstance.add(commonGoal.getInstance()));

        Serializable livingRoomInstance = livingRoom.getInstance();

        List<String> playerTurnQueueInstance = new ArrayList<>();
        playerTurnQueue.forEach(player -> playerTurnQueueInstance.add(player.getId()));

        String firstPlayerInstance = firstPlayer.getId();

        return new StandardGameInstance(
                playersInstance,
                commonGoalsInstance,
                livingRoomInstance,
                gameId,
                playerTurnQueueInstance,
                firstPlayerInstance,
                maxPlayerNumber,
                lastTurn
                );
    }

    /**
     * This method returns a stack containing one of each type of common goal for the number of players in the game.
     * @param nPlayers number of players in the game
     * @return stack containing one of each type of common goal
     */
    private ArrayList<CommonGoal> setCommonGoals(int nPlayers){
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
     * @return stack containing lists of all the standard personal goals
     */
    private Stack<List<PersonalGoal>> setPersonalGoals(){
        Stack<List<PersonalGoal>> result = new Stack<>();
        result.add(createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.FRAMES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.TROPHIES_1}, new int[]{0,0,1,2,3,5}, new int[]{0,2,4,3,1,2}));
        result.add(createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.CATS_1, Tile.GAMES_1, Tile.BOOKS_1, Tile.TROPHIES_1, Tile.FRAMES_1}, new int[]{1,2,2,3,4,5}, new int[]{1,0,2,4,3,4}));
        result.add(createPersonalGoal(new Tile[]{Tile.FRAMES_1, Tile.GAMES_1, Tile.PLANTS_1, Tile.CATS_1, Tile.TROPHIES_1, Tile.BOOKS_1}, new int[]{1,1,2,3,3,4}, new int[]{0,3,2,1,4,0}));
        result.add(createPersonalGoal(new Tile[]{Tile.GAMES_1, Tile.TROPHIES_1, Tile.FRAMES_1, Tile.PLANTS_1, Tile.BOOKS_1, Tile.CATS_1}, new int[]{0,2,2,3,4,4}, new int[]{4,0,2,3,1,2}));
        result.add(createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.FRAMES_1, Tile.BOOKS_1, Tile.PLANTS_1, Tile.GAMES_1, Tile.CATS_1}, new int[]{1,3,3,4,5,5}, new int[]{1,1,2,4,0,3}));
        result.add(createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.FRAMES_1, Tile.PLANTS_1}, new int[]{0,0,2,4,4,5}, new int[]{2,4,3,1,3,0}));
        result.add(createPersonalGoal(new Tile[]{Tile.CATS_1, Tile.FRAMES_1, Tile.PLANTS_1, Tile.TROPHIES_1, Tile.GAMES_1, Tile.BOOKS_1}, new int[]{0,1,2,3,4,5}, new int[]{0,3,1,0,4,2}));
        result.add(createPersonalGoal(new Tile[]{Tile.FRAMES_1, Tile.CATS_1, Tile.TROPHIES_1, Tile.PLANTS_1, Tile.BOOKS_1, Tile.GAMES_1}, new int[]{0,1,2,3,4,5}, new int[]{4,1,2,0,3,3}));
        result.add(createPersonalGoal(new Tile[]{Tile.GAMES_1, Tile.CATS_1, Tile.BOOKS_1, Tile.TROPHIES_1, Tile.PLANTS_1, Tile.FRAMES_1}, new int[]{0,2,3,4,4,5}, new int[]{2,2,4,1,4,0}));
        result.add(createPersonalGoal(new Tile[]{Tile.TROPHIES_1, Tile.GAMES_1, Tile.BOOKS_1, Tile.CATS_1, Tile.FRAMES_1, Tile.PLANTS_1}, new int[]{0,1,2,3,4,5}, new int[]{4,1,0,3,1,3}));
        result.add(createPersonalGoal(new Tile[]{Tile.PLANTS_1, Tile.BOOKS_1, Tile.GAMES_1, Tile.FRAMES_1, Tile.CATS_1, Tile.TROPHIES_1}, new int[]{0,1,2,3,4,5}, new int[]{2,1,0,2,4,3}));
        result.add(createPersonalGoal(new Tile[]{Tile.BOOKS_1, Tile.PLANTS_1, Tile.FRAMES_1, Tile.TROPHIES_1, Tile.GAMES_1, Tile.CATS_1}, new int[]{0,1,2,3,4,5}, new int[]{2,1,2,3,4,0}));
        Collections.shuffle(result);
        return result;
    }

    /**
     * this method returns a list of single Tile positions representing a personal goal
     * @param tiles array of Tiles of the personal goal
     * @param rows array of row position of each Tile
     * @param cols array of column position of each Tile
     * @return ArrayList representing a standard personal goal
     */
    private ArrayList<PersonalGoal> createPersonalGoal(Tile[] tiles, int[] rows, int[] cols ){
        ArrayList<PersonalGoal> personalGoal = new ArrayList<>();
        for(int i = 0; i < tiles.length; i++){
            personalGoal.add(new StandardPersonalGoal(tiles[i], rows[i], cols[i]));
        }
        return personalGoal;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public GameInfo getInfo(){
        Map<String, Integer> points = new HashMap<>();
        players.forEach((s, player) -> {
            /*  Points earned by each player are the sum of points earned by
                achieving common goals and by forming groups of tiles       */
            int playerPoints = getCommonGoalPoints(player.getAchievedCommonGoals().values().stream().toList())
                    + getShelfTilesGroupsPoints(player.getShelf().getTiles());


            /* Show points earned from personal goals only at game end */
            if(status == GameStatus.ENDED)
                playerPoints += getPersonalGoalPoints(player.getPersonalGoals());

            points.put(s,playerPoints);
        });
        return new GameInfo(status, lastTurn, getTurnPlayerId(), points);
    }



    private int getPersonalGoalPoints(List<PersonalGoal> personalGoals){
        int achieved = 0;
        for(PersonalGoal p : personalGoals)
            if(p.isAchieved())
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

    private int getCommonGoalPoints(List<Token> tokens){
        int ris = 0;

        for(Token t : tokens)
            ris += t.getPoints();

        return ris;
    }

    private int getShelfTilesGroupsPoints(Tile[][] shelf){
        int ris = 0;

        boolean[][] checked = new boolean[shelf.length][shelf[0].length];

        for(int i = 0; i< shelf.length; i++)
            for(int j = 0; j < shelf[0].length; j++)
                checked[i][j] = false;

        for(int i = 0; i< shelf.length; i++)
            for(int j = 0; j < shelf[0].length; j++)
                if(shelf[i][j] != Tile.EMPTY) {
                    int groupSize = depthSearch(i, j, shelf, checked, shelf[i][j].getColor());

                    if(groupSize>=6)
                        ris += 8;
                    else if(groupSize==5)
                        ris += 6;
                    else if(groupSize==4)
                        ris += 3;
                    else if(groupSize==3)
                        ris += 2;
                }

        return ris;
    }

    private int depthSearch(int i, int j, Tile[][] shelf, boolean[][] checked, String tileColor){
        if(i<0 || i>=checked.length || j<0 || j>= checked[0].length)
            return 0;

        if(shelf[i][j] == Tile.EMPTY)
            return 0;

        if(checked[i][j])
            return 0;

        if(!shelf[i][j].getColor().equals(tileColor))
            return 0;

        checked[i][j]=true;

        return  1
                + depthSearch(i-1, j, shelf, checked, tileColor)
                + depthSearch(i+1, j, shelf, checked, tileColor)
                + depthSearch(i, j+1, shelf, checked, tileColor)
                + depthSearch(i, j-1, shelf, checked, tileColor);
    }
}
