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
//TODO
public class TilesOrderingPanel extends JPanel implements ActionListener{
    private final List<TileButton> pickedTiles = new ArrayList<>();
    private final ActionListener moveSender;
    private ActionListener columnChooser;

    public TilesOrderingPanel(ActionListener moveSender){
        this.moveSender = moveSender;

        initializeLayout();

        swapButton1.addActionListener(this);
        swapButton2.addActionListener(this);
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
        } else if (e.getSource()== columnChooser) {

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

        columnChooser.actionPerformed(new ActionEvent(this,0,String.valueOf(pickedTiles.size())));
    }

    public void setColumnChooser(ActionListener columnChooser){
        this.columnChooser = columnChooser;
    }

    private boolean isPickable(TileButton button){
        //TODO implementa algoritmo
        return true;
    }

    /*------------------ GRAPHIC LAYOUT ---------------------*/
    private final Image background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/TilesOrderingTableBackground.png"))).getImage();
    private final SwapButton swapButton1 = new SwapButton();
    private final SwapButton swapButton2 = new SwapButton();

    private final GridBagConstraints constraints = new GridBagConstraints(
            0,0,
            1,1,
            1,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final Dimension zeroDimension = new Dimension(0,0);

    private void initializeLayout(){
        this.setLayout(new GridBagLayout());

        /* up space */
        constraints.gridwidth=3;
        constraints.weighty=30;
        this.add(new Container(), constraints);

        /* tile space */
        constraints.gridy++;
        constraints.weighty=146;
        this.add(new Container(), constraints);

        /* swap button 1 */
        constraints.gridy++;
        constraints.gridwidth=1;
        constraints.weighty = 70;
        this.add(new Container(),constraints);

        constraints.gridx++;
        swapButton1.setPreferredSize(zeroDimension);
        this.add(swapButton1,constraints);

        constraints.gridx++;
        this.add(new Container(), constraints);

        /* tile space */
        constraints.gridx=0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        constraints.weighty=146;
        this.add(new Container(),constraints);

        /* swap button 2 */
        constraints.gridy++;
        constraints.gridwidth=1;
        constraints.weighty = 70;
        this.add(new Container(),constraints);

        constraints.gridx++;
        swapButton2.setPreferredSize(zeroDimension);
        this.add(swapButton2,constraints);

        constraints.gridx++;
        this.add(new Container(), constraints);

        /* tile space */
        constraints.gridx=0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        constraints.weighty=146;
        this.add(new Container(),constraints);

        /* bottom space */
        constraints.gridy++;
        constraints.weighty=30;
        this.add(new Container(), constraints);
    }


    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
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
}
