package model;

import model.abstractModel.*;
import model.exceptions.*;

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

        this.maxPlayerNumber = playerNumber;
        this.gameId = gameId;
        this.commonGoals = new ArrayList<>(); //TODO da scegliere i common goals
        this.livingRoom = new StandardLivingRoom(playerNumber);
        this.players = new HashMap<>();
        this.playerTurnQueue = new ArrayList<>();
        this.lastTurn = false;
        this.status = GameStatus.MATCHMAKING;
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
        if(!status.equals(GameStatus.MATCHMAKING))
            throw new MatchmakingClosedException();

        /* If trying to add an already existing playerId we discard the request */
        if(players.containsKey(playerId))
            throw new PlayerAlreadyExistsException();

        /* Initialize Player model */
        List<PersonalGoal> personalGoals = new ArrayList<>(); //TODO pick personalGoals
        Player newPlayer = new StandardPlayer(playerId, personalGoals);

        /* Associate Player with playerId */
        players.put(playerId,newPlayer);

        /* Add Player to turnQueue */
        playerTurnQueue.add(newPlayer);

        /* If we have now reached the max playerNumber we set game ready to be started */
        if(players.size()==maxPlayerNumber) {
            this.status = GameStatus.STARTED;

            /* Update turn sequence and firstPlayer */
            Collections.shuffle(playerTurnQueue);
            firstPlayer = playerTurnQueue.get(0);
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
        notifyObservers(GameEvent.NEXT_TURN);

        if(p.equals(firstPlayer) && lastTurn){
            this.status = GameStatus.ENDED;
            throw new GameEndedException();
        }
    }
}
