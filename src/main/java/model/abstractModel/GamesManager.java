package model.abstractModel;

import model.Game;

import java.util.List;


public interface GamesManager {
    public void addMatch(Game newMatch);

    public List<Game> getMatches();

    public Game getMatch();
}
