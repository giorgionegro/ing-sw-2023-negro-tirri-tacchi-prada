package model;

/**
 * This class represents a collection of information that characterize a game
 */
public class GameInfo {
    /**
     * The game id
     */
    private final String gameId;
    /**
     * The {@link String} representation of game type
     */
    private final String type;
    /**
     * The number of player that are going to play the game
     */
    private final int playerNumber;
    /**
     * Construct an instance with given game id, type and player number
     * @param gameId the game id
     * @param type the {@link String} representation of the game type
     * @param playerNumber the number of player that are going to play the game
     */
    public GameInfo(String gameId, String type, int playerNumber){
        this.gameId = gameId;
        this.type = type;
        this.playerNumber = playerNumber;
    }
    /**
     * This method returns the {@link String} representation of game type
     * @return {@link #type}
     */
    public String getType() {
        return type;
    }
    /**
     * This method return the number of player that are going to play the game
     * @return {@link #playerNumber}
     */
    public int getPlayerNumber() {
        return playerNumber;
    }
    /**
     * This method returns the game id
     * @return {@link #gameId}
     */
    public String getGameId() {
        return gameId;
    }
}
