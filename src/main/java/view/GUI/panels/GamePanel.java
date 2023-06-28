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


/**
 *  This class extends JComponent manage game play graphics
 */
public class GamePanel extends JComponent implements GameGraphics {
    /**
     * The ActionListener used to send game actions
     */
    private final ActionListener listener;
    /**
     * This container is the layout placeholder for {@link #livingRoomBoard}
     */
    private final Container livingRoomContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #opponentShelfBoards}
     */
    private final Container opponentsShelvesContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #opponentLabels}
     */
    private final Container opponentLabelsContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #tilesOrderingPanel}
     */
    private final Container tableContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #playerShelf}
     */
    private final Container playerShelfContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #personalGoalPanel}
     */
    private final Container personalGoalContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #chatPanel}
     */
    private final Container chatContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #commonGoalsPanel}
     */
    private final Container commonGoalContainer = new Container();
    /**
     * This container is the layout placeholder for {@link #errorLabel}
     */
    private final Container interactionContainer = new Container();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image parquet = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/parquet.jpg"))).getImage();
    /**
     * The panel which displays living room board
     */
    private LivingRoomPanel livingRoomBoard;
    /**
     * Map that associates opponents playerID to the panel which displays his shelf
     */
    private final Map<String, ShelfPanel> opponentShelfBoards = new HashMap<>();
    /**
     * Map that associates opponents playerID to the panel which displays his PlayerID
     */
    private final Map<String, JLabel> opponentLabels = new HashMap<>();
    /**
     * The panel which displays this player shelf
     */
    private PlayerShelfPanel playerShelf;
    /**
     * The panel which allows to order picked tiles
     */
    private TilesOrderingPanel tilesOrderingPanel;
    /**
     * The panel that displays the player's personal goal
     */
    private PersonalGoalPanel personalGoalPanel;
    /**
     * The panel that displays the game's common goals
     */
    private CommonGoalsPanel commonGoalsPanel;
    /**
     * The label associated to the player's playerID
     */
    private final JLabel playerLabel = new JLabel("YOU");
    /**
     * The label that shows a waiting message during matchmaking
     */
    private final JLabel waitingLabel = new JLabel("WAITING FOR PLAYERS TO JOIN");
    /**
     * Label that shows an error when occurred
     */
    private final JLabel errorLabel = new JLabel("");
    /**
     * This player's playerID
     */
    private String thisPlayerId;
    /**
     * The playerID of the player that is on turn
     */
    private String playerOnTurn;
    /**
     * The playerID of the first player on turn
     */
    private String firstPlayer;
    /**
     * Panel that displays the chat
     */
    private ChatPanel chatPanel;
    /** Construct an {@link GamePanel} instance that uses the given {@link ActionListener} as listener for game actions
     * @param listener the ActionListener to be notified when a game action need to be sent
     */
    public GamePanel(ActionListener listener) {
        super();
        this.listener = listener;

        this.setLayout(new GridBagLayout());

        this.initializeGameContainers();
    }

    /**
     * This method initializes the layout placeholders
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


        Font font = new Font("Century", Font.BOLD, 15);

        this.playerLabel.setFont(font);
        this.playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.playerLabel.setPreferredSize(new Dimension(0,0));
        this.errorLabel.setFont(font);
        this.errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.errorLabel.setPreferredSize(new Dimension(0,0));
        this.waitingLabel.setFont(font);
        this.waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.waitingLabel.setPreferredSize(new Dimension(0,0));
    }

    /**
     * This method initializes the layout
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

    /**
     * This method resets the gameplay graphic
     */
    private void resetGameLayout() {
        this.opponentShelfBoards.clear();
        this.opponentLabels.clear();
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

        this.tilesOrderingPanel = new TilesOrderingPanel(
                e -> this.listener.actionPerformed(new ActionEvent(this, e.getID(), e.getActionCommand())),
                e -> this.playerShelf.actionPerformed(new ActionEvent(e.getSource(), PlayerShelfPanel.UPDATE_CHOICES, e.getActionCommand()))
        );

        this.playerShelf = new PlayerShelfPanel(
                e -> this.tilesOrderingPanel.actionPerformed(new ActionEvent(e.getSource(), TilesOrderingPanel.CONFIRM, e.getActionCommand()))
        );

        this.playerShelfContainer.add(this.playerShelf);

        this.livingRoomBoard = new LivingRoomPanel(
                e -> this.tilesOrderingPanel.actionPerformed(new ActionEvent(e.getSource(), TilesOrderingPanel.SELECT_TILE, e.getActionCommand()))
        );

        this.chatPanel = new ChatPanel(
                e -> this.listener.actionPerformed(new ActionEvent(this, e.getID(), e.getActionCommand())),
                this.thisPlayerId
        );
        this.chatContainer.add(this.chatPanel);

        this.personalGoalPanel = new PersonalGoalPanel();

        this.commonGoalsPanel = new CommonGoalsPanel();

        this.errorLabel.setText("");
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

        Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();
        JButton exitButton = new JButton("EXIT") {
            protected void paintComponent(Graphics g) {
                g.drawImage(buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
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

    /**
     * This method modifies the graphic in order to display a waiting scene
     */
    private void showMatchmakingScene(){
        this.tableContainer.removeAll();
        this.livingRoomContainer.removeAll();
        this.personalGoalContainer.removeAll();
        this.commonGoalContainer.removeAll();

        this.playerShelf.enableButtons(false);
        this.livingRoomContainer.add(this.waitingLabel, BorderLayout.CENTER);
    }

    /**
     * This method modifies the graphic in order to display gameplay scene
     */
    private void showGameScene(){
        this.tableContainer.removeAll();
        this.livingRoomContainer.removeAll();
        this.personalGoalContainer.removeAll();
        this.commonGoalContainer.removeAll();

        this.playerShelf.enableButtons(true);
        this.tableContainer.add(this.tilesOrderingPanel);
        this.livingRoomContainer.add(this.livingRoomBoard);
        this.personalGoalContainer.add(this.personalGoalPanel);
        this.commonGoalContainer.add(this.commonGoalsPanel);
    }

    /**
     * This method updates first player graphics
     */
    private void updateFirstPlayer(){
        this.playerLabel.setForeground(Color.BLACK);
        for (JLabel label : this.opponentLabels.values())
            label.setForeground(Color.BLACK);


        if (this.playerOnTurn.equals(this.thisPlayerId)) {
            this.playerLabel.setForeground(Color.RED);
        } else {
            if(this.opponentLabels.containsKey(this.playerOnTurn))
                this.opponentLabels.get(this.playerOnTurn).setForeground(Color.RED);
        }
    }
    /**
     * This method updates player on turn graphics
     */
    private void updatePlayerOnTurn(){
        this.playerShelf.setIsFirstPlayer(this.thisPlayerId.equals(this.firstPlayer));
        for (String s : this.opponentShelfBoards.keySet()) {
            this.opponentShelfBoards.get(s).setIsFirstPlayer(s.equals(this.firstPlayer));
        }
    }

    /**
     * {@inheritDoc}
     * @param id          the unique id of the common goal
     * @param description the description of common goal specs
     * @param tokenState  the current token value of the common goal
     */
    @Override
    public void updateCommonGoalGraphics(String id, String description, Token tokenState) {
        this.commonGoalsPanel.updateCommonGoalGraphics(id, description, tokenState);
        this.revalidate();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     * @param playerId the player ID used in the next game
     */
    @Override
    public void resetGameGraphics(String playerId) {
        this.thisPlayerId = playerId;
        this.resetGameLayout();
        this.showMatchmakingScene();
    }

    /**
     * {@inheritDoc}
     * @param status          status of the game
     * @param firstTurnPlayer id of the player that has the first turn
     * @param playerOnTurn    id of the player on turn
     * @param isLastTurn      true if the game is in the last round of turns
     * @param pointsValues    points amount of each player
     */
    @Override
    public void updateGameInfoGraphics(Game.GameStatus status, String firstTurnPlayer, String playerOnTurn, boolean isLastTurn, Map<String, Integer> pointsValues) {
        if (status == Game.GameStatus.ENDED) {
            this.removeAll();
            WinnerGamePanel winnerPanel = new WinnerGamePanel(e -> this.listener.actionPerformed(new ActionEvent(this, e.getID(), e.getActionCommand())), pointsValues, this.thisPlayerId);
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
            this.playerOnTurn = playerOnTurn;
            this.firstPlayer = firstTurnPlayer;
            this.livingRoomBoard.setIsLastTurn(isLastTurn);
            this.tilesOrderingPanel.actionPerformed(new ActionEvent(this,TilesOrderingPanel.RESET,""));
            this.showGameScene();
            this.updatePlayerOnTurn();
            this.updateFirstPlayer();
        } else if (status == Game.GameStatus.MATCHMAKING || status == Game.GameStatus.SUSPENDED) {
            this.showMatchmakingScene();
        }
        this.revalidate();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     * @param board board of the living room
     */
    @Override
    public void updateBoardGraphics(Tile[][] board) {
        this.livingRoomBoard.updateBoardGraphics(board);
        this.tilesOrderingPanel.actionPerformed(new ActionEvent(this, 0, "CLEAR SELECTION"));
        this.revalidate();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     * @param id              the id of this goal, unique among others player personal goal
     * @param hasBeenAchieved true if the goal is achieved
     * @param description     matrix representation of the goal
     */
    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description) {
        this.personalGoalPanel.updatePersonalGoalGraphics(id, hasBeenAchieved, description);
        this.revalidate();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     * @param chat list of messages sent to a player
     */
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

    /**
     * {@inheritDoc}
     * @param reportedError message of en error encountered during gameplay
     */
    @Override
    public void updateErrorState(String reportedError) {
        synchronized (this.errorLabel) {
            this.errorLabel.setText(reportedError);
        }
        //expire error message after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this.errorLabel) {
                this.errorLabel.setText("");
                this.revalidate();
                this.repaint();
            }
        }).start();
        this.revalidate();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     * @param playerId id of the player that owns the shelf
     * @param shelf    shelf representation of the player
     */
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

        this.updatePlayerOnTurn();
        this.updateFirstPlayer();

        this.revalidate();
        this.repaint();
    }

    /**
     * This method adds an opponent shelf to the graphics
     * @param shelfPanel opponent shelf panel
     * @param playerId opponent playerID
     */
    private void addPlayerShelf(JPanel shelfPanel, String playerId) {
        Container shelfContainer = new Container();
        shelfContainer.setLayout(new AspectRatioLayout((float) 1218 / 1218));
        shelfContainer.add(shelfPanel);

        Font font = new Font("Century", Font.BOLD, 15);
        JLabel playerLabel = new JLabel(playerId);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(font);
        playerLabel.setPreferredSize(new Dimension(0,0));


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

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
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

}
