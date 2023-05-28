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
import javax.swing.JComponent;

import util.ResourceProvider;
import view.GUI.*;
import view.interfaces.*;

public class GamePanel extends JComponent implements ActionListener, ShelfView, PlayerChatView, CommonGoalView, PlayerView, GameView, LivingRoomView, PersonalGoalView {
    private final Image parquet = new ImageIcon(ResourceProvider.getResourcePath("/parquet.jpg")).getImage();
    private GameInfo currentGameState;
    private final LivingRoomPanel livingRoomBoard;
    private final Map<String, ShelfPanel> opponentShelfBoards = new HashMap<>();
    private final PlayerShelfPanel playerShelf;

    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    private final TilesOrderingPanel tilesOrderingPanel;
    private final PersonalGoalPanel personalGoalPanel;
    private final CommonGoalsPanel commonGoalsPanel;
    private String thisPlayerId;

    private final ChatPanel chatPanel;

    public GamePanel(ServerInterface serverInterface, ClientInterface clientInterface){
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;

        initializeLayout();

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

        this.revalidate();
        this.repaint();
    }

    /*------------------------------------------------------------*/

    private final Container livingRoomContainer = new Container();

    private final Container opponentsShelvesContainer = new Container();
    private final Container opponentLabels = new Container();

    private final Container tableContainer = new Container();

    private final Container playerShelfContainer = new Container();

    private final Container personalGoalContainer = new Container();

    private final Container chatContainer = new Container();

    private final Container commonGoalContainer = new Container();

    private final JLabel playerLabel = new JLabel();

    private void initializeLayout(){
        this.setLayout(new GridBagLayout());

        livingRoomContainer.setLayout(new AspectRatioLayout(1));
        GridBagConstraints livingRoomContraints= new GridBagConstraints(
                0,0,
                1,5,
                16,16,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(livingRoomContainer,livingRoomContraints);

        opponentsShelvesContainer.setLayout(new GridBagLayout());
        GridBagConstraints opponentsShelvesConstraints = new GridBagConstraints(
                1,0,
                3,1,
                18,5,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(opponentsShelvesContainer, opponentsShelvesConstraints);


        tableContainer.setLayout(new AspectRatioLayout((float)205/635));
        GridBagConstraints tableConstraints = new GridBagConstraints(
                1,2,
                1,1,
                4,8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(tableContainer, tableConstraints);

        playerShelfContainer.setLayout(new AspectRatioLayout((float)1218/1389));
        GridBagConstraints playerShelfConstraints = new GridBagConstraints(
                2,2,
                1,1,
                8,8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(playerShelfContainer, playerShelfConstraints);

        personalGoalContainer.setLayout(new AspectRatioLayout((float)756/1110));
        GridBagConstraints personalGoalConstraints = new GridBagConstraints(
                3,2,
                1,1,
                6,8,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(personalGoalContainer, personalGoalConstraints);

        chatContainer.setLayout(new BorderLayout());
        GridBagConstraints chatConstraints = new GridBagConstraints(
                0,5,
                1,1,
                16,6,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(chatContainer, chatConstraints);

        commonGoalContainer.setLayout(new BorderLayout());
        GridBagConstraints commonGoalConstraints = new GridBagConstraints(
                1,5,
                3,1,
                18,6,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(commonGoalContainer, commonGoalConstraints);

        playerLabel.setText("YOU");
        Font font = new Font("Century", Font.BOLD, 20);
        playerLabel.setFont(font);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints playerLabelConstraints = new GridBagConstraints(
                2,4,
                1,1,
                8,1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(playerLabel,playerLabelConstraints);

        opponentLabels.setLayout(new GridBagLayout());
        GridBagConstraints opponentLabelsConstraints = new GridBagConstraints(
                1,1,
                3,1,
                16,1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );
        this.add(opponentLabels,opponentLabelsConstraints);
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
        opponentLabels.add(playerLabel,shelfConstraints);
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
            //TODO pulisci selezione
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
        }
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        commonGoalsPanel.update(o,evt);
    }

    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {

    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        livingRoomBoard.update(o,evt);
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
