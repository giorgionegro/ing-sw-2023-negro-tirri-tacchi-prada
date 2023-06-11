package model;

/**
 * This enumeration contains all the possible tiles that can appear in the game
 */
public enum Tile {
    CATS_1("Green"),
    CATS_2("Green"),
    CATS_3("Green"),
    BOOKS_1("White"),
    BOOKS_2("White"),
    BOOKS_3("White"),
    GAMES_1("Yellow"),
    GAMES_2("Yellow"),
    GAMES_3("Yellow"),
    FRAMES_1("Blue"),
    FRAMES_2("Blue"),
    FRAMES_3("Blue"),
    TROPHIES_1("LightBlue"),
    TROPHIES_2("LightBlue"),
    TROPHIES_3("LightBlue"),
    PLANTS_1("Magenta"),
    PLANTS_2("Magenta"),
    PLANTS_3("Magenta"),

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
