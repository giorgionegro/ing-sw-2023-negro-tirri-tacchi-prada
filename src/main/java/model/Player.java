package model;

import model.abstractModel.PersonalGoal;

import java.util.List;

public class Player {

    private String idPlayer;
    private Tile[][] shelf;
    private int points;
    private List<PersonalGoal> personalGoal;

    private List<Token> tokens;

    public Player(){
        throw new UnsupportedOperationException("Not implemented yet");
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

    public void assignToken(Token newToken){
        throw new UnsupportedOperationException("Not implemented yet");
    }


}
