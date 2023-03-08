package model;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private String matchId;
    private LivingRoom livingRoom;
    private Chat chat;
    private List<Player> players;
    private CommonGoal commonGoal_A, commonGoal_B;
    public Match(String matchId){
        this.matchId = matchId;
        this.livingRoom = new LivingRoom();
        this.chat = new Chat();
        this.players = new ArrayList<>();

        //TODO scegliere i common goal in modo più intelligente
        //this.commonGoal_A = new CommonGoal();
        //this.commonGoal_B = new CommonGoal();
    }

    public String getId(){
        return this.matchId;
    }

}
