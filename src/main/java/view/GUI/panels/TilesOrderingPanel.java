package view.GUI.panels;

import view.GUI.components.SwapButton;
import view.GUI.components.TileButton;
import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TilesOrderingPanel extends JPanel implements ActionListener{
    private final Image background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/TilesOrderingTableBackground.png"))).getImage();
    private final GridBagConstraints tileSpacer = new GridBagConstraints(
            1,1,
            1,1,
            30,170,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final GridBagConstraints horizontalSpacerContraints = new GridBagConstraints(
            0,0,
            1,7,
            80,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final GridBagConstraints verticalSpacerContraints = new GridBagConstraints(
            1,0,
            1,1,
            30,30,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final GridBagConstraints swapConstraints = new GridBagConstraints(
            1,2,
            1,1,
            30,60,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final List<TileButton> pickedTiles = new ArrayList<>();

    SwapButton swapButton1,swapButton2;

    private final ActionListener moveSender;
    private ActionListener columnChoser;

    public TilesOrderingPanel(ActionListener moveSender){
        this.moveSender = moveSender;

        swapButton1 = new SwapButton();
        swapButton1.addActionListener(this);
        swapButton2 = new SwapButton();
        swapButton2.addActionListener(this);

        this.setLayout(new GridBagLayout());
        this.add(new Container(), verticalSpacerContraints);
        this.add(new Container(), tileSpacer);
        this.add(swapButton1, swapConstraints);
        tileSpacer.gridy+=2;
        this.add(new Container(), tileSpacer);
        swapConstraints.gridy+=2;
        this.add(swapButton2,swapConstraints);
        tileSpacer.gridy+=2;
        this.add(new Container(), tileSpacer);
        verticalSpacerContraints.gridy+=6;
        this.add(new Container(),verticalSpacerContraints);
        this.add(new Container(),horizontalSpacerContraints);
        horizontalSpacerContraints.gridx+=2;
        this.add(new Container(),horizontalSpacerContraints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double ratio = (double)getWidth()/background.getWidth(null);
        int topPadding = (int)Math.round(30*ratio);
        int leftPadding = (int)Math.round(30*ratio);
        int size = (int) Math.round(145*ratio);
        int vVertical = (int) Math.round(70*ratio);

        int verticalSkip = size + vVertical;

        g.drawImage(background,0,0,getWidth(),getHeight(),null);

        int y = topPadding + 2*verticalSkip;
        for(TileButton t : pickedTiles){
            Image tileImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Tile/" + t.getTile().name() + ".png"))).getImage();
            g.drawImage(tileImage, leftPadding, y, size, size, null);
            y-=verticalSkip;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==moveSender){
            //Resets the selection if there is an update of livingRoom
            for(TileButton t : pickedTiles) {
                t.setSelected(false);
            }
            pickedTiles.clear();
        }else if(e.getSource()==swapButton1){
            if(pickedTiles.size()==3){
                pickedTiles.add(pickedTiles.remove(1));
            }
        } else if (e.getSource()==swapButton2) {
            if(pickedTiles.size()>1){
                pickedTiles.add(0,pickedTiles.remove(1));
            }
        } else if (e.getSource()==columnChoser) {

            if(pickedTiles.size()>0){
                String columnInShelf = e.getActionCommand();
                StringBuilder picked = new StringBuilder();
                for(TileButton t : pickedTiles) {
                    picked.append(t.getTileY()).append(",").append(t.getTileX()).append(" ");
                    t.setSelected(false);
                }

                pickedTiles.clear();

                String move = picked+"\n"+columnInShelf;
                moveSender.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MOVE, move));
            }

        } else if(e.getSource() instanceof TileButton button){
            boolean selected = button.isSelected();
            if (selected) {
                button.setSelected(false);
                pickedTiles.remove(button);
            } else {
                if (pickedTiles.size() < 3 && isPickable(button)) {
                    button.setSelected(true);
                    pickedTiles.add(button);
                }
            }
        }

        this.revalidate();
        this.repaint();

        columnChoser.actionPerformed(new ActionEvent(this,0,String.valueOf(pickedTiles.size())));
    }

    public void setColumnChoser(ActionListener columnChoser){
        this.columnChoser = columnChoser;
    }

    private boolean isPickable(TileButton button){
        //TODO implementa algoritmo
        return true;
    }
}
