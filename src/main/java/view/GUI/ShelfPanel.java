package view.GUI;

import javax.swing.*;
import java.awt.*;

import model.Tile;
import view.ResourceProvider;

public class ShelfPanel extends JPanel {
    ImageIcon shelf;

    JPanel foreground;
    public ShelfPanel(int x, int y, int w, int h) {

        shelf = new ImageIcon(ResourceProvider.getResourcePath()+"/bookshelf.png");
        foreground = new JPanel(){
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
        }};

        foreground.setBounds(0,0,w,h);
        foreground.setOpaque(false);
        this.add(foreground);
        this.setLayout(null);
        this.setOpaque(false);
        this.setBounds(x, y, w, h);
    }

    public void updateOpponent(Tile[][] tiles){
        updateShelf(tiles,23,15,25, 7,2);
    }




    private void updateShelf(Tile[][] tiles, int x, int y, int size, double h, double k){
        this.removeAll();
        this.add(foreground);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                double v = x + size * j + j * h;
                double v1 = y + size * i + i * k;
                if (tiles[i][j]==Tile.EMPTY) {
                    JLabel EmptyTile = new JLabel();
                    EmptyTile.setOpaque(false);
                    EmptyTile.setBounds((int) v, (int) v1, size, size);
                    this.add(EmptyTile);
                } else {
                    ImageIcon tile = new ImageIcon(GamePanel.ReturnImageTile(tiles[i][j]));
                    JPanel imagetile = new JPanel() {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.drawImage(tile.getImage(), 0, 0, getWidth(), getHeight(), null);
                        }
                    };
                    imagetile.setBounds((int) v, (int) v1, size, size);
                    this.add(imagetile);
                }
            }
        }
        refresh();
    }
    public void refresh(){
        this.revalidate();
        this.repaint();
    }

    public void updatePlayer(Tile[][] tiles){
        updateShelf(tiles,36,22,37,11,4);
    }


}
