package view.GUI;

import view.ResourceProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Table extends JPanel implements ActionListener {
    ActionListener listener;
    ImageIcon table;

    JButton swap1, swap2;

    JPanel tiles;
    public Table(ActionListener listener){

        this.setSize(new Dimension(77, 300));

        this.listener = listener;

        table = new ImageIcon(ResourceProvider.getResourcePath()+"/table.png");


        this.setLayout(null);

        tiles = new JPanel();
        tiles.setLayout(null);
        tiles.setOpaque(false);
        tiles.setBounds(0,0,77,300);
        this.add(tiles);


        ImageIcon swapArrowImage = new ImageIcon(ResourceProvider.getResourcePath()+"/swapButton.png");
        swap1 = new JButton() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(swapArrowImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        swap1.setBounds(28 , 85, 20, 30);
        swap1.addActionListener(this);
        this.add(swap1);

        swap2 = new JButton() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(swapArrowImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        swap2.setBounds(28 , 85+99, 20, 30);
        swap2.addActionListener(this);
        this.add(swap2);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(table.getImage(), 0, 0, getWidth(), getHeight(), null);
    }

    List<TileButton> selectedTiles = new ArrayList<>();
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==swap1){
            if(selectedTiles.size()==3){
                selectedTiles.add(selectedTiles.remove(1));
            }
        } else if (e.getSource()==swap2) {
            if(selectedTiles.size()>1){
                selectedTiles.add(0,selectedTiles.remove(1));
            }
        }else {
            TileButton button = ((TileButton) e.getSource());
            boolean selected = button.isSelected();
            if (selected) {
                button.setSelected(false);
                selectedTiles.remove(button);
            } else {
                if (selectedTiles.size() < 3 && isPickable(button)) {
                    button.setSelected(true);
                    selectedTiles.add(button);
                }
            }
        }
        printPicked();

        StringBuilder command = new StringBuilder();
        for(TileButton t : selectedTiles)
            command.append(t.getTileX()).append("\n").append(t.getTileY()).append("\n");

        command.deleteCharAt(command.length()-1);

        listener.actionPerformed(new ActionEvent(this, 0, command.toString()));
    }

    private boolean isPickable(TileButton newSelected){
        //TODO implementare algoritmo
        return true;
    }

    private void printPicked(){
        tiles.removeAll();
        int i = 0;
        for(TileButton t : selectedTiles){
            ImageIcon tileImage = new ImageIcon(GamePanel.ReturnImageTile(t.getTile()));
            JLabel pickedTile = new JLabel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(tileImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };

            pickedTile.setBounds(6,300-66-(18+(65+34)*i),65,65);
            tiles.add(pickedTile);
            i++;
        }
        tiles.revalidate();
        tiles.repaint();
    }
}
