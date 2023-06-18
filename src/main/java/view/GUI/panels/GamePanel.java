package view.GUI.panels;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.*;
import modelView.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JComponent;


import view.GUI.*;
import view.interfaces.*;

public class GamePanel extends JComponent implements ActionListener, ShelfView, PlayerChatView, CommonGoalView, PlayerView, GameView, LivingRoomView, PersonalGoalView {
    private final Image parquet = new ImageIcon(Objects.requireNonNull(getClass().getResource("/parquet.jpg"))).getImage();

    private LivingRoomPanel livingRoomBoard;
    private Map<String, ShelfPanel> opponentShelfBoards;
    private Map<String,JLabel> opponentLabels;
    private PlayerShelfPanel playerShelf;
    private JLabel playerLabel;
    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    private TilesOrderingPanel tilesOrderingPanel;
    private PersonalGoalPanel personalGoalPanel;
    private CommonGoalsPanel commonGoalsPanel;
    private String thisPlayerId;
    private WinnerGamePanel winnerPanel;

    private JButton exitButton;

    private JLabel errorLabel;
    private ChatPanel chatPanel;

    private final ActionListener listener;

    public GamePanel(ActionListener listener, ServerInterface serverInterface, ClientInterface clientInterface){
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;

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
                new Insets(0,0,0,0),
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

    public void resetGameLayout(){
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
        tilesOrderingPanel.setColumnChoser(playerShelf);
        playerShelfContainer.add(playerShelf);

        livingRoomBoard = new LivingRoomPanel(tilesOrderingPanel);
        livingRoomContainer.add(livingRoomBoard);

        chatPanel = new ChatPanel(serverInterface, clientInterface);
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

        exitButton = new JButton("EXIT");
        exitButton.addActionListener(this);
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

    public void setPlayerId(String playerId){
        this.thisPlayerId = playerId;
        chatPanel.setPlayerId(playerId);
    }

    private void addPlayerShelf(JPanel shelfPanel, String playerId){
        Container shelfContainer = new Container();
        shelfContainer.setLayout(new AspectRatioLayout((float)1218/1218));
        shelfContainer.add(shelfPanel);

        Font font = new Font("Century", Font.BOLD, 18);
        JLabel playerLabel = new JLabel(playerId);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(font);
        playerLabel.setBackground(new Color(255,127,39));


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
        if (e.getSource() instanceof PlayerMoveInfo) {
            try {
                serverInterface.doPlayerMove(clientInterface, (PlayerMoveInfo) e.getSource());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() instanceof Message) {
            try {
                serverInterface.sendMessage(clientInterface, (Message) e.getSource());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == exitButton) {
            try {
                serverInterface.leaveGame(clientInterface);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);//TODO
            }
            listener.actionPerformed(new ActionEvent(this,e.getID(),"EXIT"));
        } else if (e.getSource() == winnerPanel) {

            listener.actionPerformed(new ActionEvent(this,e.getID(),"EXIT"));
        }
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        commonGoalsPanel.update(o,evt);
    }

    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {
        if(evt == Game.Event.NEXT_TURN){
            playerLabel.setOpaque(false);
            for(JLabel label : opponentLabels.values())
                label.setOpaque(false);

            if(o.playerOnTurn().equals(thisPlayerId))
                playerLabel.setOpaque(true);
            else
                opponentLabels.get(o.playerOnTurn()).setOpaque(true);
        }else if (o.status() == Game.GameStatus.ENDED) {
            this.removeAll();
            winnerPanel = new WinnerGamePanel(this,o.points());
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
        } else if (o.status() == Game.GameStatus.STARTED) {
           //TODO
        }else if (o.status() == Game.GameStatus.MATCHMAKING) {
            //TODO
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        livingRoomBoard.update(o,evt);
        tilesOrderingPanel.actionPerformed(new ActionEvent(this, 0, "CLEAR SELECTION"));
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        personalGoalPanel.update(o,evt);
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        chatPanel.update(o,evt);
    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
        commonGoalsPanel.update(o,evt);
        errorLabel.setText(o.errorMessage());
        this.revalidate();
        this.repaint();
    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
        chatPanel.update(o,evt);

        if(o.playerId().equals(thisPlayerId)){
            playerShelf.update(o,evt);
        }else{
            ShelfPanel shelfPanel;
            if(!opponentShelfBoards.containsKey(o.playerId())) {
                shelfPanel = new ShelfPanel();
                addPlayerShelf(shelfPanel,o.playerId());
                opponentShelfBoards.put(o.playerId(),shelfPanel);
            }
            else{
                shelfPanel = opponentShelfBoards.get(o.playerId());
            }
            shelfPanel.update(o,evt);
        }
    }
}
