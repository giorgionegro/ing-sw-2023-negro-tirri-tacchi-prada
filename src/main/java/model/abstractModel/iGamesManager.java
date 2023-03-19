package model.abstractModel;

import model.Match;

import java.util.List;


public interface iGamesManager {
    public void addMatch(Match newMatch);

    public List<Match> getMatches();

    public Match getMatch();
}
