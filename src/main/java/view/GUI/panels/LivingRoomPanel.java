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
    private final Image background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LivingRoomBackground.png"))).getImage();
    private final Image endGameToken = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Token/" + Token.TOKEN_GAME_END.name() + ".png"))).getImage();

    private final Container topSpacer = new Container();
    private final Container leftSpacer = new Container();
    private final Container rightSpacer = new Container();
    private final Container bottomSpacer = new Container();
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

    private final ActionListener orderingTable;
    public LivingRoomPanel(ActionListener orderingTable) {
        this.orderingTable = orderingTable;

        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(endGameToken,0,0,getWidth(),getHeight(),null);
    }
    @Override
    public void updateBoardGraphics(Tile[][] board) {
        this.removeAll();

        this.add(topSpacer,topSpacerConstraints);
        this.add(bottomSpacer,bottomSpacerConstraints);
        this.add(leftSpacer,leftSpacerConstraints);
        this.add(rightSpacer,rightSpacerConstraints);

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
}

