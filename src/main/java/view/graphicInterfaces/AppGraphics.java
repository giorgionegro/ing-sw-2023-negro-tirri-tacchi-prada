package view.graphicInterfaces;


import java.awt.event.ActionListener;

/**
 * This interface represents the graphics functionalities of an application.
 * It provides methods to set action listeners, retrieve game graphics, show connection status, server interaction messages,
 * join and create errors, show game messages, and exit the application.
 */
public interface AppGraphics {
    /**
     * Sets an action listener for the application graphics.
     * @param actionListener the action listener to be set
     */
    void setActionListener(ActionListener actionListener);

    /**
     * This method recovers the game graphics associated with the application
     * @return the GameGraphics object representing the game graphics
     */
    GameGraphics getGameGraphics();

    /** This method shows an error message
     * @param error the error message to be displayed
     */
    void showConnection(String error);

    /** This method shows the server interaction message
     * @param message the message to be displayed
     */
    void showServerInteraction(String message);

    /** This method shows the error message
     * @param error the error message to be displayed
     *
     */
    void showJoin(String error);

    /**
     * @param error
     */
    void showCreate(String error);

    /**
     * @param message
     */
    void showGame(String message);

    /**
     *
     */
    void exit();
}
