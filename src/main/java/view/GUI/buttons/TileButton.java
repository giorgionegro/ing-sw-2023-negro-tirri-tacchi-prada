package view.GUI.buttons;

import javax.swing.*;
import model.Tile;


import java.awt.*;
import java.util.Objects;

/**
 * This class extends JButton and represents a graphical component that shows the tile button for selecting tiles in {@link view.GUI.panels.LivingRoomPanel}
 */
public class TileButton extends JButton {
    /**
     * This int contains the x coordinate of the tile
     */
    private final int tileX;
    /**
     * This int contains the y coordinate of the tile
     */
    private final int tileY;
    /**
     * The Tile that this button represents
     */
    private final Tile tile;
    /**
     * This image contains the image of the tile
     */
    private final Image image;
    /**
     * This image contains the image of the selected tile
     */
    private final Image selectedImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ForegroundFilter.png"))).getImage();

    /**
     * Construct an {@link TileButton} instance
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @param tile the tile that this button represents
     */
    public TileButton(int x, int y, Tile tile) {
        super();
        this.tileX = x;
        this.tileY = y;
        this.tile = tile;

        this.setPreferredSize(new Dimension(0, 0));
        this.image = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Tile/" + tile.name() + ".png"))).getImage();
    }

    /**
     * This method overrides {@link JButton#paintComponent(Graphics)} and draws the image of the tile
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);
        if (this.isSelected()) {
            g.drawImage(this.selectedImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    /**
     * This method returns the x coordinate of the tile
     * @return the x coordinate of the tile
     */
    public int getTileX() {
        return this.tileX;
    }

    /**
     * This method returns the y coordinate of the tile
     * @return the y coordinate of the tile
     */
    public int getTileY() {
        return this.tileY;
    }

    /**
     * This method returns the tile that this button represents
     * @return the tile that this button represents
     */
    public Tile getTile() {
        return this.tile;
    }


}
