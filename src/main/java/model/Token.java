package model;

/**
 * This enumerable contains all the possible token of the game
 * Every instance is characterized by the points that represent
 */
public enum Token {
    /**
     * This token represents 8 points
     */
    TOKEN_8_POINTS(8),
    /**
     * This token represents 6 points
     */
    TOKEN_6_POINTS(6),
    /**
     * This token represents 4 points
     */
    TOKEN_4_POINTS(4),
    /**
     * This token represents 2 points
     */
    TOKEN_2_POINTS(2),
    /**
     * This token represents 0 points, represents an empty stack of token
     */
    TOKEN_EMPTY(0),
    /**
     * This token represents 1 points
     * <p>
     * Player that obtain this token is the first that has completed the shelf
     */
    TOKEN_GAME_END(1);

    /**
     * The points amount
     */
    private final int points;

    /**
     * Construct a token that rwith given points
     * @param points the token points amount
     */
    Token(int points){
        this.points = points;
    }

    /**
     * This method returns the token points amount
     * @return {@link #points}
     */
    public int getPoints(){
        return points;
    }
}
