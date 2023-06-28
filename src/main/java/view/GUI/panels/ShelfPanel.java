package view.GUI.panels;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

import model.Tile;

import view.graphicInterfaces.PlayerShelfGraphics;

/**
 * This class extends JPanel and represents a graphical component that shows the other players shelf
 */

public class ShelfPanel extends JPanel implements PlayerShelfGraphics {
    /**
     * Matrix of tiles that represents the player's shelf
     */
    private Tile[][] shelfState = new Tile[6][5];
    /**
     * Boolean that represents if the player is the first player
     */
    private boolean isFirstPlayer;

    /**
     * Construct an {@link ShelfPanel} instance
     */
    public ShelfPanel() {
        super();
        this.setOpaque(false);
        for (Tile[] r : this.shelfState)
            Arrays.fill(r, Tile.EMPTY);
    }

    /**
     *  Set if the player is the first player
     *
     * @param isFirstPlayer the boolean that represents if the player is the first player
     */
    public void setIsFirstPlayer(boolean isFirstPlayer){
        this.isFirstPlayer = isFirstPlayer;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerId id of the player that owns the shelf
     * @param shelf    shelf representation of the player
     */
    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        this.shelfState = shelf;
        this.revalidate();
        this.repaint();
    }

    /* ------------------- GRAPHIC LAYOUT -------------------*/


    /**
     * Override {@link JComponent#paintComponent(Graphics)} to enable resizing of the component
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image foreground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BookshelfForeground.png"))).getImage();
        Image background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BookshelfBackground.png"))).getImage();
        Image firstPlayerOverlay = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/FirstPlayerOverlay.png"))).getImage();
        double ratio = (double)this.getWidth()/foreground.getWidth(null);
        int topPadding = (int)Math.round(79*ratio);
        int leftPadding = (int)Math.round(144*ratio);
        int size = (int) Math.round((145+18)*ratio);
        int vHorizontal = (int) Math.round(34*ratio);
        int vVertical = (int) Math.round(6*ratio);

        int horizontalSkip = size + vHorizontal;
        int verticalSkip = size + vVertical;

        g.drawImage(background,0,0,this.getWidth(),this.getHeight(),null);

        int y = topPadding;
        for(int i=0;i<6; i++){
            int x = leftPadding;
            for(int j=0;j<5;j++){
                if(this.shelfState[i][j]!=Tile.EMPTY) {
                    Image tileImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Tile/" + this.shelfState[i][j].name() + ".png"))).getImage();
                    g.drawImage(tileImage, x, y, size, size, null);
                }
                x+=horizontalSkip;
            }
            y+=verticalSkip;
        }

        g.drawImage(foreground,0,0,this.getWidth(),this.getHeight(),null);

        if(this.isFirstPlayer){
            g.drawImage(firstPlayerOverlay,0,0,this.getWidth(),this.getHeight(),null);
        }
    }
}
