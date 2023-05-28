package view.GUI;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import modelView.*;
import util.TimedLock;
import view.GUI.panels.*;
import view.interfaces.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class  GUI implements UI, ActionListener {
    private final NetworkChoicePanel networkChoise = new NetworkChoicePanel(this);
    private final HomePanel homePanel = new HomePanel(this);
    private JoinGamePanel join;
    private CreateGamePanel create;
    private GamePanel game;
    private final Container root;
    public GUI(){
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
        this.root = frame.getContentPane();
    }

    private void createNetworkChoice(){
        root.removeAll();
        root.add(networkChoise);
        refresh();
    }

    public void home(){
        root.removeAll();
        root.add(homePanel);
        refresh();
    }

    public void createGame(){
        root.removeAll();
        root.add(create);
        refresh();
    }

    public void joinGame(){
        root.removeAll();
        root.add(join);
        refresh();
    }

    public void playGame(){
        root.removeAll();
        root.add(game);
        refresh();
    }

    public void refresh(){
        root.revalidate();
        root.repaint();
    }

    public void winner(){
        WinnerGamePanel winner = new WinnerGamePanel();
        root.removeAll();
        root.add(winner, BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        game.update(o,evt);
    }

    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {
        game.update(o,evt);
    }

    @Override
    public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {
        //TODO enrico deciditi ad implementarlo
    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {
        game.update(o,evt);
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        game.update(o,evt);
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        game.update(o,evt);
    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
        game.update(o,evt);
    }
    @Override
    public void update(ShelfInfo sV, Shelf.Event evt) throws RemoteException {
        game.update(sV,evt);
    }

    @Override
    public String askRMIorSocket() {
        createNetworkChoice();
        try {
            if(!askRMISOCKET.hasBeenNotified())
                askRMISOCKET.lock(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return askRMISOCKET.getValue();
    }

    @Override
    public void showError(String error) {

    }

    private final TimedLock<String> askRMISOCKET = new TimedLock<>("");

    @Override
    public void run(ServerInterface server, ClientInterface client) {
        this.game = new GamePanel(server,client);
        this.create = new CreateGamePanel(this,server,client);
        this.join = new JoinGamePanel(this,server, client);
        home();
        //TODO trovare una soluzione migliore di sospendere questo thread così
        try {
            askRMISOCKET.reset();
            askRMISOCKET.lock(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        if(create!=null)
            create.update(o,evt);
        if(join!=null)
            join.update(o,evt);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == networkChoise) {
            switch (e.getActionCommand()) {
                case "RMI" -> askRMISOCKET.notify("r");
                case "SOCKET" -> askRMISOCKET.notify("s");
                case "EXIT" -> askRMISOCKET.notify("");
            }
        } else if (e.getSource() == homePanel) {
            switch (e.getActionCommand()) {
                case "CREATE" -> createGame();
                case "JOIN" -> joinGame();
            }
        } else if (e.getSource() == join) {
            String[] parts = e.getActionCommand().split("\n");
           switch (parts[0]){
               case "ID" -> game.setPlayerId(parts[1]);
               case "JOINED" -> playGame();
               case "EXIT" -> home();
           }
        } else if (e.getSource() == create) {
            switch (e.getActionCommand()){
                case "CREATED" -> joinGame();
                case "EXIT" -> home();
            }
        }
    }
}