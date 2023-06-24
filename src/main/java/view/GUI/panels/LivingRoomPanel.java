package view.GUI.panels;

import model.Tile;
import model.Token;

import view.GUI.AspectRatioLayout;
import view.GUI.components.TileButton;
import view.graphicInterfaces.LivingRoomGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LivingRoomPanel extends JPanel implements LivingRoomGraphics {
    private final ActionListener orderingTable;

    private boolean isLastTurn;

    public LivingRoomPanel(ActionListener orderingTable) {
        this.orderingTable = orderingTable;
        this.isLastTurn = false;
        this.setLayout(new GridBagLayout());
    }

    public void setIsLastTurn(boolean isLastTurn){
        this.isLastTurn = isLastTurn;
    }

    @Override
    public void updateBoardGraphics(Tile[][] board) {
        this.removeAll();

        this.add(new Container(),topSpacerConstraints);
        this.add(new Container(),bottomSpacerConstraints);
        this.add(new Container(),leftSpacerConstraints);
        this.add(new Container(),rightSpacerConstraints);

        for(int i=0;i< board.length;i++){
            for(int j=0; j<board[i].length; j++){
                tileConstraints.gridx = 1+j;
                tileConstraints.gridy = 1+i;

                Container imagetileContainer = new Container();
                imagetileContainer.setLayout(new AspectRatioLayout(1));

                if (board[i][j]==Tile.EMPTY) {
                    imagetileContainer.add(new Container(),tileConstraints);
                } else {
                    TileButton tileButton = new TileButton(j,i,board[i][j]);
                    tileButton.addActionListener(orderingTable);
                    imagetileContainer.add(tileButton);
                }

                this.add(imagetileContainer,tileConstraints);
            }
        }
        this.revalidate();
        this.repaint();
    }

    /*------------------------ GRAPHIC LAYOUT -------------------*/

    private final Image background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LivingRoomBackground.png"))).getImage();
    private final Image endGameToken = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Token/" + Token.TOKEN_GAME_END.name() + ".png"))).getImage();
    private final GridBagConstraints topSpacerConstraints = new GridBagConstraints(
            0,0,
            11,1,
            1,134,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints leftSpacerConstraints = new GridBagConstraints(
            0,1,
            1,9,
            121,300,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints rightSpacerConstraints = new GridBagConstraints(
            10,1,
            1,9,
            148,300,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints bottomSpacerConstraints = new GridBagConstraints(
            0,10,
            11,1,
            1,135,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints tileConstraints = new GridBagConstraints(
            0,0,
            1,1,
            298,298,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        if(!isLastTurn)
            g.drawImage(endGameToken,0,0,getWidth(),getHeight(),null);
    }
}

