package model;

public enum Tile {

    CATS("Green"),
    BOOKS("White"),
    GAMES("Yellow"),
    FRAMES("Blue"),
    TROPHIES("LightBlue"),
    PLANTS("Magenta");

    private String color;
    Tile(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
