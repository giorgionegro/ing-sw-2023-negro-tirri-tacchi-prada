package view.GUI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        this.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon (GUI.class.getResource("/Icon.png").getPath());
        this.setIconImage(icon.getImage());

        this.setTitle("My Shelfie Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        this.setResizable(false);

    }
}
