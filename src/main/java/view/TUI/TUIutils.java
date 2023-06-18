package view.TUI;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class TUIutils {
    private TUIutils(){}

    public static final int WHITE = 37;
    public static final int GREEN = 32;
    public static final int YELLOW = 33;
    public static final int BLUE = 34;
    public static final int MAGENTA = 35;
    public static final int CYAN = 36;
    public static final int RED = 31;
    public static final int DEFAULT = 39;

    /** This method returns the numeric value of a String that represents a color
     * @param color String of a certain color
     * @return integer value of said color
     */
    public static int getColour(String color) {
        int colour;
        switch (color) {
            case "Green" -> colour = GREEN;
            case "White" -> colour = WHITE;
            case "Yellow" -> colour = YELLOW;
            case "Blue" -> colour = BLUE;
            case "LightBlue" -> colour = CYAN;
            case "Magenta" -> colour = MAGENTA;
            default -> colour = DEFAULT;
        }
        return colour;
    }

    /**
     * //TODO function javadoc
     * This method checks what OS the game is being run on and clears the terminal.
     * @param function callback to be run on screen cleaning error
     */
    public static void ClearScreen(Consumer<String> function) {
        try {
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor(10, TimeUnit.MILLISECONDS);
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor(10, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            function.accept("Error: " + e.getMessage());
        }
    }

}
