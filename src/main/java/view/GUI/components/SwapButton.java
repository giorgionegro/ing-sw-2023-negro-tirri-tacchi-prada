package view.GUI.components;



import javax.swing.*;
import java.awt.*;

public class SwapButton extends JButton {

    private final Image swapArrowImage = new ImageIcon(getClass().getResource("/SwapTilesIcon.png")).getImage();

    public SwapButton(){
        this.setPreferredSize(new Dimension(0,0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(swapArrowImage, 0, 0, getWidth(), getHeight(), null);
    }
}
