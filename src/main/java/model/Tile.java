package model;

/**
 * This enumeration contains all the possible tiles that can appear in the game
 */
public enum Tile {
    /**
     * Green tile variant 1
     */
    CATS_1("Green"),
    /**
     * Green tile variant 2
     */
    CATS_2("Green"),
    /**
     * Green tile variant 3
     */
    CATS_3("Green"),
    /**
     * White tile variant 1
     */
    BOOKS_1("White"),
    /**
     * White tile variant 2
     */
    BOOKS_2("White"),
    /**
     * White tile variant 3
     */
    BOOKS_3("White"),
    /**
     * Yellow tile variant 1
     */
    GAMES_1("Yellow"),
    /**
     * Yellow tile variant 2
     */
    GAMES_2("Yellow"),
    /**
     * Yellow tile variant 3
     */
    GAMES_3("Yellow"),
    /**
     * Blue tile variant 1
     */
    FRAMES_1("Blue"),
    /**
     * Blue tile variant 2
     */
    FRAMES_2("Blue"),
    /**
     * Blue tile variant 3
     */
    FRAMES_3("Blue"),
    /**
     * LightBlue tile variant 1
     */
    TROPHIES_1("LightBlue"),
    /**
     * LightBlue tile variant 2
     */
    TROPHIES_2("LightBlue"),
    /**
     * LightBlue tile variant 3
     */
    TROPHIES_3("LightBlue"),
    /**
     * Magenta tile variant 1
     */
    PLANTS_1("Magenta"),
    /**
     * Magenta tile variant 2
     */
    PLANTS_2("Magenta"),
    /**
     * Magenta tile variant 3
     */
    PLANTS_3("Magenta"),
    /**
     * Empty tile variant 1
     */

    EMPTY("Empty");

    /**
     * The color of the tile, there can be many tiles with the same color
     */
    private final String color;

    /**
     * The color of the tile
     * @param color a string representation of the color
     */
    Tile(String color){
        this.color = color;
    }

    /**
     * This method returns the color of the tile
     * @return {@link #color}
     */
    public String getColor() {
        return color;
    }
}
