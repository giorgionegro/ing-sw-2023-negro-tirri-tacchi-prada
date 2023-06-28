package view.GUI;

import view.GUI.panels.*;
import view.graphicInterfaces.AppGraphics;
import view.graphicInterfaces.GameGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * This class extends JFrame and represents the main frame of the GUI
 */
public class GUI extends JFrame implements AppGraphics, ActionListener {
    /**
     * This is the panel that allows the user to choose the network type
     */
    private final NetworkChoicePanel networkChoice = new NetworkChoicePanel(this);
    /**
     * This is the panel that allows the user to switch between join and create panels
     */
    private final HomePanel homePanel = new HomePanel(this);
    /**
     * This is the panel that allows the user to join a game
     */
    private final JoinGamePanel join = new JoinGamePanel(this);
    /**
     * This is the panel that allows the user to create a game
     */
    private final CreateGamePanel create = new CreateGamePanel(this);
    /**
     * This is the panel that allows the user to play the game
     */
    private final GamePanel game = new GamePanel(this);
    /**
     * root container of the frame
     */
    private final Container root;

    /**
     * view logic
     */
    private ActionListener viewLogic;

    /**
     * Construct an instance of this class initializing the frame
     */
    public GUI(){
        super();

        this.setLayout(new BorderLayout());

        Image icon = new ImageIcon (Objects.requireNonNull(this.getClass().getResource("/AppIcon.png"))).getImage();
        this.setIconImage(icon);

        this.setTitle("My Shelfie");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(new Dimension(1100, 700));
        this.setMinimumSize(new Dimension(825,525));

        this.setResizable(true);
        this.setVisible(true);

        this.root = this.getContentPane();
    }

    /**
     * This method refreshes the frame
     */
    public void refresh() {
        this.root.revalidate();
        this.root.repaint();
    }

    /**
     * {@inheritDoc}
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== this.game)
            this.viewLogic.actionPerformed(e);
        else
            this.viewLogic.actionPerformed(new ActionEvent(this,e.getID(),e.getActionCommand()));
    }

    /**
     * {@inheritDoc}
     * @param actionListener the action listener to be set
     */
    @Override
    public void setActionListener(ActionListener actionListener) {
        this.viewLogic = actionListener;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public GameGraphics getGameGraphics() {
        return this.game;
    }

    /**
     * {@inheritDoc}
     * @param error the error message to be displayed
     */
    @Override
    public void showConnection(String error) {
        this.networkChoice.setErrorMessage(error);
        this.root.removeAll();
        this.root.add(this.networkChoice);
        this.refresh();
    }

    /**
     * {@inheritDoc}
     * @param message the message to be displayed
     */
    @Override
    public void showServerInteraction(String message) {
        this.homePanel.setMessage(message);
        this.root.removeAll();
        this.root.add(this.homePanel);
        this.refresh();
    }

    /**
     * {@inheritDoc}
     * @param error the error message to be displayed
     */
    @Override
    public void showJoin(String error) {
        this.join.setMessage(error);
        this.root.removeAll();
        this.root.add(this.join);
        this.refresh();
    }

    /**
     * {@inheritDoc}
     * @param error the error message to be displayed
     */
    @Override
    public void showCreate(String error) {
        this.create.setMessage(error);
        this.root.removeAll();
        this.root.add(this.create);
        this.refresh();
    }

    /**
     * {@inheritDoc}
     * @param message the message to be displayed
     */
    @Override
    public void showGame(String message) {
        this.root.removeAll();
        this.root.add(this.game);
        this.refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exit() {
        System.exit(0);
    }
}