package model;

import model.abstractModel.PersonalGoal;
import model.abstractModel.Player;
import model.abstractModel.PlayerChat;
import model.abstractModel.Shelf;
import modelView.PlayerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is an implementation of {@link Player}.
 * <p>
 * It uses a Map to manage achieved common goals
 */
public class StandardPlayer extends Player {

    /**
     * shelf of the player
     */
    private final Shelf shelf;
    /**
     * personal goals of the player
     */
    private final List<PersonalGoal> personalGoal;

    /**
     * common goals of the player
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
     * Constructor of a new Player with no achieved common goal and initialized with the given shelf, chat and personal goals,
     *
     * @param shelf        the player shelf
     * @param personalGoal personal goals of the player
     * @param chat         the player chat
     */
    public StandardPlayer(Shelf shelf, List<PersonalGoal> personalGoal, PlayerChat chat) {
        super();
        this.shelf = shelf;
        this.personalGoal = new ArrayList<>(personalGoal);
        this.achievedCommonGoals = new HashMap<>();
        this.chat = chat;
    }

    /**
     * {@inheritDoc}
     *
     * @return a copy of {@link #shelf}
     */
    @Override
    public Shelf getShelf() {
        return this.shelf;
    }

    /**
     * {@inheritDoc}
     *
     * @return a copy of {@link #personalGoal}
     */
    @Override
    public List<PersonalGoal> getPersonalGoals() {
        return new ArrayList<>(this.personalGoal);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link #chat}
     */
    @Override
    public PlayerChat getPlayerChat() {
        return this.chat;
    }

    /**
     * {@inheritDoc}
     *
     * @return a copy of {@link #achievedCommonGoals}
     */
    public Map<String, Token> getAchievedCommonGoals() {
        return new HashMap<>(this.achievedCommonGoals);
    }

    /**
     * {@inheritDoc}
     *
     * @param description the common goal description
     * @param token       the token won by achieving the common goal
     */
    public void addAchievedCommonGoal(String description, Token token) {
        this.achievedCommonGoals.put(description, token);
        this.setChanged();
        this.notifyObservers(Event.COMMON_GOAL_ACHIEVED);
    }

    /**
     * {@inheritDoc}
     *
     * @param error the error description
     */
    @Override
    public void reportError(String error) {
        this.errorReport = error;
        this.setChanged();
        this.notifyObservers(Event.ERROR_REPORTED);
    }

    /**
     * {@inheritDoc}
     *
     * @return A {@link PlayerInfo} representing this object instance
     */
    @Override
    public PlayerInfo getInfo() {
        return new PlayerInfo(this.errorReport, new HashMap<>(this.achievedCommonGoals));
    }
}
