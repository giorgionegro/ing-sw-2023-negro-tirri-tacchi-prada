package model;

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

    private final String color;
    Tile(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
