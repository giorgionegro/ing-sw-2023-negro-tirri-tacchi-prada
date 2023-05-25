package view.GUI;

import javax.swing.*;
import model.Tile;
import view.ResourceProvider;

import java.awt.*;

public class TileButton extends JButton {
    int tileX;
    int tileY;
    Tile tile;
    ImageIcon image;
    ImageIcon selectedImage;

    public TileButton(int x, int y, Tile tile){
        this.tileX = x;
        this.tileY = y;
        this.tile = tile;

        image = new ImageIcon(GamePanel.ReturnImageTile(tile));
        selectedImage = new ImageIcon(ResourceProvider.getResourcePath()+"/selected.png");
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), null);
        if (isSelected()) {
            g.drawImage(selectedImage.getImage(), 0, 0, getWidth(), getHeight(), null);
        }
    }


   /* private ImageIcon darkenImage(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // set the transparency to 50%
        g2d.fillRect(0, 0, image.getWidth(null), image.getHeight(null)); // draw a transparent white rectangle over the top
        g2d.dispose();
        return new ImageIcon(bi);
    }
*/
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
