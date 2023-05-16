package model;

import model.abstractModel.PersonalGoal;
import model.abstractModel.Player;
import model.abstractModel.PlayerChat;
import model.abstractModel.Shelf;
import model.instances.StandardPersonalGoalInstance;
import model.instances.StandardPlayerChatInstance;
import model.instances.StandardPlayerInstance;
import model.instances.StandardShelfInstance;
import modelView.PlayerInfo;

import java.io.Serializable;
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
    private final Shelf shelf;
    /**
     * personal goals of the player (list of personal goals)
     */
    private final List<PersonalGoal> personalGoal;

    /**
     * common goals of the player (map of common goals descriptions and tokens)
     */
    private final Map<String, Token> achievedCommonGoals;

    /**
     * chat of the player
     */
    private final PlayerChat chat;

    /**
     * last error the player reported during gameplay
     */

    private String errorReport;

    /**
     * Constructor of a new Player with no achieved common goal and an empty chat, but initialized with the given shelf,
     * personal goals and playerId.
     * @param idPlayer id of the player
     * @param personalGoal personal goals of the player
     */
    public StandardPlayer(String idPlayer, List<PersonalGoal> personalGoal){
        this.idPlayer = idPlayer;
        this.shelf = new StandardShelf();
        this.personalGoal = new ArrayList<>(personalGoal);
        this.achievedCommonGoals = new HashMap<>();
        this.chat = new StandardPlayerChat();
    }

    public StandardPlayer(StandardPlayerInstance instance){
        this.idPlayer = instance.idPlayer();
        this.shelf = new StandardShelf((StandardShelfInstance) instance.shelf());
        this.personalGoal = new ArrayList<>();
        instance.personalGoal().forEach(standardPersonalGoalInstance -> personalGoal.add(new StandardPersonalGoal((StandardPersonalGoalInstance) standardPersonalGoalInstance)));
        this.achievedCommonGoals = instance.achievedCommonGoals();
        this.chat = new StandardPlayerChat((StandardPlayerChatInstance) instance.chat());
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
    public Shelf getShelf() {
        return shelf;
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
        setChanged();
        notifyObservers(Event.COMMONGOAL_ACHIEVED);
    }

    /**
     * {@inheritDoc}
     * @param error the error description
     */
    @Override
    public void reportError(String error){
        this.errorReport = error;
        setChanged();
        notifyObservers(Event.ERROR_REPORTED);
    }

    /**
     * {@inheritDoc}
     * @return {@link #errorReport}
     */
    @Override
    public String getReportedError() {
        return errorReport;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public PlayerInfo getInfo(){
        return new PlayerInfo(errorReport, new HashMap<>(achievedCommonGoals));
    }

    @Override
    public Serializable getInstance(){
        List<Serializable> personalGoalInstance = new ArrayList<>();
        personalGoal.forEach(goal -> personalGoalInstance.add(goal.getInstance()));

        return new StandardPlayerInstance(idPlayer,shelf.getInstance(),personalGoalInstance,achievedCommonGoals,chat.getInstance());
    }
}
