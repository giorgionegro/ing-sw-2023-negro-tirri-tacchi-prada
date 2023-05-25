package view.GUI;

import javax.swing.*;
import java.awt.*;

public class NamePlayersPanel extends JPanel {
    public NamePlayersPanel() {
        Font font = new Font("Century", Font.BOLD, 12);
        this.setLayout(null);
        this.setOpaque(false);

        JLabel player2 = new JLabel("Player2");
        player2.setFont(font);
        player2.setBounds(0, 0, 50, 50);
        this.add(player2);

        JLabel player3 = new JLabel("Player3");
        player3.setFont(font);
        player3.setBounds(200, 0, 50, 50);
        this.add(player3);

        JLabel player4 = new JLabel("Player4");
        player4.setFont(font);
        player4.setBounds(400, 0, 50, 50);
        this.add(player4);
    }
}
