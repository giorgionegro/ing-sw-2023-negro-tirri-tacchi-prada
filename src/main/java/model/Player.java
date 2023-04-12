package model;

import model.abstractModel.CommonGoal;
import model.abstractModel.PersonalGoal;

import java.util.List;
import java.util.Map;

/**
 * Class that represents a player
 */
public class Player {

    /**
     * id of the player
     */
    private String idPlayer;
    /**
     * shelf of the player (2D array of tiles)
     */
    private Tile[][] shelf;
    /**
     * personal goals of the player (list of personal goals)
     */
    private List<PersonalGoal> personalGoal;

    /**
     * common goals of the player (map of common goals and tokens)
     */
    private Map<CommonGoal, Token> commonGoals;

    /**
     * @param idPlayer id of the player
     * @param shelf shelf of the player
     * @param personalGoal personal goals of the player
     */
    public Player(String idPlayer, Tile[][] shelf, List<PersonalGoal> personalGoal){ //forse conviene aggiunfere un costruttore senza la shelf che la inizializza a shelf vuota
        this.idPlayer = idPlayer;
        this.shelf = shelf;
        this.personalGoal = personalGoal;
    }

    /**
     * @return id of the player
     */
    public String getId(){
        return idPlayer;
    }

    /**
     * @return shelf of the player
     */
    public Tile[][] getShelf() {
        throw new UnsupportedOperationException("Not implemented yet"); //TODO meglio passare una copia di shelf non un riferimento alla stessa, vedi cloneTileMatrix in evaluators_utils
    }

    /**
     * @param modifiedShelf modified shelf of the player
     */
    public void setShelf(Tile[][] modifiedShelf){
        throw new UnsupportedOperationException("Not implemented yet"); //TODO meglio copiare la modifiedShelf
    }

    /**
     * @return personal goals of the player
     */
    public List<PersonalGoal> getPersonalGoal() {
        return personalGoal;
    }

    /**
     * @return common goals of the player
     */
    public Map<CommonGoal,Token> getCommonGoals(){ return commonGoals;}
}
