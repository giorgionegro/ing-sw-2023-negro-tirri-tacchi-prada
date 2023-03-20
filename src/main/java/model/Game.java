package model;

import model.abstractModel.Chat;
import model.abstractModel.CommonGoal;
import model.abstractModel.LivingRoom;

import java.util.List;
import java.util.Map;

public class Game {
    private Map<String,Player> players;

    private List<CommonGoal> commonGoals;

    private LivingRoom livingRoom;

    private Chat chat;

    public void addPlayer(String playerId){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Player getPlayer(String playerId){
        throw new UnsupportedOperationException("Not implemented yet");
    }
}