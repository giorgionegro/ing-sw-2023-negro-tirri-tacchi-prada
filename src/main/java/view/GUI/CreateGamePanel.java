package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateGamePanel extends JPanel implements ActionListener {
    Image CreateGame;
    JButton PlayButton;
    private final ActionListener listener;
    public CreateGamePanel(ActionListener listener){
        this.listener = listener;
        CreateGame = new ImageIcon (this.getClass().getResource("/desktop.png").getPath()).getImage();
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());
        ImageIcon buttonIdG = new ImageIcon(GUI.class.getResource("/GameID.png").getPath());
        ImageIcon buttonIdP = new ImageIcon(GUI.class.getResource("/PlayerID.png").getPath());
        ImageIcon buttonIdN = new ImageIcon(GUI.class.getResource("/nplayers.png").getPath());
        Font font1 = new Font("Century", Font.BOLD, 24);


        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.insets = new Insets(20, 0, 0, 0);
        this.setBounds(0,0,962,545);
        this.setLayout(new BorderLayout());

        JPanel ButtonsCreatePanel = new JPanel ();

        ButtonsCreatePanel.setOpaque(false);
        ButtonsCreatePanel.setBackground(new Color(0,0,0));
        ButtonsCreatePanel.setLayout(new GridBagLayout());



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


        String[] nPlayers = {"2", "3", "4"};
        JComboBox<String> comboBox = new JComboBox<>(nPlayers);
        comboBox.setPreferredSize(new Dimension(150,40));


        JLabel NumberIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdN.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        NumberIdField.setPreferredSize(new Dimension(190,41));


        PlayButton= new JButton();
        PlayButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("CREATE");
        PlayButtonLabel.setFont(font1);
        PlayButton.add(PlayButtonLabel);

        ButtonsCreatePanel.add(PlayerIdField,c);
        c.gridx++;
        ButtonsCreatePanel.add(PlayerId,c);
        c.gridx--;
        c.gridy++;
        ButtonsCreatePanel.add(GameIdField,c);
        c.gridx++;
        ButtonsCreatePanel.add(GameId,c);
        c.gridy++;
        c.gridx--;
        ButtonsCreatePanel.add(NumberIdField,c);
        c.gridx++;
        ButtonsCreatePanel.add(comboBox, c);
        c.gridy++;
        ButtonsCreatePanel.add(PlayButton,c);

        PlayButton.addActionListener(this);

        this.add(ButtonsCreatePanel);

    }

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
                listener.actionPerformed(new ActionEvent(this, e.getID(), "PLAY"));
        }
    }
}
