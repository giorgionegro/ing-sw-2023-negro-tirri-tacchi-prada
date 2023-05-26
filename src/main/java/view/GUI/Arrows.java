package view.GUI;

import view.ResourceProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Arrows extends JPanel implements ActionListener{
    ActionListener listener;
    public Arrows(ActionListener listener){
        this.listener = listener;
        this.setLayout(null);
        this.setOpaque(false);
        int k = 0;
        for (int i = 0; i < 5; i++) {
            ImageIcon insertArrow = new ImageIcon(ResourceProvider.getResourcePath()+"/insert2.png");
            JButton insert = new JButton() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            insert.setBounds(k, 0, 30, 40);
            insert.addActionListener(this);
            insert.setActionCommand(String.valueOf(i));
            this.add(insert);
            k = k + 50;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed(new ActionEvent(this, e.getID(), e.getActionCommand()));
    }
}
