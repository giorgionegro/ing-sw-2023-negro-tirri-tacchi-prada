package view.GUI.buttons;



import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * This class extends JButton and represents a graphical component that shows the swap button for swapping tiles in {@link view.GUI.panels.TilesOrderingPanel}
 */
public class SwapButton extends JButton {

    /**
     * This image contains the swap arrow image
     */
    private final Image swapArrowImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/SwapTilesIcon.png"))).getImage();

    /**
     * Construct an {@link SwapButton} instance
     */
    public SwapButton() {
        super();
        this.setPreferredSize(new Dimension(0, 0));
    }

    /**
     * {@inheritDoc}
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.swapArrowImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
