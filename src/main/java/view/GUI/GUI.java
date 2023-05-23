package view.GUI;

import javax.swing.*;
import java.awt.*;


public class  GUI {
    private JButton CreateButton;
    private JButton JoinButton;
    private JButton RMIButton;
    private JButton SocketButton;
    private MyFrame frame;

    private Container root;

    public static void main(String[] a){//JFrame = a GUI window to add components to
        GUI GUI = new GUI();

    }

    public GUI(){
        this.frame = new MyFrame();
        this.root = frame.getContentPane();

        createNetworkChoice();

       // createHome();
        frame.setVisible(true);
    }

    private void createNetworkChoice(){
        NetworkChoicePanel NetworkPanel = new NetworkChoicePanel();
        root.removeAll();
        root.add(NetworkPanel, BorderLayout.CENTER);
        refresh();
    }

    public void createHome(){
        HomePanel home = new HomePanel();
        root.removeAll();
        root.add(home, BorderLayout.CENTER);
        refresh();
    }

    public void createGame(){
        CreateGamePanel create = new CreateGamePanel();
        root.removeAll();
        root.add(create, BorderLayout.CENTER);
        refresh();
    }

    public void joinGame(){
        JoinGamePanel join = new JoinGamePanel();
        root.removeAll();
        root.add(join, BorderLayout.CENTER);
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

}