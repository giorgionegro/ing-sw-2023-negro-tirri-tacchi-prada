package view.GUI.panels;

import modelView.PickedTile;
import view.GUI.buttons.SwapButton;
import view.GUI.buttons.TileButton;
import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This class extends JPanel and represents a graphical component whose purpose is to allow the user to choose the order of the tiles
 */
//TODO
public class TilesOrderingPanel extends JPanel implements ActionListener{
    /**
     * List of the tiles that have been picked by the user
     */
    private final List<TileButton> pickedTiles = new ArrayList<>();
    /**
     * The ActionListener that will be notified when the user confirms the selection
     */
    private final ActionListener moveSender;

    /**
     * Code that represents the action of selecting a tile
     */
    public static final int SELECT_TILE = 0;
    /**
     * Code that represents the action of resetting the selection
     */
    public static final int RESET = 1;

    /**
     * Code that represents the action of switching the first and the second tile
     */
    private final int SWITCH_ONE_TWO = 3;
    /**
     * Code that represents the action of switching the second and the third tile
     */
    private final int SWITCH_TWO_THREE = 4;
    /**
     * Code that represents the action of confirming the selection and sending the move
     */
    public static final int CONFIRM = 2;

    /*------------------ GRAPHIC LAYOUT ---------------------*/


    /**
     * Background image of the panel
     */
    private final Image background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/TilesOrderingTableBackground.png"))).getImage();
    /**
     * Swap button that allows the user to switch the first and the second tile
     */
    private final SwapButton swapButton1 = new SwapButton();
    /**
     * Swap button that allows the user to switch the second and the third tile
     */
    private final SwapButton swapButton2 = new SwapButton();

    /**
     * Define a zero dimension
     */
    private final Dimension zeroDimension = new Dimension(0, 0);
    /**
     * Action listener that allows the user to choose the column of the tile
     */
    private final ActionListener columnChooser;

    /**
     * Constructs a {@link TilesOrderingPanel} instance
     *
     * @param moveSender  the ActionListener that will be notified when the user confirms the selection
     * @param columnChooser the ActionListener that will be notified when the user chooses the column of the tile
     */
    public TilesOrderingPanel(ActionListener moveSender, ActionListener columnChooser) {
        super();
        this.columnChooser = columnChooser;
        this.moveSender = moveSender;

        this.initializeLayout();

        this.swapButton1.addActionListener(e -> this.actionPerformed(new ActionEvent(this.swapButton1,this.SWITCH_ONE_TWO,e.getActionCommand())));
        this.swapButton2.addActionListener(e -> this.actionPerformed(new ActionEvent(this.swapButton2,this.SWITCH_TWO_THREE,e.getActionCommand())));
    }

    /**
     * {@inheritDoc}
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getID() == RESET) {
            //Resets the selection if there is an update of livingRoom
            for (TileButton t : this.pickedTiles) {
                t.setSelected(false);
            }
            this.pickedTiles.clear();
        } else if (e.getID() == this.SWITCH_ONE_TWO) {
            if (this.pickedTiles.size() == 3) {
                this.pickedTiles.add(this.pickedTiles.remove(1));
            }
        } else if (e.getID() == this.SWITCH_TWO_THREE) {
            if (this.pickedTiles.size() > 1) {
                this.pickedTiles.add(0, this.pickedTiles.remove(1));
            }
        } else if (e.getID() == CONFIRM) {
            if (this.pickedTiles.size() > 0) {
                String columnInShelf = e.getActionCommand();
                StringBuilder picked = new StringBuilder();
                for (TileButton t : this.pickedTiles) {
                    picked.append(t.getTileY()).append(",").append(t.getTileX()).append(" ");
                    t.setSelected(false);
                }
                this.pickedTiles.clear();
                String move = picked + "\n" + columnInShelf;
                this.moveSender.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MOVE, move));
            }
        } else if (e.getID() == SELECT_TILE && e.getSource() instanceof TileButton button) {
            boolean selected = button.isSelected();
            if (selected) {
                button.setSelected(false);
                this.pickedTiles.remove(button);
            } else {
                if (this.pickedTiles.size() < 3 && this.isPickable(button)) {
                    button.setSelected(true);
                    this.pickedTiles.add(button);
                }
            }
        }

        this.revalidate();
        this.repaint();

        this.columnChooser.actionPerformed(new ActionEvent(this, 0, String.valueOf(this.pickedTiles.size())));
    }

    /**
     * This method check if a tile is pickable in relation to the tiles that have already been picked
     *
     * @param Tile {@link TileButton} to be checked
     * @return true if the tile is pickable, false otherwise
     */
    private boolean isPickable(TileButton Tile) {

        List<PickedTile> temp = new ArrayList<>();
        for (TileButton t : this.pickedTiles) {
            temp.add(new PickedTile(t.getTileX(), t.getTileY()));
        }
        temp.add(new PickedTile(Tile.getTileX(), Tile.getTileY()));

        return this.pickable(temp);
    }


    /**
     * This method checks if a list of {@link PickedTile}s is pickable
     *
     * @param pickedTiles the tiles to be checked
     * @return true if the tiles are pickable, false otherwise
     */
    public boolean pickable(List<PickedTile> pickedTiles) {
        if (!this.areTilesDifferent(new ArrayList<>(pickedTiles))) {
            // print error this("Tiles are not different", RED);
            return false;
        }
        // print error  this.printCommandLine("Tiles are not aligned", RED);
        return this.areTilesAligned(new ArrayList<>(pickedTiles));
    }

    /**
     * This method checks if a list of {@link PickedTile}s are adjacent and aligned
     *
     * @param pickedTiles the tiles to be checked
     * @return true if the tiles are aligned and adjacent, false otherwise
     */
    private boolean areTilesAligned(List<PickedTile> pickedTiles) {

        boolean rowAligned = true;
        boolean colAligned = true;

        for (int i = 1; i < pickedTiles.size(); i++) {
            rowAligned = rowAligned && (pickedTiles.get(i - 1).row() == pickedTiles.get(i).row());
            colAligned = colAligned && (pickedTiles.get(i - 1).col() == pickedTiles.get(i).col());
        }

        if (rowAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::col));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).col() + 1 != pickedTiles.get(i + 1).col())
                    return false;
        }

        if (colAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::row));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).row() + 1 != pickedTiles.get(i + 1).row())
                    return false;
        }


        return rowAligned || colAligned;
    }

    /**
     * This method checks if a list of {@link PickedTile}s are different
     *
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(List<PickedTile> pickedTiles) {
        for (int i = 0; i < pickedTiles.size() - 1; i++) {
            for (int j = i + 1; j < pickedTiles.size(); j++) {
                if (pickedTiles.get(i).row() == pickedTiles.get(j).row())
                    if (pickedTiles.get(i).col() == pickedTiles.get(j).col())
                        return false;
            }
        }
        return true;
    }

    /**
     * This method initializes the layout of the panel
     */
    private void initializeLayout() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints(
                0, 0,
                1, 1,
                1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        /* up space */
        constraints.gridwidth = 3;
        constraints.weighty = 30;
        this.add(new Container(), constraints);

        /* tile space */
        constraints.gridy++;
        constraints.weighty = 146;
        this.add(new Container(), constraints);

        /* swap button 1 */
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.weighty = 70;
        this.add(new Container(), constraints);

        constraints.gridx++;
        this.swapButton1.setPreferredSize(this.zeroDimension);
        this.add(this.swapButton1, constraints);

        constraints.gridx++;
        this.add(new Container(), constraints);

        /* tile space */
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        constraints.weighty = 146;
        this.add(new Container(), constraints);

        /* swap button 2 */
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.weighty = 70;
        this.add(new Container(), constraints);

        constraints.gridx++;
        this.swapButton2.setPreferredSize(this.zeroDimension);
        this.add(this.swapButton2, constraints);

        constraints.gridx++;
        this.add(new Container(), constraints);

        /* tile space */
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        constraints.weighty = 146;
        this.add(new Container(), constraints);

        /* bottom space */
        constraints.gridy++;
        constraints.weighty = 30;
        this.add(new Container(), constraints);
    }


    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double ratio = (double) this.getWidth() / this.background.getWidth(null);
        int topPadding = (int) Math.round(30 * ratio);
        int leftPadding = (int) Math.round(30 * ratio);
        int size = (int) Math.round(145 * ratio);
        int vVertical = (int) Math.round(70 * ratio);

        int verticalSkip = size + vVertical;

        g.drawImage(this.background, 0, 0, this.getWidth(), this.getHeight(), null);

        int y = topPadding + 2 * verticalSkip;
        for (TileButton t : this.pickedTiles) {
            Image tileImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Tile/" + t.getTile().name() + ".png"))).getImage();
            g.drawImage(tileImage, leftPadding, y, size, size, null);
            y -= verticalSkip;
        }
    }
}
