package view.GUI;

import model.Tile;
import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;
import view.ResourceProvider;
import view.interfaces.LivingRoomView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class LivingRoomPanel extends JPanel implements LivingRoomView, ActionListener {
    ImageIcon living;
    ActionListener listener;
    public LivingRoomPanel(ActionListener listener) {
        this.listener = listener;
        living = new ImageIcon(ResourceProvider.getResourcePath()+"/livingroom.png");

        this.setMinimumSize(new Dimension(600, 600));
        this.setLayout(null);

    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(living.getImage(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        //this.currentLivingRoom = o;
        this.removeAll();
        Tile[][] board = o.board();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]==Tile.EMPTY) {
                    JLabel EmptyTile = new JLabel();
                    EmptyTile.setOpaque(false);
                    EmptyTile.setBounds(27+60*i,30+60*j,60,60);
                    this.add(EmptyTile);
                } else {
                    TileButton tileButton = new TileButton(j,i,board[i][j]);
                    tileButton.setBounds(27+60*j,30+60*i,60,60);
                    tileButton.addActionListener(this);
                    this.add(tileButton);
                }
            }
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed(e);
    }

}

