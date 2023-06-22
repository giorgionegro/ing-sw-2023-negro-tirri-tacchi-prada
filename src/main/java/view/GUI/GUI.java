package view.GUI;

import view.GUI.panels.*;
import view.graphicInterfaces.AppGraphics;
import view.graphicInterfaces.GameGraphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class  GUI implements AppGraphics, ActionListener {
    private final NetworkChoicePanel networkChoise = new NetworkChoicePanel(this);
    private final HomePanel homePanel = new HomePanel(this);
    private final JoinGamePanel join = new JoinGamePanel(this);
    private final CreateGamePanel create = new CreateGamePanel(this);
    private final GamePanel game = new GamePanel(this);
    private final Container root;

    private ActionListener viewLogic;
    public GUI(){
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
        this.root = frame.getContentPane();
    }

    public void refresh() {
        root.revalidate();
        root.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==game)
            viewLogic.actionPerformed(e);
        else
            viewLogic.actionPerformed(new ActionEvent(this,e.getID(),e.getActionCommand()));
//        if (e.getSource() == networkChoise) {
//            switch (e.getActionCommand()) {
//                case "RMI" -> askRMISOCKET.notify("r");
//                case "SOCKET" -> askRMISOCKET.notify("s");
//                case "EXIT" -> askRMISOCKET.notify("");
//            }
//        } else if (e.getSource() == homePanel) {
//            switch (e.getActionCommand()) {
//                case "CREATE" -> createGame();
//                case "JOIN" -> joinGame();
//            }
//        } else if (e.getSource() == join) {
//            String[] parts = e.getActionCommand().split("\n");
//            switch (parts[0]){
//               case "ID" -> game.setPlayerId(parts[1]);
//               case "JOINED" -> playGame();
//               case "EXIT" -> home();
//            }
//        } else if (e.getSource() == create) {
//            switch (e.getActionCommand()){
//                case "CREATED" -> joinGame();
//                case "EXIT" -> home();
//            }
//        } else if (e.getSource() == game) {
//            // Redirect action
//            viewLogic.actionPerformed(e);
//        }
    }

    @Override
    public void setActionListener(ActionListener actionListener) {
        this.viewLogic = actionListener;
    }

    @Override
    public GameGraphics getGameGraphics() {
        return game;
    }

    @Override
    public void showConnection(String error) {
        networkChoise.setErrorMessage(error);
        root.removeAll();
        root.add(networkChoise);
        refresh();
    }

    @Override
    public void showServerInteraction(String message) {
        homePanel.setMessage(message);
        root.removeAll();
        root.add(homePanel);
        refresh();
    }

    @Override
    public void showJoin(String error) {
        join.setMessage(error);
        root.removeAll();
        root.add(join);
        refresh();
    }

    @Override
    public void showCreate(String error) {
        create.setMessage(error);
        root.removeAll();
        root.add(create);
        refresh();
    }

    @Override
    public void showGame(String message) {
        root.removeAll();
        root.add(game);
        refresh();
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}