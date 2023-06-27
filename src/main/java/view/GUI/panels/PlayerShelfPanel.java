package view.GUI.panels;

import model.Tile;

import view.GUI.buttons.ColumnChooserButton;
import view.graphicInterfaces.PlayerShelfGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class extends JPanel and represents a graphical component that shows the player shelf
 */
public class PlayerShelfPanel extends JPanel implements PlayerShelfGraphics, ActionListener {
    /**
     * Matrix of tiles that represents the player's shelf
     */
    private Tile[][] shelfState = new Tile[6][5];
    /**
     * List of column selector buttons
     */
    private final List<JButton> columnSelectorList = new ArrayList<>();
    /**
     * Boolean that represents if the player is the first player
     */
    private boolean isFirstPlayer;

    /**
     * Update choices event code
     */
    public static final int UPDATE_CHOICES = 0;

    /** Construct an {@link PlayerShelfPanel} instance that uses the given {@link ActionListener} as listener for the column buttons
     * @param orderingTable ActionListener used to send column selector events
     */
    public PlayerShelfPanel(ActionListener orderingTable) {
        super();
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());

        for (Tile[] r : this.shelfState)
            Arrays.fill(r, Tile.EMPTY);

        this.initializeLayout(orderingTable);

        this.isFirstPlayer = false;
    }

    /**{@inheritDoc}
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getID() == UPDATE_CHOICES) {
            int n_picked = Integer.parseInt(e.getActionCommand());
            for (int i = 0; i < this.columnSelectorList.size(); i++) {
                this.columnSelectorList.get(i).setEnabled(this.countEmpty(i) >= n_picked);
            }
        }
    }

    /**
     This method counts the number of empty tiles in a specific column of the shelf state.
     @param column the column index to count the empty tiles in.
     @return the number of empty tiles in the specified column.
     */
    private int countEmpty(int column){
        int count = 0;
        for (Tile[] tiles : this.shelfState) {
            if (tiles[column] == Tile.EMPTY)
                count++;
        }
        return count;
    }


    /**
     * This method enable or disable the column chooser buttons based on parameter value
     * @param enable if enable or not the buttons
     */
    public void enableButtons(boolean enable){
        for(JButton b : this.columnSelectorList){
            b.setVisible(enable);
        }
    }

    /** {@inheritDoc}
     * @param playerId id of the player that owns the shelf
     * @param shelf    shelf representation of the player
     */
    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        this.shelfState = shelf;
        this.revalidate();
        this.repaint();
    }

    /**
     * This method sets whether the player is the first player.
     * @param isFirstPlayer true if the player is the first player, false otherwise.
     */
    public void setIsFirstPlayer(boolean isFirstPlayer){
        this.isFirstPlayer = isFirstPlayer;
    }

    /* ------------------ GRAPHIC LAYOUT ---------------------*/

    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image foreground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BookshelfForeground.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BookshelfBackground.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image firstPlayerOverlay = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/FirstPlayerOverlay.png"))).getImage();

    /**
     This method initializes the layout and the contents of the panel;
     * The positions and dimensions of components within the grid are set.
     */
    private void initializeLayout(ActionListener orderingTable){
        GridBagConstraints leftSpacerConstraints = new GridBagConstraints(
                0,0,
                1,1,
                144,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );

        GridBagConstraints rightSpacerConstraints = new GridBagConstraints(
                7,0,
                1,1,
                144,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );

        GridBagConstraints buttonsConstraints = new GridBagConstraints(
                0,0,
                1,1,
                270,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );

        this.add(new Container(),leftSpacerConstraints);

        for (int i = 0; i < 5; i++) {
            buttonsConstraints.gridx = 1+i;

            ColumnChooserButton insert = new ColumnChooserButton(i);
            insert.addActionListener(orderingTable);
            this.columnSelectorList.add(insert);
            this.add(insert,buttonsConstraints);
        }

        this.add(new Container(),rightSpacerConstraints);

        Container shelfSpacer = new Container();
        GridBagConstraints spacerConstraints = new GridBagConstraints(
                0,1,
                7,1,
                1,7.4,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );

        this.add(shelfSpacer,spacerConstraints);
    }

    /**
     * This method overrides {@link JComponent#paintComponent(Graphics)} drawing the {@link #shelfState} on background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double ratio = (double) this.getWidth()/ this.foreground.getWidth(null);
        int buttonPadding = (int)Math.round(171*ratio);
        int topPadding = (int)Math.round(79*ratio) + buttonPadding;
        int leftPadding = (int)Math.round(144*ratio);
        int size = (int) Math.round((145+18)*ratio);
        int vHorizontal = (int) Math.round(34*ratio);
        int vVertical = (int) Math.round(6*ratio);

        int horizontalSkip = size + vHorizontal;
        int verticalSkip = size + vVertical;

        g.drawImage(this.background,0,buttonPadding, this.getWidth(), this.getHeight()-buttonPadding,null);

        int y = topPadding;
        for (Tile[] tiles : this.shelfState) {
            int x = leftPadding;
            for (int j = 0; j < this.shelfState[0].length; j++) {
                if (tiles[j] != Tile.EMPTY) {
                    Image tileImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Tile/" + tiles[j].name() + ".png"))).getImage();
                    g.drawImage(tileImage, x, y, size, size, null);
                }
                x += horizontalSkip;
            }
            y += verticalSkip;
        }

        g.drawImage(this.foreground,0,buttonPadding, this.getWidth(), this.getHeight()-buttonPadding,null);

        if(this.isFirstPlayer){
            g.drawImage(this.firstPlayerOverlay,0,buttonPadding, this.getWidth(), this.getHeight()-buttonPadding,null);
        }
    }
}
