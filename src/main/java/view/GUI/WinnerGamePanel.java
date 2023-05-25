package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinnerGamePanel extends JPanel implements ActionListener {
    Image CreateGame;

    public WinnerGamePanel() {
        CreateGame = new ImageIcon(GUI.class.getResource("/winner.jpg").getPath()).getImage();
        ImageIcon button = new ImageIcon(GUI.class.getResource("/img.png").getPath());


        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 0, 0, 0);

        this.setBounds(0, 0, 962, 545);
        this.setLayout(new BorderLayout());

        JPanel ClassificaWinnerPanel = new JPanel();

        ClassificaWinnerPanel.setOpaque(false);
        ClassificaWinnerPanel.setBackground(new Color(0, 0, 0));
        ClassificaWinnerPanel.setLayout(new GridBagLayout());

        this.add(ClassificaWinnerPanel);

        JTextField FirstPlayer = new JTextField("FirstPlayer:");
        FirstPlayer.setEditable(false);
        FirstPlayer.setPreferredSize(new Dimension(250, 50));

        JTextField FirstPlayerPoints = new JTextField("");
        FirstPlayerPoints.setEditable(false);
        FirstPlayerPoints.setPreferredSize(new Dimension(60, 50));

        JTextField SecondPlayer = new JTextField("SecondPlayer:");
        SecondPlayer.setEditable(false);
        SecondPlayer.setPreferredSize(new Dimension(250, 50));

        JTextField SecondPlayerPoints = new JTextField("");
        SecondPlayerPoints.setEditable(false);
        SecondPlayerPoints.setPreferredSize(new Dimension(60, 50));

        JTextField ThirdPlayer = new JTextField("ThirdPlayer:");
        ThirdPlayer.setEditable(false);
        ThirdPlayer.setPreferredSize(new Dimension(250, 50));

        JTextField ThirdPlayerPoints = new JTextField("");
        ThirdPlayerPoints.setEditable(false);
        ThirdPlayerPoints.setPreferredSize(new Dimension(60, 50));

        JTextField FourthPlayer = new JTextField("FourthPlayer:");
        FourthPlayer.setEditable(false);
        FourthPlayer.setPreferredSize(new Dimension(250, 50));

        JTextField FourthPlayerPoints = new JTextField("");
        FourthPlayerPoints.setEditable(false);
        FourthPlayerPoints.setPreferredSize(new Dimension(60, 50));


        c.gridx--;
        ClassificaWinnerPanel.add(FirstPlayer, c);
        ClassificaWinnerPanel.add(FirstPlayerPoints, c);

        c.gridy++;
        ClassificaWinnerPanel.add(SecondPlayer, c);
        ClassificaWinnerPanel.add(SecondPlayerPoints, c);

        c.gridy++;
        ClassificaWinnerPanel.add(ThirdPlayer, c);
        ClassificaWinnerPanel.add(ThirdPlayerPoints, c);
        c.gridy++;
        ClassificaWinnerPanel.add(FourthPlayer, c);
        ClassificaWinnerPanel.add(FourthPlayerPoints, c);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(CreateGame, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}


