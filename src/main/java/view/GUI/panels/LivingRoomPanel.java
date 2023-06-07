package view.GUI.panels;

import model.Tile;
import model.abstractModel.LivingRoom;
import modelView.LivingRoomInfo;

import view.GUI.AspectRatioLayout;
import view.GUI.components.TileButton;
import view.interfaces.LivingRoomView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class LivingRoomPanel extends JPanel implements LivingRoomView {
    private final Image background = new ImageIcon(getClass().getResource("/LivingRoomBackground.png")).getImage();
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
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        this.removeAll();

        this.add(topSpacer,topSpacerConstraints);
        this.add(bottomSpacer,bottomSpacerConstraints);
        this.add(leftSpacer,leftSpacerConstraints);
        this.add(rightSpacer,rightSpacerConstraints);

        Tile[][] tiles = o.board();

        for(int i=0;i< tiles.length;i++){
            for(int j=0; j<tiles[i].length; j++){
                tileConstraints.gridx = 1+j;
                tileConstraints.gridy = 1+i;

                Container imagetileContainer = new Container();
                imagetileContainer.setLayout(new AspectRatioLayout(1));

                if (tiles[i][j]==Tile.EMPTY) {
                    imagetileContainer.add(new Container(),tileConstraints);
                } else {
                    TileButton tileButton = new TileButton(j,i,tiles[i][j]);
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

