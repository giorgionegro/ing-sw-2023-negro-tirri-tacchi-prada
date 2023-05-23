package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinGamePanel extends JPanel implements ActionListener {
    public JoinGamePanel(){
        ImageIcon CreateGame = new ImageIcon(GUI.class.getResource("/create.jpg").getPath());

        JPanel BackgroundJoinPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(CreateGame.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 10, 0, 10);

        BackgroundJoinPanel.setBounds(0, 0, 962, 545);
        BackgroundJoinPanel.setLayout(new BorderLayout());

        JPanel ButtonsJoinPanel = new JPanel();

        ButtonsJoinPanel.setOpaque(false);
        ButtonsJoinPanel.setBackground(new Color(0, 0, 0));
        ButtonsJoinPanel.setLayout(new GridBagLayout());


        JTextField PlayerId = new JTextField("Insert Player Id");
        PlayerId.setPreferredSize(new Dimension(250, 40));

        JTextField GameId = new JTextField("Insert Game Id");
        GameId.setPreferredSize(new Dimension(250, 40));


        Font font = new Font("Century", Font.BOLD, 18);
        JButton PlayButton = new JButton();
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());
        PlayButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("PLAY");
        PlayButtonLabel.setFont(font);
        PlayButton.add(PlayButtonLabel);

        ButtonsJoinPanel.add(PlayerId, c);
        c.gridy++;
        ButtonsJoinPanel.add(GameId, c);
        c.gridy++;
        ButtonsJoinPanel.add(PlayButton, c);

        BackgroundJoinPanel.add(ButtonsJoinPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}
