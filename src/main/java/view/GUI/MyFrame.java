package view.GUI;



import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;
import java.util.Objects;

public class MyFrame extends JFrame {
    public MyFrame() {
        this.setLayout(new BorderLayout());

        Image icon = new ImageIcon (Objects.requireNonNull(getClass().getResource("/AppIcon.png"))).getImage();
        this.setIconImage(icon);

        this.setTitle("My Shelfie");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(1100, 700));
        this.setMinimumSize(new Dimension(825,525));
        this.setResizable(true);
    }
}
