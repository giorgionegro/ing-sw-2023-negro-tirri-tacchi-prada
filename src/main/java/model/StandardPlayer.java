package model;

import model.abstractModel.PersonalGoal;
import model.abstractModel.Player;
import model.abstractModel.PlayerChat;

import java.util.*;

/**
 * This class is an implementation of Player.
 * <p>
 * It uses a Map to manage achieved common goals
 */
public class StandardPlayer extends Player {

    /**
     * id of the player
     */
    private final String idPlayer;
    /**
     * shelf of the player (2D array of tiles)
     */
    private Tile[][] shelf;
    /**
     * personal goals of the player (list of personal goals)
     */
    private final List<PersonalGoal> personalGoal;

    /**
     * common goals of the player (map of common goals descriptions and tokens)
     */
    private Map<String, Token> achievedCommonGoals;

    /**
     * chat of the player
     */
    private final PlayerChat chat;

    /**
     * Constructor of a new Player with no achieved common goal and an empty chat, but initialized with the given shelf,
     * personal goals and playerId.
     * @param idPlayer id of the player
     * @param personalGoal personal goals of the player
     */
    public StandardPlayer(String idPlayer, List<PersonalGoal> personalGoal){
        this.idPlayer = idPlayer;
        this.shelf = new Tile[6][5];
        this.personalGoal = new ArrayList<>(personalGoal);
        this.achievedCommonGoals = new HashMap<>();
        this.chat = new StandardPlayerChat();
    }

    /**
     * {@inheritDoc}
     * @return {@link #idPlayer}
     */
    @Override
    public String getId(){
        return idPlayer;
    }

    /**
     * {@inheritDoc}
     * @return a copy of {@link #shelf}
     */
    @Override
    public Tile[][] getShelf() {
        return Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * {@inheritDoc}
     * @param modifiedShelf modified shelf of the player
     */
    @Override
    public void setShelf(Tile[][] modifiedShelf){
        this.shelf = Arrays.stream(modifiedShelf).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    /**
     * {@inheritDoc}
     * @return a copy of {@link #personalGoal}
     */
    @Override
    public List<PersonalGoal> getPersonalGoals() {
        return new ArrayList<>(personalGoal);
    }

    /**
     * {@inheritDoc}
     * @return {@link #chat}
     */
    @Override
    public PlayerChat getPlayerChat() {
        return chat;
    }

    /**
     * {@inheritDoc}
     * @return a copy of {@link #achievedCommonGoals}
     */
    public Map<String,Token> getAchievedCommonGoals(){ return new HashMap<>(achievedCommonGoals);}

    /**
     * {@inheritDoc}
     * @param description the common goal description
     * @param token the token won by achieving the common goal
     */
    public void addAchievedCommonGoal(String description, Token token){
        achievedCommonGoals.put(description, token);
    }
}
