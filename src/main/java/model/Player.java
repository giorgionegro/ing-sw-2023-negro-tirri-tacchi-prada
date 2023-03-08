package model;

public class Player {

    private String idPlayer;
    private Tile[][] shelf;
    private PersonalGoal personalGoal;
    public Player(String idPlayer){
        this.idPlayer = idPlayer;
        this.shelf = new Tile[5][6];
        //TODO Assegnagnare il personalGoal
    }

    public String getId(){
        return idPlayer;
    }

    public Tile[][] getShelf() {
        throw new UnsupportedOperationException("Not implemented yet"); //TODO meglio passare una copia di shelf non un riferimento alla stessa
    }

    public void setShelf(Tile[][] modifiedShelf){
        throw new UnsupportedOperationException("Not implemented yet"); //TODO meglio copiare la modifiedShelf
    }
}
