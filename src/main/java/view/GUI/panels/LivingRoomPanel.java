package view.GUI.panels;

import model.Tile;
import model.Token;
import view.GUI.layouts.AspectRatioLayout;
import view.GUI.buttons.TileButton;
import view.graphicInterfaces.LivingRoomGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class LivingRoomPanel extends JPanel implements LivingRoomGraphics {
    private final ActionListener orderingTable;
    private final Image background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/LivingRoomBackground.png"))).getImage();
    private final Image endGameToken = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Token/" + Token.TOKEN_GAME_END.name() + ".png"))).getImage();
    private final GridBagConstraints topSpacerConstraints = new GridBagConstraints(
            0, 0,
            11, 1,
            1, 134,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );
    private final GridBagConstraints leftSpacerConstraints = new GridBagConstraints(
            0, 1,
            1, 9,
            121, 300,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );
    private final GridBagConstraints rightSpacerConstraints = new GridBagConstraints(
            10, 1,
            1, 9,
            148, 300,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );

    /*------------------------ GRAPHIC LAYOUT -------------------*/
    private final GridBagConstraints bottomSpacerConstraints = new GridBagConstraints(
            0, 10,
            11, 1,
            1, 135,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );
    private final GridBagConstraints tileConstraints = new GridBagConstraints(
            0, 0,
            1, 1,
            298, 298,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );
    private boolean isLastTurn;

    public LivingRoomPanel(ActionListener orderingTable) {
        super();
        this.orderingTable = orderingTable;
        this.isLastTurn = false;
        this.setLayout(new GridBagLayout());
    }

    public void setIsLastTurn(boolean isLastTurn) {
        this.isLastTurn = isLastTurn;
    }

    @Override
    public void updateBoardGraphics(Tile[][] board) {
        this.removeAll();

        this.add(new Container(), this.topSpacerConstraints);
        this.add(new Container(), this.bottomSpacerConstraints);
        this.add(new Container(), this.leftSpacerConstraints);
        this.add(new Container(), this.rightSpacerConstraints);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.tileConstraints.gridx = 1 + j;
                this.tileConstraints.gridy = 1 + i;

                Container imagetileContainer = new Container();
                imagetileContainer.setLayout(new AspectRatioLayout(1));

                if (board[i][j] == Tile.EMPTY) {
                    imagetileContainer.add(new Container(), this.tileConstraints);
                } else {
                    TileButton tileButton = new TileButton(j, i, board[i][j]);
                    if (this.isTilePickable(i, j, board))
                        tileButton.addActionListener(this.orderingTable);
                    else {
                        tileButton.setEnabled(false);
                    }
                    imagetileContainer.add(tileButton);
                }

                this.add(imagetileContainer, this.tileConstraints);
            }
        }
        this.revalidate();
        this.repaint();
    }

    /**
     * @param row    row of the tile
     * @param column column of the tile
     * @param board  board to check
     * @return true if tile is pickable, false otherwise
     */
    private boolean isTilePickable(int row, int column, Tile[][] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column] == Tile.EMPTY || board[row][column] == null)
            return false;

        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        return board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.background, 0, 0, this.getWidth(), this.getHeight(), null);
        if (!this.isLastTurn)
            g.drawImage(this.endGameToken, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}

