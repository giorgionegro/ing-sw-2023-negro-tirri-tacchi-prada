package view.GUI;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.abstractModel.*;
import modelView.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

import model.Tile;
import model.Token;
import view.ResourceProvider;
import view.interfaces.*;

public class GamePanel extends JComponent implements ActionListener, ShelfView, PlayerChatView, CommonGoalView, PlayerView, GameView, LivingRoomView, PersonalGoalView {
    private final Image parquet;
    private GameInfo currentGameState;
    private final LivingRoomPanel livingRoomBoard;
    private final Map<String,ShelfPanel> opponentShelfBoards = new HashMap<>();
    private ShelfPanel playerShelf;
    private final Arrows arrows;

    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    private final Table table;

    private final PersonalGoalPanel personalGoalPanel;
    private final CommonGoalsPanel commonGoalsPanel;
    private String thisPlayerId;

    private final ChatPanel chatPanel;

    private final List<PickedTile> selectedTiles = new ArrayList<>();
    public GamePanel(ServerInterface serverInterface, ClientInterface clientInterface) {
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;
        this.setMaximumSize(new Dimension(1200, 900));
        parquet = new ImageIcon(ResourceProvider.getResourcePath()+"/parquet.jpg").getImage();
        Font font = new Font("Century", Font.BOLD, 20);

        this.setLayout(null);

        //Table Panel
        table = new Table(this);
        table.setLocation(670, 290);
        this.add(table);

        //Game Board Panel
        livingRoomBoard = new LivingRoomPanel(table);
        livingRoomBoard.setBounds(0, 0, 600, 600);
        this.add(livingRoomBoard);

        //Arrows Panel
        arrows = new Arrows(this);
        arrows.setBounds(830,260,300,300);
        this.add(arrows);

        //Label <<CommonGoals>>
        JLabel commongoals = new JLabel("Common Goals:");
        commongoals.setFont(font);
        commongoals.setBounds(880, 630, 200, 30);
        this.add(commongoals);

        //CommonGoals Panel
        commonGoalsPanel = new CommonGoalsPanel();
        commonGoalsPanel.setBounds(750, 675, 600, 200);
        this.add(commonGoalsPanel);

        //Label <<PersonalGoal>>
        JLabel personalgoallabel = new JLabel("Personal Goal:");
        personalgoallabel.setFont(font);
        personalgoallabel.setBounds(1115, 250, 226, 150);
        this.add(personalgoallabel);

        //PersonalGoal Panel
        personalGoalPanel = new PersonalGoalPanel();
        personalGoalPanel.setBounds(1110, 350, 157, 230);
        this.add(personalGoalPanel);

        //Label <<Chat>>
        JLabel chatLabel = new JLabel("Chat:");
        chatLabel.setFont(font);
        chatLabel.setBounds(260, 600, 100, 40);
        this.add(chatLabel);

        //Chat
        chatPanel = new ChatPanel(serverInterface, clientInterface);
        chatPanel.setBounds(0, 640, 600, 140);
        this.add(chatPanel);

        //Label <<YOU>>
        JLabel player1 = new JLabel("YOU");
        player1.setFont(font);
        player1.setBounds(930, 590, 50, 50);
        this.add(player1);

        //Other_Players_Name Panel
        NamePlayersPanel namePlayersPanel = new NamePlayersPanel();
        namePlayersPanel.setBounds(680,200,900,60);
        this.add(namePlayersPanel);

    }

    public void setPlayerId(String playerId){
        this.thisPlayerId = playerId;
        chatPanel.setPlayerId(playerId);
    }

    static public String ReturnImageTile(Tile tile){
        return ResourceProvider.getResourcePath() + "/Tile/" + switch (tile) {
            case GAMES_1 -> "Giochi1.1.png";
            case GAMES_2 -> "Giochi1.2.png";
            case GAMES_3 -> "Giochi1.3.png";
            case FRAMES_1 -> "Cornici1.1.png";
            case FRAMES_2 -> "Cornici1.2.png";
            case FRAMES_3 -> "Cornici1.3.png";
            case CATS_1 -> "Gatti1.1.png";
            case CATS_2 -> "Gatti1.2.png";
            case CATS_3 -> "Gatti1.3.png";
            case BOOKS_1 -> "Libri1.1.png";
            case BOOKS_2 -> "Libri1.2.png";
            case BOOKS_3 -> "Libri1.3.png";
            case TROPHIES_1 -> "Trofei1.1.png";
            case TROPHIES_2 -> "Trofei1.2.png";
            case TROPHIES_3 -> "Trofei1.3.png";
            case PLANTS_1 -> "Piante1.1.png";
            case PLANTS_2 -> "Piante1.2.png";
            case PLANTS_3 -> "Piante1.3.png";
            case EMPTY -> "empty";
        };
    }

    static public String returnImageCommonGoal(String id){
        return ResourceProvider.getResourcePath() + "/commonGoals/GUI/" + switch(id){
            case "StandardTwoSquares"-> "1.jpg";
            case "Standard2ColumnsOfDifferentTiles"-> "2.jpg";
            case "Standard4Groups4Tiles"-> "3.jpg";
            case "StandardSixGroup2Tiles"-> "4.jpg";
            case "Standard3ColumnMax3Types"-> "5.jpg";
            case "Standard4RowsMax3Types"-> "6.jpg";
            case "Standard2RowsOfDifferentTiles"-> "7.jpg";
            case "StandardCorners"-> "8.jpg";
            case "Standard8TilesSameType" -> "9.jpg";
            case "StandardXOfDifferentTiles" -> "10.jpg";
            case "Standard5TileDiagonal"-> "11.jpg";
            case "StandardStairs"-> "12.jpg";
            default -> "";
        };
    }

    static public String returnImageToken(Token token){
        return ResourceProvider.getResourcePath() + "/Scoring/" + switch (token){
            case TOKEN_2_POINTS-> "scoring_2.png";
            case TOKEN_4_POINTS-> "scoring_4.png";
            case TOKEN_6_POINTS-> "scoring_6.png";
            case TOKEN_8_POINTS-> "scoring_8.png";
            case TOKEN_EMPTY ->"";
            case TOKEN_GAME_END ->"ciao";
        };
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(parquet!=null){
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
    }
        @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==table){
            String[] coordinates = e.getActionCommand().split("\n");
            this.selectedTiles.clear();

            for(int i=0; i<coordinates.length; i+=2)
                selectedTiles.add(
                        new PickedTile(
                                Integer.parseInt(coordinates[i+1]),
                                Integer.parseInt(coordinates[i])
                        )
                );
        } else if (e.getSource()==arrows) {
            if(selectedTiles.size()>0){
                int column = Integer.parseInt(e.getActionCommand());
                try {
                    serverInterface.doPlayerMove(clientInterface,new PlayerMoveInfo(selectedTiles,column));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    //TODO errore invio
                }
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
            if(playerShelf==null) {
                playerShelf = new ShelfPanel(800, 300, 300, 300);
                this.add(playerShelf);
            }
            playerShelf.updatePlayer(o.shelf());
        }else{
            ShelfPanel shelfPanel;
            if(!opponentShelfBoards.containsKey(o.playerId())) {
                shelfPanel = new ShelfPanel(603 + (opponentShelfBoards.size() * 200), 15, 200, 200);
                opponentShelfBoards.put(o.playerId(),shelfPanel);
                this.add(shelfPanel);
            }
            else{
                shelfPanel = opponentShelfBoards.get(o.playerId());
            }
            shelfPanel.updateOpponent(o.shelf());
        }
    }
}
