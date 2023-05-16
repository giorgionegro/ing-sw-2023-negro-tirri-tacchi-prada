package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel implements ActionListener {
    JButton CreateButton;
    JButton JoinButton;
    public HomePanel(){//JFrame = a GUI window to add components to
        ImageIcon desktop = new ImageIcon (this.getClass().getResource("/desktop01.jpg").getPath());
        JPanel BackgroundHomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(desktop.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.insets =  new Insets(60,10,0,10);

        JPanel ButtonsHomePanel = new JPanel ();

        BackgroundHomePanel.setBounds(0,0,1000,700);
        BackgroundHomePanel.setLayout(new BorderLayout());


        ButtonsHomePanel.setOpaque(false);
        ButtonsHomePanel.setBackground(new Color(0,0,0));
        //ButtonsHomePanel.setBounds(0,0,1000,700);
        ButtonsHomePanel.setLayout(new GridBagLayout());
        BackgroundHomePanel.add(ButtonsHomePanel);

        //Buttons
        Font font = new Font("Century", Font.BOLD, 18);
        JoinButton = new JButton();
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());
        JoinButton.setIcon(button);
        JLabel join = new JLabel("Join Game");
        join.setFont(font);
        JoinButton.add(join);


        CreateButton= new JButton();
        CreateButton.setIcon(button);
        JLabel create = new JLabel("Create Game");
        create.setFont(font);
        CreateButton.add(create);

        ButtonsHomePanel.add(JoinButton, c);
        c.gridx++;
        ButtonsHomePanel.add(CreateButton, c);

        JoinButton.addActionListener(this);
        CreateButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == CreateButton) {
           // TODO createGame();
        } else if (e.getSource() == JoinButton) {
           // TODO joinGame();
        }

    }
}
