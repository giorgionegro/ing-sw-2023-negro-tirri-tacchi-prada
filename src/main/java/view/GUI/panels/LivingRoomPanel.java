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

/**
 * This class extends JPanel and represents a graphical component that shows the living room board
 */
@SuppressWarnings("DuplicatedCode")
public class LivingRoomPanel extends JPanel implements LivingRoomGraphics {
    /**
     * Action listener for the buttons of the living room
     */
    private final ActionListener orderingTable;
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/LivingRoomBackground.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image endGameToken = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Token/" + Token.TOKEN_GAME_END.name() + ".png"))).getImage();

    /**
     * Boolean that represents when it's the last turn
     */
    private boolean isLastTurn;

    /** Construct an {@link CreateGamePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param orderingTable listener the ActionListener to be notified when a button is pressed
     */
    public LivingRoomPanel(ActionListener orderingTable) {
        super();
        this.orderingTable = orderingTable;
        this.isLastTurn = false;
        this.setLayout(new GridBagLayout());
    }

    /** This method sets whether it is the last turn.
     * @param isLastTurn boolean that represents when it's the last turn
     */
    public void setIsLastTurn(boolean isLastTurn) {
        this.isLastTurn = isLastTurn;
    }

    /**
     * {@inheritDoc}
     * @param board board of the living room
     */
    @Override
    public void updateBoardGraphics(Tile[][] board) {
        this.removeAll();
        GridBagConstraints tileConstraints = new GridBagConstraints(
                0, 0,
                1, 1,
                298, 298,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        //topSpacerConstraints
        this.add(new Container(), new GridBagConstraints(
                0, 0,
                11, 1,
                1, 134,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        ));
        //bottomSpacerConstraints
        this.add(new Container(), new GridBagConstraints(
                0, 10,
                11, 1,
                1, 135,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        ));
        //leftSpacerConstraints
        this.add(new Container(), new GridBagConstraints(
                0, 1,
                1, 9,
                121, 300,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        ));
        //rightSpacerConstraints
        this.add(new Container(), new GridBagConstraints(
                10, 1,
                1, 9,
                148, 300,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        ));

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                tileConstraints.gridx = 1 + j;
                tileConstraints.gridy = 1 + i;

                Container imagetileContainer = new Container();
                imagetileContainer.setLayout(new AspectRatioLayout(1));

                if (board[i][j] == Tile.EMPTY) {
                    imagetileContainer.add(new Container(), tileConstraints);
                } else {
                    TileButton tileButton = new TileButton(j, i, board[i][j]);
                    if (this.isTilePickable(i, j, board))
                        tileButton.addActionListener(this.orderingTable);
                    else {
                        tileButton.setEnabled(false);
                    }
                    imagetileContainer.add(tileButton);
                }

                this.add(imagetileContainer, tileConstraints);
            }
        }
        this.revalidate();
        this.repaint();
    }

    /**
     * This method checks whether a tile at the specified position on the board is clickable or not.
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

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.background, 0, 0, this.getWidth(), this.getHeight(), null);
        if (!this.isLastTurn)
            g.drawImage(this.endGameToken, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}

