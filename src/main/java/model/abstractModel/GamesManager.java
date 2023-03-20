package model.abstractModel;

import model.Match;

import java.util.List;


public interface GamesManager {
    public void addMatch(Match newMatch);

    public List<Match> getMatches();

    public Match getMatch();
}
