package view.TUI;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class contains utility methods for the TUI
 */
final class TUIutils {

    /**
     * This is the ANSI codes for white
     */
    public static final int WHITE = 37;
    /**
     * This is the ANSI codes for green
     */
    public static final int GREEN = 32;
    /**
     * This is the ANSI codes for yellow
     */
    public static final int YELLOW = 33;
    /**
     * This is the ANSI codes for blue
     */
    public static final int BLUE = 34;
    /**
     * This is the ANSI codes for magenta
     */
    public static final int MAGENTA = 35;
    /**
     * This is the ANSI codes for cyan
     */
    public static final int CYAN = 36;
    /**
     * This is the ANSI codes for red
     */
    public static final int RED = 31;
    /**
     * This is the ANSI codes for default
     */
    public static final int DEFAULT = 39;

    /**
     * @hidden
     */
    private TUIutils() {
    }

    /**
     * This method returns the numeric value of a String that represents a color
     *
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
     * This method clears the terminal.
     *
     * @param function callback to be run on screen cleaning error
     */
    public static void ClearScreen(Consumer<? super String> function) {
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
