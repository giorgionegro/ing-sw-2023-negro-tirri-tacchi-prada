package view.GUI.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NetworkChoicePanel extends JPanel implements ActionListener {

    private final JButton ExitButton;
    private final JButton SocketButton;
    private final JButton RMIButton;
    private final ActionListener listener;

    public  NetworkChoicePanel(ActionListener listener) {
        this.listener = listener;
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 0, 0, 0);


        this.setBackground(new Color(0, 0, 0));
        this.setLayout(new GridBagLayout());

        ImageIcon titleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/connect.png")));
        JLabel title = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(titleImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        title.setOpaque(false);
        title.setPreferredSize(new Dimension(558,80));

        //Buttons
        Font font = new Font("Century", Font.BOLD, 18);
        RMIButton = new JButton();
        ImageIcon button = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png")));
        RMIButton.setIcon(button);
        JLabel RMI = new JLabel("     RMI    ");
        RMI.setFont(font);
        RMIButton.add(RMI);


        SocketButton = new JButton();
        SocketButton.setIcon(button);
        JLabel socket = new JLabel("SOCKET");
        socket.setFont(font);
        SocketButton.add(socket);

        ExitButton = new JButton();
        ExitButton.setIcon(button);
        JLabel exit = new JLabel("    Exit     ");
        exit.setFont(font);
        ExitButton.add(exit);

        this.add(title);
        c.gridy++;
        this.add(RMIButton, c);
        c.gridy++;
        this.add(SocketButton, c);
        c.gridy++;
        this.add(ExitButton,c);

        SocketButton.addActionListener(this);
        RMIButton.addActionListener(this);
        ExitButton.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == SocketButton) {
            listener.actionPerformed(new ActionEvent(this,e.getID(),"SOCKET"));
        } else if (e.getSource() == RMIButton) {
            listener.actionPerformed(new ActionEvent(this,e.getID(),"RMI"));
        } else if (e.getSource() == ExitButton) {
            listener.actionPerformed(new ActionEvent(this,e.getID(),"EXIT"));
        }

    }
}
