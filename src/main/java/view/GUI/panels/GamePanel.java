package view.GUI.panels;

import model.Tile;
import model.Token;
import model.abstractModel.Game;
import model.abstractModel.Message;
import view.GUI.layouts.AspectRatioLayout;
import view.ViewLogic;
import view.graphicInterfaces.GameGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO
public class GamePanel extends JComponent implements ActionListener, GameGraphics {
    private final ActionListener listener;
    private final Container livingRoomContainer = new Container();

    /*------------------------------------------------------------*/
    private final Container opponentsShelvesContainer = new Container();
    private final Container opponentLabelsContainer = new Container();
    private final Container tableContainer = new Container();
    private final Container playerShelfContainer = new Container();
    private final Container personalGoalContainer = new Container();
    private final Container chatContainer = new Container();
    private final Container commonGoalContainer = new Container();
    private final Container interactionContainer = new Container();
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();
    private final Image parquet = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/parquet.jpg"))).getImage();
    private final Color accent = Color.red;
    private final Color normal = Color.BLACK;
    private LivingRoomPanel livingRoomBoard;
    private Map<String, ShelfPanel> opponentShelfBoards;
    private Map<String, JLabel> opponentLabels;
    private PlayerShelfPanel playerShelf;
    private JLabel playerLabel;
    private TilesOrderingPanel tilesOrderingPanel;
    private PersonalGoalPanel personalGoalPanel;
    private CommonGoalsPanel commonGoalsPanel;
    private String thisPlayerId;
    private JLabel errorLabel;
    private ChatPanel chatPanel;

    private final Object errorLock = new Object();

    public GamePanel(ActionListener listener) {
        super();
        this.listener = listener;

        this.setLayout(new GridBagLayout());

        this.initializeGameContainers();
    }

    /**
     * This method initializes the containers for the Game Panel allowing you to make the page resizable.
     */
    private void initializeGameContainers() {
        this.livingRoomContainer.setLayout(new AspectRatioLayout(1));
        this.opponentsShelvesContainer.setLayout(new GridBagLayout());
        this.tableContainer.setLayout(new AspectRatioLayout((float) 205 / 635));
        this.playerShelfContainer.setLayout(new AspectRatioLayout((float) 1218 / 1389));
        this.personalGoalContainer.setLayout(new AspectRatioLayout((float) 756 / 1110));
        this.chatContainer.setLayout(new BorderLayout());
        this.commonGoalContainer.setLayout(new BorderLayout());
        this.opponentLabelsContainer.setLayout(new GridBagLayout());
        this.interactionContainer.setLayout(new GridBagLayout());

        this.playerLabel = new JLabel("YOU");
        Font font = new Font("Century", Font.BOLD, 20);
        this.playerLabel.setFont(font);
        this.playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.playerLabel.setBackground(new Color(255, 127, 39));
    }

    /**
     *
     */
    private void initializeGameLayout() {
        this.removeAll();

        GridBagConstraints livingRoomConstraints = new GridBagConstraints(
                0, 0,
                1, 5,
                16, 16,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.livingRoomContainer, livingRoomConstraints);

        GridBagConstraints opponentsShelvesConstraints = new GridBagConstraints(
                1, 0,
                3, 1,
                18, 5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.opponentsShelvesContainer, opponentsShelvesConstraints);

        GridBagConstraints tableConstraints = new GridBagConstraints(
                1, 2,
                1, 1,
                4, 8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.tableContainer, tableConstraints);


        GridBagConstraints playerShelfConstraints = new GridBagConstraints(
                2, 2,
                1, 1,
                8, 8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.playerShelfContainer, playerShelfConstraints);

        GridBagConstraints personalGoalConstraints = new GridBagConstraints(
                3, 2,
                1, 1,
                6, 8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.personalGoalContainer, personalGoalConstraints);

        GridBagConstraints chatConstraints = new GridBagConstraints(
                0, 5,
                1, 2,
                16, 6,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10),
                0, 0
        );
        this.add(this.chatContainer, chatConstraints);

        GridBagConstraints commonGoalConstraints = new GridBagConstraints(
                1, 5,
                3, 1,
                18, 6,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.commonGoalContainer, commonGoalConstraints);


        GridBagConstraints playerLabelConstraints = new GridBagConstraints(
                2, 4,
                1, 1,
                8, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.playerLabel, playerLabelConstraints);

        GridBagConstraints opponentLabelsConstraints = new GridBagConstraints(
                1, 1,
                3, 1,
                16, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.opponentLabelsContainer, opponentLabelsConstraints);


        GridBagConstraints interactionConstraints = new GridBagConstraints(
                1, 6,
                3, 1,
                1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.add(this.interactionContainer, interactionConstraints);
    }

    public void resetGameLayout(String newPlayerId) {
        this.thisPlayerId = newPlayerId;
        this.opponentShelfBoards = new HashMap<>();
        this.opponentLabels = new HashMap<>();
        this.initializeGameLayout();

        this.livingRoomContainer.removeAll();
        this.opponentsShelvesContainer.removeAll();
        this.opponentLabelsContainer.removeAll();
        this.tableContainer.removeAll();
        this.playerShelfContainer.removeAll();
        this.personalGoalContainer.removeAll();
        this.chatContainer.removeAll();
        this.commonGoalContainer.removeAll();
        this.commonGoalContainer.removeAll();
        this.interactionContainer.removeAll();

        this.tilesOrderingPanel = new TilesOrderingPanel(this);
        this.tableContainer.add(this.tilesOrderingPanel);

        this.playerShelf = new PlayerShelfPanel(this.tilesOrderingPanel);
        this.tilesOrderingPanel.setColumnChooser(this.playerShelf);
        this.playerShelfContainer.add(this.playerShelf);

        this.livingRoomBoard = new LivingRoomPanel(this.tilesOrderingPanel);
        this.livingRoomContainer.add(this.livingRoomBoard);

        this.chatPanel = new ChatPanel(this, newPlayerId);
        this.chatContainer.add(this.chatPanel);

        this.personalGoalPanel = new PersonalGoalPanel();
        this.personalGoalContainer.add(this.personalGoalPanel);

        this.commonGoalsPanel = new CommonGoalsPanel();
        this.commonGoalContainer.add(this.commonGoalsPanel);

        this.errorLabel = new JLabel("");
        this.errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints errorLabelConstraints = new GridBagConstraints(
                0, 0,
                1, 1,
                6, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.interactionContainer.add(this.errorLabel, errorLabelConstraints);

        JButton exitButton = new JButton("EXIT") {
            protected void paintComponent(Graphics g) {
                g.drawImage(GamePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
                super.paintComponent(g);
            }
        };
        exitButton.setBackground(new Color(0, 0, 0, 0));
        exitButton.addActionListener(e ->
                this.listener.actionPerformed(new ActionEvent(this, ViewLogic.LEAVE_GAME, ""))
        );
        exitButton.setPreferredSize(new Dimension(0, 0));
        GridBagConstraints exitButtonConstraint = new GridBagConstraints(
                1, 0,
                1, 1,
                1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        this.interactionContainer.add(exitButton, exitButtonConstraint);
    }

    /*------------------------------------------------------------*/
    private void addPlayerShelf(JPanel shelfPanel, String playerId) {
        Container shelfContainer = new Container();
        shelfContainer.setLayout(new AspectRatioLayout((float) 1218 / 1218));
        shelfContainer.add(shelfPanel);

        Font font = new Font("Century", Font.BOLD, 18);
        JLabel playerLabel = new JLabel(playerId);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(font);
        //playerLabel.setBackground(new Color(255,127,39));


        GridBagConstraints shelfConstraints = new GridBagConstraints(
                -1, -1,
                1, 1,
                1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );

        this.opponentsShelvesContainer.add(shelfContainer, shelfConstraints);
        this.opponentLabelsContainer.add(playerLabel, shelfConstraints);
        this.opponentLabels.put(playerId, playerLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double ratio = 1.765;
        double windowRatio = (double) this.getWidth() / this.getHeight();
        int width;
        int height;
        if (windowRatio > ratio) {
            width = this.getWidth();
            height = (int) (this.getWidth() / ratio);
        } else {
            height = this.getHeight();
            width = (int) (this.getHeight() * ratio);
        }
        g.drawImage(this.parquet, 0, 0, width, height, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.listener.actionPerformed(new ActionEvent(this, e.getID(), e.getActionCommand()));
    }

    @Override
    public void updateCommonGoalGraphics(String id, String description, Token tokenState) {
        this.commonGoalsPanel.updateCommonGoalGraphics(id, description, tokenState);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void resetGameGraphics(String playerId) {
        this.resetGameLayout(playerId);
    }

    @Override
    public void updateGameInfoGraphics(Game.GameStatus status, String firstTurnPlayer, String playerOnTurn, boolean isLastTurn, Map<String, Integer> pointsValues) {
        this.playerLabel.setForeground(this.normal);
        for (JLabel label : this.opponentLabels.values())
            label.setForeground(this.normal);

        if (playerOnTurn.equals(this.thisPlayerId))
            this.playerLabel.setForeground(this.accent);
        else
            this.opponentLabels.get(playerOnTurn).setForeground(this.accent);

        if (status == Game.GameStatus.ENDED) {
            this.removeAll();
            WinnerGamePanel winnerPanel = new WinnerGamePanel(this, pointsValues, this.thisPlayerId);
            GridBagConstraints winnerConstraints = new GridBagConstraints(
                    0, 0,
                    10, 10,
                    1, 1,
                    GridBagConstraints.NORTHWEST,
                    GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0),
                    0, 0
            );
            this.add(winnerPanel, winnerConstraints);
        } else if (status == Game.GameStatus.STARTED) {
            this.playerShelf.setIsFirstPlayer(this.thisPlayerId.equals(firstTurnPlayer));
            for (String s : this.opponentShelfBoards.keySet()) {
                this.opponentShelfBoards.get(s).setIsFirstPlayer(s.equals(firstTurnPlayer));
            }
            livingRoomBoard.setIsLastTurn(isLastTurn);
        } else if (status == Game.GameStatus.MATCHMAKING || status == Game.GameStatus.SUSPENDED) {
            //TODO show message and not commonGoal personalGoal and LivingRoom
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updateBoardGraphics(Tile[][] board) {
        this.livingRoomBoard.updateBoardGraphics(board);
        this.tilesOrderingPanel.actionPerformed(new ActionEvent(this, 0, "CLEAR SELECTION"));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description) {
        this.personalGoalPanel.updatePersonalGoalGraphics(id, hasBeenAchieved, description);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updatePlayerChatGraphics(List<? extends Message> chat) {
        this.chatPanel.updatePlayerChatGraphics(chat);
        this.revalidate();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     * @param achievedCommonGoals map of achieved common goals with earned token
     */
    @Override
    public void updateAchievedCommonGoals(Map<String, Token> achievedCommonGoals) {
        this.commonGoalsPanel.setAchievedCommonGoals(achievedCommonGoals);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updateErrorState(String reportedError) {
        synchronized (errorLock) {
            this.errorLabel.setText(reportedError);
        }
        //expire error message after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (errorLock) {
                this.errorLabel.setText("");
                this.revalidate();
                this.repaint();
            }
        }).start();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        this.chatPanel.addSubject(playerId);

        if (playerId.equals(this.thisPlayerId)) {
            this.playerShelf.updatePlayerShelfGraphics(playerId, shelf);
        } else {
            ShelfPanel shelfPanel;
            if (!this.opponentShelfBoards.containsKey(playerId)) {
                shelfPanel = new ShelfPanel();
                this.addPlayerShelf(shelfPanel, playerId);
                this.opponentShelfBoards.put(playerId, shelfPanel);
            } else {
                shelfPanel = this.opponentShelfBoards.get(playerId);
            }
            shelfPanel.updatePlayerShelfGraphics(playerId, shelf);
        }
        this.revalidate();
        this.repaint();
    }

}
