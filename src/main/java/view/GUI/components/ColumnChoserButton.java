package view.GUI.components;

import util.ResourceProvider;

import javax.swing.*;
import java.awt.*;

public class ColumnChoserButton extends JButton {

    private final ImageIcon insertArrow = new ImageIcon(ResourceProvider.getResourcePath("/insert.png"));
    private final Image disabledImage = new ImageIcon(ResourceProvider.getResourcePath("/TileSelectedFilter.png")).getImage();

    public ColumnChoserButton(int column){
        this.setActionCommand(String.valueOf(column));
        this.setPreferredSize(new Dimension(0,0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
        if(!this.isEnabled()){
            g.drawImage(disabledImage,0,0,getWidth(),getHeight(),null);
        }
    }
}
