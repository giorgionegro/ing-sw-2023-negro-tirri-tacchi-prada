package view.GUI.components;

import javax.swing.*;
import model.Tile;
import util.ResourceProvider;

import java.awt.*;

public class TileButton extends JButton {
    private final int tileX;
    private final int tileY;
    private final Tile tile;
    private final Image image;
    private final Image selectedImage = new ImageIcon(ResourceProvider.getResourcePath("/TileSelectedFilter.png")).getImage();

    public TileButton(int x, int y, Tile tile){
        this.tileX = x;
        this.tileY = y;
        this.tile = tile;

        this.setPreferredSize(new Dimension(0,0));
        image = new ImageIcon(ResourceProvider.getResourcePath("/Tile/"+tile.name()+".png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        if (isSelected()) {
            g.drawImage(selectedImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public Tile getTile() {
        return tile;
    }


}
