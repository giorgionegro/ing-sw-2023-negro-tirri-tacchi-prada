package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateGamePanel extends JPanel implements ActionListener {
    public CreateGamePanel(){
        ImageIcon CreateGame = new ImageIcon (this.getClass().getResource("/create.jpg").getPath());
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());
        JPanel BackgroundCreatePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(CreateGame.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.insets =  new Insets(20,0,0,0);

        BackgroundCreatePanel.setBounds(0,0,962,545);
        BackgroundCreatePanel.setLayout(new BorderLayout());

        JPanel ButtonsCreatePanel = new JPanel ();

        ButtonsCreatePanel.setOpaque(false);
        ButtonsCreatePanel.setBackground(new Color(0,0,0));
        ButtonsCreatePanel.setLayout(new GridBagLayout());

        JTextField PlayerId = new JTextField("Insert Player Id");
        PlayerId.setPreferredSize(new Dimension(200,40));

        JTextField GameId = new JTextField("Insert Game Id");

        GameId.setPreferredSize(new Dimension(200,40));
        String[] nPlayers = {"2", "3", "4"};
        JComboBox<String> comboBox = new JComboBox<>(nPlayers);
        comboBox.setPreferredSize(new Dimension(50,40));


        JTextField nPlayersText = new JTextField("Choose number of players");
        nPlayersText.setEditable(false);
        nPlayersText.setPreferredSize(new Dimension(200,40));


        Font font1 = new Font("Century", Font.BOLD, 18);
        JButton PlayButton= new JButton();
        PlayButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("CREATE");
        PlayButtonLabel.setFont(font1);
        PlayButton.add(PlayButtonLabel);

        ButtonsCreatePanel.add(PlayerId,c);
        c.gridy++;
        ButtonsCreatePanel.add(GameId,c);
        c.gridy++;
        c.gridx--;
        ButtonsCreatePanel.add(nPlayersText,c);
        ButtonsCreatePanel.add(comboBox, c);
        c.gridy++;
        ButtonsCreatePanel.add(PlayButton,c);

        BackgroundCreatePanel.add(ButtonsCreatePanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}
