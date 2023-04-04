package model.abstractModel;

import model.Player;
import model.exceptions.PlayerAlreadyExistsException;
import model.exceptions.PlayerNotExistsException;

import java.util.List;

public interface Game {
    void addPlayer(String playerId) throws PlayerAlreadyExistsException;

    Player getPlayer(String playerId) throws PlayerNotExistsException;

    List<Player> getPlayers();

    LivingRoom getLivingRoom();
}