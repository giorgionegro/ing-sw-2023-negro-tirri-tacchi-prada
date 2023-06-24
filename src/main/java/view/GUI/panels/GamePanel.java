package view.GUI.panels;

import model.Tile;
import model.Token;
import model.abstractModel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JComponent;


import view.GUI.*;
import view.ViewLogic;
import view.graphicInterfaces.GameGraphics;

public class GamePanel extends JComponent implements ActionListener, GameGraphics{
    private final ActionListener listener;
    public GamePanel(ActionListener listener){
        this.listener = listener;

        this.setLayout(new GridBagLayout());

        initializeGameContainers();
    }

    /*------------------------------------------------------------*/

    private final Container livingRoomContainer = new Container();

    private final Container opponentsShelvesContainer = new Container();
    private final Container opponentLabelsContainer = new Container();

    private final Container tableContainer = new Container();

    private final Container playerShelfContainer = new Container();

    private final Container personalGoalContainer = new Container();

    private final Container chatContainer = new Container();

    private final Container commonGoalContainer = new Container();

    private final Container interactionContainer = new Container();

    /**
     * This method initializes the containers for the Game Panel allowing you to make the page resizable.
     */
    private void initializeGameContainers(){
        livingRoomContainer.setLayout(new AspectRatioLayout(1));
        opponentsShelvesContainer.setLayout(new GridBagLayout());
        tableContainer.setLayout(new AspectRatioLayout((float)205/635));
        playerShelfContainer.setLayout(new AspectRatioLayout((float)1218/1389));
        personalGoalContainer.setLayout(new AspectRatioLayout((float)756/1110));
        chatContainer.setLayout(new BorderLayout());
        commonGoalContainer.setLayout(new BorderLayout());
        opponentLabelsContainer.setLayout(new GridBagLayout());
        interactionContainer.setLayout(new GridBagLayout());

        playerLabel = new JLabel("YOU");
        Font font = new Font("Century", Font.BOLD, 20);
        playerLabel.setFont(font);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setBackground(new Color(255,127,39));
    }

    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    private final Image parquet = new ImageIcon(Objects.requireNonNull(getClass().getResource("/parquet.jpg"))).getImage();
    private LivingRoomPanel livingRoomBoard;
    private Map<String, ShelfPanel> opponentShelfBoards;
    private Map<String,JLabel> opponentLabels;
    private PlayerShelfPanel playerShelf;
    private JLabel playerLabel;
    private TilesOrderingPanel tilesOrderingPanel;
    private PersonalGoalPanel personalGoalPanel;
    private CommonGoalsPanel commonGoalsPanel;
    private String thisPlayerId;
    private JLabel errorLabel;
    private ChatPanel chatPanel;

    /**
     *
     */
    private void initializeGameLayout(){
        this.removeAll();

        GridBagConstraints livingRoomContraints= new GridBagConstraints(
                0,0,
                1,5,
                16,16,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(livingRoomContainer,livingRoomContraints);

        GridBagConstraints opponentsShelvesConstraints = new GridBagConstraints(
                1,0,
                3,1,
                18,5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(opponentsShelvesContainer, opponentsShelvesConstraints);

        GridBagConstraints tableConstraints = new GridBagConstraints(
                1,2,
                1,1,
                4,8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(tableContainer, tableConstraints);


        GridBagConstraints playerShelfConstraints = new GridBagConstraints(
                2,2,
                1,1,
                8,8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(playerShelfContainer, playerShelfConstraints);

        GridBagConstraints personalGoalConstraints = new GridBagConstraints(
                3,2,
                1,1,
                6,8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(personalGoalContainer, personalGoalConstraints);

        GridBagConstraints chatConstraints = new GridBagConstraints(
                0,5,
                1,2,
                16,6,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(10,10,10,10),
                0,0
        );
        this.add(chatContainer, chatConstraints);

        GridBagConstraints commonGoalConstraints = new GridBagConstraints(
                1,5,
                3,1,
                18,6,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(commonGoalContainer, commonGoalConstraints);


        GridBagConstraints playerLabelConstraints = new GridBagConstraints(
                2,4,
                1,1,
                8,1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(playerLabel,playerLabelConstraints);

        GridBagConstraints opponentLabelsConstraints = new GridBagConstraints(
                1,1,
                3,1,
                16,1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(opponentLabelsContainer,opponentLabelsConstraints);


        GridBagConstraints interactionConstraints = new GridBagConstraints(
                1,6,
                3,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(interactionContainer,interactionConstraints);
    }

    public void resetGameLayout(String newPlayerId){
        thisPlayerId = newPlayerId;
        opponentShelfBoards = new HashMap<>();
        opponentLabels = new HashMap<>();
        initializeGameLayout();

        livingRoomContainer.removeAll();
        opponentsShelvesContainer.removeAll();
        opponentLabelsContainer.removeAll();
        tableContainer.removeAll();
        playerShelfContainer.removeAll();
        personalGoalContainer.removeAll();
        chatContainer.removeAll();
        commonGoalContainer.removeAll();
        commonGoalContainer.removeAll();
        interactionContainer.removeAll();

        tilesOrderingPanel = new TilesOrderingPanel(this);
        tableContainer.add(tilesOrderingPanel);

        playerShelf = new PlayerShelfPanel(tilesOrderingPanel);
        tilesOrderingPanel.setColumnChooser(playerShelf);
        playerShelfContainer.add(playerShelf);

        livingRoomBoard = new LivingRoomPanel(tilesOrderingPanel);
        livingRoomContainer.add(livingRoomBoard);

        chatPanel = new ChatPanel(this,newPlayerId);
        chatContainer.add(chatPanel);

        personalGoalPanel = new PersonalGoalPanel();
        personalGoalContainer.add(personalGoalPanel);

        commonGoalsPanel = new CommonGoalsPanel();
        commonGoalContainer.add(commonGoalsPanel);

        errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints errorLabelConstraints = new GridBagConstraints(
                0,0,
                1,1,
                6,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        interactionContainer.add(errorLabel,errorLabelConstraints);

        JButton exitButton = new JButton("EXIT"){
            protected void paintComponent(Graphics g) {
                g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
                super.paintComponent(g);
            }
        };
        exitButton.setBackground(new Color(0,0,0,0));
        exitButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this,ViewLogic.LEAVE_GAME,""))
        );
        exitButton.setPreferredSize(new Dimension(0,0));
        GridBagConstraints exitButtonConstraint = new GridBagConstraints(
                1,0,
                1,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        interactionContainer.add(exitButton,exitButtonConstraint);
    }

    /*------------------------------------------------------------*/
    private void addPlayerShelf(JPanel shelfPanel, String playerId){
        Container shelfContainer = new Container();
        shelfContainer.setLayout(new AspectRatioLayout((float)1218/1218));
        shelfContainer.add(shelfPanel);

        Font font = new Font("Century", Font.BOLD, 18);
        JLabel playerLabel = new JLabel(playerId);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(font);
        //playerLabel.setBackground(new Color(255,127,39));


        GridBagConstraints shelfConstraints = new GridBagConstraints(
                -1,-1,
                1,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );

        opponentsShelvesContainer.add(shelfContainer,shelfConstraints);
        opponentLabelsContainer.add(playerLabel,shelfConstraints);
        opponentLabels.put(playerId,playerLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double ratio = 1.765;
        double windowRatio = (double)getWidth()/getHeight();
        int width;
        int height;
        if(windowRatio>ratio) {
            width = getWidth();
            height = (int) (getWidth()/ratio);
        }else{
            height = getHeight();
            width = (int) (getHeight()*ratio);
        }
        g.drawImage(parquet, 0, 0, width, height, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed(new ActionEvent(this, e.getID(),e.getActionCommand()));
    }

    @Override
    public void updateCommonGoalGraphics(String id, String description, Token tokenState) {
        commonGoalsPanel.updateCommonGoalGraphics(id,description,tokenState);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void resetGameGraphics(String playerId) {
        resetGameLayout(playerId);
    }

    private final Color accent = Color.red;
    private final Color normal = new Color(0,0,0);
    @Override
    public void updateGameInfoGraphics(Game.GameStatus status, String playerOnTurn, boolean isLastTurn, Map<String, Integer> pointsValues) {
        playerLabel.setForeground(normal);
        for(JLabel label : opponentLabels.values())
            label.setForeground(normal);

        if(playerOnTurn.equals(thisPlayerId))
            playerLabel.setForeground(accent);
        else
            opponentLabels.get(playerOnTurn).setForeground(accent);

        if (status == Game.GameStatus.ENDED) {
            this.removeAll();
            WinnerGamePanel winnerPanel = new WinnerGamePanel(this,pointsValues,thisPlayerId);
            GridBagConstraints winnerConstraints = new GridBagConstraints(
                    0,0,
                    10,10,
                    1,1,
                    GridBagConstraints.NORTHWEST,
                    GridBagConstraints.BOTH,
                    new Insets(0,0,0,0),
                    0,0
            );
            this.add(winnerPanel,winnerConstraints);
        } else if (status == Game.GameStatus.STARTED) {
            //TODO show points on playerShelf
        }else if (status == Game.GameStatus.MATCHMAKING) {
            //TODO
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updateBoardGraphics(Tile[][] board) {
        livingRoomBoard.updateBoardGraphics(board);
        tilesOrderingPanel.actionPerformed(new ActionEvent(this, 0, "CLEAR SELECTION"));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description) {
        personalGoalPanel.updatePersonalGoalGraphics(id,hasBeenAchieved,description);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updatePlayerChatGraphics(List<Message> chat) {
        chatPanel.updatePlayerChatGraphics(chat);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updateAchievedCommonGoals(Map<String, Token> achievedCommonGoals) {
        commonGoalsPanel.setAchievedCommonGoals(achievedCommonGoals);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updateErrorState(String reportedError) {
        errorLabel.setText(reportedError);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        chatPanel.addSubject(playerId);

        if(playerId.equals(thisPlayerId)){
            playerShelf.updatePlayerShelfGraphics(playerId,shelf);
        }else{
            ShelfPanel shelfPanel;
            if(!opponentShelfBoards.containsKey(playerId)) {
                shelfPanel = new ShelfPanel();
                addPlayerShelf(shelfPanel,playerId);
                opponentShelfBoards.put(playerId,shelfPanel);
            }
            else{
                shelfPanel = opponentShelfBoards.get(playerId);
            }
            shelfPanel.updatePlayerShelfGraphics(playerId,shelf);
        }
        this.revalidate();
        this.repaint();
    }

}
