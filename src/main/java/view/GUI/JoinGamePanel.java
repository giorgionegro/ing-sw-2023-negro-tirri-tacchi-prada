package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinGamePanel extends JPanel implements ActionListener {
    Image CreateGame;
    private final ActionListener listener;
    JButton PlayButton;
    JTextField PlayerId;
    JTextField GameId;
    public JoinGamePanel(ActionListener listener){
        this.listener = listener;
        CreateGame = new ImageIcon(GUI.class.getResource("/desktop.png").getPath()).getImage();
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());
        ImageIcon buttonIdG = new ImageIcon(GUI.class.getResource("/GameID.png").getPath());
        ImageIcon buttonIdP = new ImageIcon(GUI.class.getResource("/PlayerID.png").getPath());
        Font font1 = new Font("Century", Font.BOLD, 24);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 10, 0, 10);

        this.setBounds(0, 0, 962, 545);
        this.setLayout(new BorderLayout());

        JPanel ButtonsJoinPanel = new JPanel();

        ButtonsJoinPanel.setOpaque(false);
        ButtonsJoinPanel.setBackground(new Color(0, 0, 0));
        ButtonsJoinPanel.setLayout(new GridBagLayout());


        JTextField PlayerId = new JTextField();
        PlayerId.setPreferredSize(new Dimension(150,40));
        JLabel PlayerIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdP.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        PlayerIdField.setPreferredSize(new Dimension(165,41));

        JTextField GameId = new JTextField();
        GameId.setPreferredSize(new Dimension(150,40));
        JLabel GameIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdG.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        GameIdField.setPreferredSize(new Dimension(165,41));



        PlayButton = new JButton();
        PlayButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("PLAY");
        PlayButtonLabel.setFont(font1);
        PlayButton.add(PlayButtonLabel);

        ButtonsJoinPanel.add(PlayerIdField,c);
        c.gridx++;
        ButtonsJoinPanel.add(PlayerId,c);
        c.gridx--;
        c.gridy++;
        ButtonsJoinPanel.add(GameIdField,c);
        c.gridx++;
        ButtonsJoinPanel.add(GameId,c);
        c.gridy++;
        ButtonsJoinPanel.add(PlayButton,c);

        PlayButton.addActionListener(this);
        this.add(ButtonsJoinPanel);


    }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(CreateGame!=null){
                double ratio = 1.765;
                double windowRatio = (double)getWidth()/getHeight();
                int width;
                int height;
                if(windowRatio>ratio) {
                    width = getWidth();
                    height = (int) (getWidth()/ratio);
                }else{
                    height = getHeight();
                    width = (int) (getHeight()*ratio);
                }
                g.drawImage(CreateGame, 0, 0, width, height, null);

            }
        }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == PlayButton) {
            listener.actionPerformed(new ActionEvent(this,e.getID(),"PLAY"));
            String IdPlayer = PlayerId.getText();
            String IdGame = GameId.getText();

        }
    }
}
