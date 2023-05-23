package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NetworkChoicePanel extends JPanel implements ActionListener {

    private JButton SocketButton;
    private JButton RMIButton;

    public  NetworkChoicePanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(60, 10, 0, 10);

        JPanel NetworkChoicePanel = new JPanel();
        NetworkChoicePanel.setBackground(new Color(0, 0, 0));
        NetworkChoicePanel.setLayout(new GridBagLayout());

        //Buttons
        Font font = new Font("Century", Font.BOLD, 18);
        RMIButton = new JButton();
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());
        RMIButton.setIcon(button);
        JLabel RMI = new JLabel("RMI");
        RMI.setFont(font);
        RMIButton.add(RMI);


        SocketButton = new JButton();
        SocketButton.setIcon(button);
        JLabel socket = new JLabel("SOCKET");
        socket.setFont(font);
        SocketButton.add(socket);

        NetworkChoicePanel.add(SocketButton, c);
        c.gridx++;
        NetworkChoicePanel.add(RMIButton, c);

        SocketButton.addActionListener(this);
        RMIButton.addActionListener(this);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == SocketButton) {
            //TODO

        } else if (e.getSource() == RMIButton) {
            //TODO

        }

    }
}
