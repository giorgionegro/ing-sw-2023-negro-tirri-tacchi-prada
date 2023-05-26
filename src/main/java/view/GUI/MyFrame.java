package view.GUI;

import view.ResourceProvider;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        this.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon (ResourceProvider.getResourcePath()+"/Icon.png");
        this.setIconImage(icon.getImage());

        this.setTitle("My Shelfie Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        this.setSize(new Dimension(1300, 900));
        this.setResizable(true);

    }
}
