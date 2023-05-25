package view.GUI;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import model.abstractModel.*;
import modelView.*;
import util.TimedLock;
import view.interfaces.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class  GUI implements UI, ActionListener {

    public enum Actions{
        RMI,
        SOCKET,
    }
    private LivingRoomInfo currentLivingRoom;
    private JButton CreateButton;
    private JButton JoinButton;
    private JButton RMIButton;
    private JButton SocketButton;
    private MyFrame frame;
    private NetworkChoicePanel networkChoise = new NetworkChoicePanel(this);
    private HomePanel home = new HomePanel(this);
    private JoinGamePanel join = new JoinGamePanel(this);
    private CreateGamePanel create = new CreateGamePanel(this);
    private GamePanel game = new GamePanel(this);


    private Container root;
    public GUI(){
        this.frame = new MyFrame();
        frame.setVisible(true);

        this.root = frame.getContentPane();


    }

    private void createNetworkChoice(){
        //NetworkChoicePanel NetworkPanel = new NetworkChoicePanel(this);
        root.removeAll();
        root.add(networkChoise, BorderLayout.CENTER);
        refresh();
    }

    public void CreateHomePanel(){
        //HomePanel home = new HomePanel(this);
        root.removeAll();
        root.add(home, BorderLayout.CENTER);
        refresh();
    }

    public void createGame(){
       // CreateGamePanel create = new CreateGamePanel();
        root.removeAll();
        root.add(create, BorderLayout.CENTER);
        refresh();
    }

    public void joinGame(){
        //JoinGamePanel join = new JoinGamePanel();
        root.removeAll();
        root.add(join, BorderLayout.CENTER);
        refresh();
    }

    public void Game(){
        //GamePanel game = new GamePanel();
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

    }

    @Override
    public void update(GameInfo o, Game.Event evt) throws RemoteException {

    }

    @Override
    public void update(GamesManagerInfo o, GamesManager.Event evt) throws RemoteException {

    }

    @Override
    public void update(LivingRoomInfo o, LivingRoom.Event evt) throws RemoteException {


    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {

    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {

    }

    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {

    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {

    }

    @Override
    public String askRMIorSocket() {
        createNetworkChoice();
        try {
            askRMISOCKET.lock(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return askRMISOCKET.getValue();
    }

    @Override
    public void showError(String error) {

    }

    private TimedLock<String> askRMISOCKET = new TimedLock<>("");
    @Override
    public void run(ServerInterface server, ClientInterface client) {
        CreateHomePanel();
        try {
            askRMISOCKET.reset();
            askRMISOCKET.lock(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==networkChoise){
            switch (e.getActionCommand()){
                case "RMI" -> askRMISOCKET.notify("r");
                case "SOCKET" -> askRMISOCKET.notify("s");
                case "EXIT" -> askRMISOCKET.notify("");
            }
        } else if (e.getSource()==home) {
            switch (e.getActionCommand()){
                case "CREATE" -> createGame();
                case "JOIN" -> joinGame();
            }
        }else if (e.getSource()==join){
            switch(e.getActionCommand()) {
                case "PLAY" -> Game();
            }
        }else if(e.getSource()==create){
            switch(e.getActionCommand()) {
                case "PLAY" -> Game();
            }
        }
    }
}