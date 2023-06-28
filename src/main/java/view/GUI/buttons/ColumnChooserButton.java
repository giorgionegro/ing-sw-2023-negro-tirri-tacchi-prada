package view.GUI.buttons;



import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * This class extends JButton and represents a graphical component that allows the user to choose a column
 */
public class ColumnChooserButton extends JButton {

    /**
     * Image of the arrow for the button
     */
    private final ImageIcon insertArrow = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/insert.png")));
    /**
     * Image of filter for the button
     */
    private final Image disabledImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ForegroundFilter.png"))).getImage();

    /**
     * @param column the column to be associated with this button
     */
    public ColumnChooserButton(int column) {
        super();
        this.setActionCommand(String.valueOf(column));
        this.setPreferredSize(new Dimension(0, 0));
    }

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image based on the button state
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.insertArrow.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        if(!this.isEnabled()){
            g.drawImage(this.disabledImage,0,0, this.getWidth(), this.getHeight(),null);
        }
    }
}
