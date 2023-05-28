package view.GUI.components;

import util.ResourceProvider;

import javax.swing.*;
import java.awt.*;

public class SwapButton extends JButton {

    private final Image swapArrowImage = new ImageIcon(ResourceProvider.getResourcePath("/SwapTilesIcon.png")).getImage();

    public SwapButton(){
        this.setPreferredSize(new Dimension(0,0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(swapArrowImage, 0, 0, getWidth(), getHeight(), null);
    }
}
