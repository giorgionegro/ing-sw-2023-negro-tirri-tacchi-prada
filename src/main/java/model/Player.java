package model;

import model.abstractModel.CommonGoal;
import model.abstractModel.PersonalGoal;

import java.util.List;
import java.util.Map;

public class Player {

    private String idPlayer;
    private Tile[][] shelf;
    private List<PersonalGoal> personalGoal;

    private Map<CommonGoal, Token> commonGoals;

    public Player(String idPlayer, Tile[][] shelf, List<PersonalGoal> personalGoal){
        this.idPlayer = idPlayer;
        this.shelf = shelf;
        this.personalGoal = personalGoal;
    }

    public String getId(){
        return idPlayer;
    }

    public Tile[][] getShelf() {
        throw new UnsupportedOperationException("Not implemented yet"); //TODO meglio passare una copia di shelf non un riferimento alla stessa, vedi cloneTileMatrix in evaluators_utils
    }

    public void setShelf(Tile[][] modifiedShelf){
        throw new UnsupportedOperationException("Not implemented yet"); //TODO meglio copiare la modifiedShelf
    }

    public List<PersonalGoal> getPersonalGoal() {
        return personalGoal;
    }

    public Map<CommonGoal,Token> getCommonGoals(){ return commonGoals;}
}
