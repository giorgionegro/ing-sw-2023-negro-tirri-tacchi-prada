package model;

import model.abstractModel.GamesManager;

import java.util.List;
import java.util.Map;

public class StandardGamesManager implements GamesManager {

    private Map<String,Game> games;

    @Override
    public void addMatch(Game newMatch) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Game> getMatches() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Game getMatch() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
