package model;

import model.abstractModel.*;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardGame implements Game {

    private Map<String, Player> players;

    private List<CommonGoal> commonGoals;

    private LivingRoom livingRoom;

    private Chat chat;

    public StandardGame(int playerNumber){
        this.chat = new StandardChat();
        this.commonGoals = new ArrayList<>(); //TODO da scegliere i common goals
        this.livingRoom = new StandardLivingRoom(playerNumber);
        this.players = new HashMap<>();
    }

    @Override
    public void addPlayer(String playerId) throws PlayerAlreadyExistsException {
        if(players.containsKey(playerId))
            throw new PlayerAlreadyExistsException();

        List<PersonalGoal> personalGoals = null; //TODO pick personalGoals
        Player newPlayer = new Player(playerId, new Tile[6][5], personalGoals);
        players.put(playerId,newPlayer);
    }

    @Override
    public Player getPlayer(String playerId) throws PlayerNotExistsException {
        if(!players.containsKey(playerId))
            throw new PlayerNotExistsException();
        return players.get(playerId);
    }

    @Override
    public List<Player> getPlayers() {
        return null;//TODO
    }

    @Override
    public LivingRoom getLivingRoom() {
        return null;//TODO
    }
}
