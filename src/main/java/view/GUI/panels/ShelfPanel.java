package view.GUI.panels;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

import model.Tile;

import view.graphicInterfaces.PlayerShelfGraphics;

public class ShelfPanel extends JPanel implements PlayerShelfGraphics {
    private Tile[][] shelfState = new Tile[6][5];

    public ShelfPanel() {
        this.setOpaque(false);
        for(Tile[] r : shelfState)
            Arrays.fill(r, Tile.EMPTY);
    }

    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        shelfState = shelf;
        this.revalidate();
        this.repaint();
    }

    /* ------------------- GRAPHIC LAYOUT -------------------*/

    private final Image foreground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/BookshelfForeground.png"))).getImage();
    private final Image background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/BookshelfBackground.png"))).getImage();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double ratio = (double)getWidth()/foreground.getWidth(null);
        int topPadding = (int)Math.round(79*ratio);
        int leftPadding = (int)Math.round(144*ratio);
        int size = (int) Math.round((145+18)*ratio);
        int vHorizontal = (int) Math.round(34*ratio);
        int vVertical = (int) Math.round(6*ratio);

        int horizontalSkip = size + vHorizontal;
        int verticalSkip = size + vVertical;

        g.drawImage(background,0,0,getWidth(),getHeight(),null);

        int y = topPadding;
        for(int i=0;i<6; i++){
            int x = leftPadding;
            for(int j=0;j<5;j++){
                if(shelfState[i][j]!=Tile.EMPTY) {
                    Image tileImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Tile/" + shelfState[i][j].name() + ".png"))).getImage();
                    g.drawImage(tileImage, x, y, size, size, null);
                }
                x+=horizontalSkip;
            }
            y+=verticalSkip;
        }

        g.drawImage(foreground,0,0,getWidth(),getHeight(),null);
    }
}
