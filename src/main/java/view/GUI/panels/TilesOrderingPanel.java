package view.GUI.panels;

import modelView.PickedTile;
import view.GUI.components.SwapButton;
import view.GUI.components.TileButton;
import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class TilesOrderingPanel extends JPanel implements ActionListener {
    private final List<TileButton> pickedTiles = new ArrayList<>();
    private final ActionListener moveSender;
    /*------------------ GRAPHIC LAYOUT ---------------------*/
    private final Image background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/TilesOrderingTableBackground.png"))).getImage();
    private final SwapButton swapButton1 = new SwapButton();
    private final SwapButton swapButton2 = new SwapButton();
    private final GridBagConstraints constraints = new GridBagConstraints(
            0, 0,
            1, 1,
            1, 1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );
    private final Dimension zeroDimension = new Dimension(0, 0);
    private ActionListener columnChooser;

    public TilesOrderingPanel(ActionListener moveSender) {
        super();
        this.moveSender = moveSender;

        this.initializeLayout();

        this.swapButton1.addActionListener(this);
        this.swapButton2.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.moveSender) {
            //Resets the selection if there is an update of livingRoom
            for (TileButton t : this.pickedTiles) {
                t.setSelected(false);
            }
            this.pickedTiles.clear();
        } else if (e.getSource() == this.swapButton1) {
            if (this.pickedTiles.size() == 3) {
                this.pickedTiles.add(this.pickedTiles.remove(1));
            }
        } else if (e.getSource() == this.swapButton2) {
            if (this.pickedTiles.size() > 1) {
                this.pickedTiles.add(0, this.pickedTiles.remove(1));
            }
        } else if (e.getSource() == this.columnChooser) {

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

        } else if (e.getSource() instanceof TileButton button) {

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

    public void setColumnChooser(ActionListener columnChooser) {
        this.columnChooser = columnChooser;
    }

    private boolean isPickable(TileButton button) {

        List<PickedTile> temp = new ArrayList<>();
        for (TileButton t : this.pickedTiles) {
            temp.add(new PickedTile(t.getTileX(), t.getTileY()));
        }
        temp.add(new PickedTile(button.getTileX(), button.getTileY()));
        //TODO implementa algoritmo
        return this.pickable(temp);
    }


    public boolean pickable(List<PickedTile> pickedTiles) {
        if (!this.areTilesDifferent(new ArrayList<>(pickedTiles))) {
            // print error this("Tiles are not different", RED);
            return false;
        }
        // print error  this.printCommandLine("Tiles are not aligned", RED);
        return this.areTilesAligned(new ArrayList<>(pickedTiles));
    }

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


    private void initializeLayout() {
        this.setLayout(new GridBagLayout());

        /* up space */
        this.constraints.gridwidth = 3;
        this.constraints.weighty = 30;
        this.add(new Container(), this.constraints);

        /* tile space */
        this.constraints.gridy++;
        this.constraints.weighty = 146;
        this.add(new Container(), this.constraints);

        /* swap button 1 */
        this.constraints.gridy++;
        this.constraints.gridwidth = 1;
        this.constraints.weighty = 70;
        this.add(new Container(), this.constraints);

        this.constraints.gridx++;
        this.swapButton1.setPreferredSize(this.zeroDimension);
        this.add(this.swapButton1, this.constraints);

        this.constraints.gridx++;
        this.add(new Container(), this.constraints);

        /* tile space */
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        this.constraints.gridwidth = 3;
        this.constraints.weighty = 146;
        this.add(new Container(), this.constraints);

        /* swap button 2 */
        this.constraints.gridy++;
        this.constraints.gridwidth = 1;
        this.constraints.weighty = 70;
        this.add(new Container(), this.constraints);

        this.constraints.gridx++;
        this.swapButton2.setPreferredSize(this.zeroDimension);
        this.add(this.swapButton2, this.constraints);

        this.constraints.gridx++;
        this.add(new Container(), this.constraints);

        /* tile space */
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        this.constraints.gridwidth = 3;
        this.constraints.weighty = 146;
        this.add(new Container(), this.constraints);

        /* bottom space */
        this.constraints.gridy++;
        this.constraints.weighty = 30;
        this.add(new Container(), this.constraints);
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
