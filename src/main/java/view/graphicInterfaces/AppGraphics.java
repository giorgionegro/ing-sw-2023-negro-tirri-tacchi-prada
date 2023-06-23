package view.graphicInterfaces;


import java.awt.event.ActionListener;

public interface AppGraphics {
    void setActionListener(ActionListener actionListener);
    GameGraphics getGameGraphics();
    void showConnection(String error);
    void showServerInteraction(String message);
    void showJoin(String error);
    void showCreate(String error);
    void showGame(String message);
    void exit();
}
