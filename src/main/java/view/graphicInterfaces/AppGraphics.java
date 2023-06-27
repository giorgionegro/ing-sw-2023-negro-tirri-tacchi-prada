package view.graphicInterfaces;


import java.awt.event.ActionListener;

/**
 * This interface represents the graphics functionalities of the application.
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

    /**
     * This method asks graphics to show the connection page, showing an error if necessary
     * @param error the error message to be displayed
     */
    void showConnection(String error);

    /**
     * This method asks graphics to show the server-interaction page, showing a message if necessary
     * @param message the message to be displayed
     */
    void showServerInteraction(String message);

    /**
     This method asks graphics to show the join game page, showing an error if necessary
     * @param error the error message to be displayed
     *
     */
    void showJoin(String error);

    /**
     This method asks graphics to show the crete game page, showing an error if necessary
     * @param error the error message to be displayed
     */
    void showCreate(String error);

    /**
     * This method asks graphics to show the Game page, showing a message if necessary
     * @param message the message to be displayed
     */
    void showGame(String message);

    /**
     *This method asks graphics to close
     */
    void exit();
}
