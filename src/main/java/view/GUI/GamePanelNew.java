package view.GUI;

import modelView.GameInfo;
import modelView.LivingRoomInfo;
import modelView.PlayerChatInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanelNew  extends JPanel implements ActionListener {
        Image parquet;
        private GameInfo currentGameState;
        private LivingRoomInfo currentLivingRoom;
        private PlayerChatInfo currentPlayerChat;
        JButton invia;
        ActionListener listener;
        public GamePanelNew(ActionListener listener){
            this.listener = listener;
            this.setMaximumSize(new Dimension(1200,900));
            this.currentLivingRoom = currentLivingRoom;
            parquet = new ImageIcon (this.getClass().getResource("/parquet.jpg").getPath()).getImage();
            ImageIcon living = new ImageIcon(this.getClass().getResource(("/livingroom.png")).getPath());
            ImageIcon shelf = new ImageIcon(this.getClass().getResource(("/bookshelf.png")).getPath());

            //Panel principale
            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.FIRST_LINE_START;

            JPanel up = new JPanel(new GridLayout(1,2));
            up.setOpaque(false);
            c.ipadx= 600;
            c.ipady = 600;
            c.weightx = 0.6;
            c.weighty = 1;

            //Game Board
            JLabel livingroomBoard = new JLabel(){
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(living.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            livingroomBoard.setMinimumSize(new Dimension(600,600));


            up.add(livingroomBoard,0,0);

            //Panel con le shelf

            JPanel shelvesBoard = new JPanel(new GridLayout(2,2));
            shelvesBoard.setOpaque(false);
            shelvesBoard.setMinimumSize(new Dimension(600,600));


            //Creazione 4 Shelf
            JPanel livingroomShelf1 = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            livingroomShelf1.setOpaque(false);

            JPanel livingroomShelf2 = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            livingroomShelf2.setOpaque(false);

            JPanel livingroomShelf3 = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            livingroomShelf3.setOpaque(false);
            JPanel livingroomShelf4 = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            livingroomShelf4.setOpaque(false);

            shelvesBoard.add(livingroomShelf1,0,0);
            shelvesBoard.add(livingroomShelf2,0,1);
            shelvesBoard.add(livingroomShelf3,1,0);
            shelvesBoard.add(livingroomShelf4,1,1);


            up.add(shelvesBoard,0,1);

            //CommonGoals + PersonalGoal Panel

            JPanel under = new JPanel(new GridLayout(1,2));
            under.setOpaque(false);
            c.ipadx= 600;
            c.ipady = 300;
            c.weightx = 0.6;
            c.weighty = 1;

            JPanel goals = new JPanel(new GridLayout(2,2));
            livingroomBoard.setMinimumSize(new Dimension(600,200));
            ImageIcon commonGoal_1 = new ImageIcon(this.getClass().getResource(("/commonGoals.GUI/1.jpg")).getPath());
            ImageIcon commonGoal_2 = new ImageIcon(this.getClass().getResource(("/commonGoals.GUI/2.jpg")).getPath());
            ImageIcon personalGoal_1 = new ImageIcon(this.getClass().getResource(("/front_EMPTY.jpg")).getPath());
            JLabel commonGoal1 = new JLabel(){
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(commonGoal_1.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };

            JLabel commonGoal2 = new JLabel(){
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(commonGoal_2.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };

            JLabel personalGoal = new JLabel(){
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(personalGoal_1.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };

            goals.add(commonGoal1,0,0);
            goals.add(commonGoal2,1,0);
            goals.add(personalGoal,0,1);


            under.add(goals);

            //CHAT
            JPanel chatPanel = new JPanel();
            SpringLayout layoutChat = new SpringLayout();
            chatPanel.setLayout(layoutChat);
            int margine = 5;

            JTextArea textarea = new JTextArea();
            textarea.setEditable(false);
            JScrollPane scrolltextarea = new JScrollPane(textarea);
            JTextField mex = new JTextField();
            invia = new JButton("Send");

            invia.addActionListener(this);

            chatPanel.add(scrolltextarea);
            chatPanel.add(mex);
            chatPanel.add(invia);

            layoutChat.putConstraint(BorderLayout.WEST,scrolltextarea, margine,SpringLayout.WEST, chatPanel);
            layoutChat.putConstraint(BorderLayout.NORTH,scrolltextarea, margine,SpringLayout.NORTH, chatPanel);
            layoutChat.putConstraint(BorderLayout.EAST,scrolltextarea, -margine,SpringLayout.EAST, chatPanel);
            layoutChat.putConstraint(BorderLayout.SOUTH,scrolltextarea, -margine,SpringLayout.NORTH, mex);

            layoutChat.putConstraint(BorderLayout.WEST,mex, margine,SpringLayout.WEST, chatPanel);
            layoutChat.putConstraint(BorderLayout.EAST,mex, -margine,SpringLayout.WEST, invia);
            layoutChat.putConstraint(BorderLayout.SOUTH,mex, -margine,SpringLayout.SOUTH, chatPanel);

            layoutChat.putConstraint(BorderLayout.NORTH,invia, margine,SpringLayout.SOUTH, scrolltextarea);
            layoutChat.putConstraint(BorderLayout.EAST,invia, -margine,SpringLayout.EAST, chatPanel);
            layoutChat.putConstraint(BorderLayout.SOUTH,invia, -margine,SpringLayout.SOUTH, chatPanel);

            under.add(chatPanel, 0,1 );


            this.add(up,c);
            c.gridx++;
            this.add(under,c);



/*
        try{
            Tile[][] board = new Tile[4][5];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    //TODO
                }
            }


            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (ReturnImageTile(board[i][j]).equals("empty")) {
                        JLabel EmptyTile = new JLabel();
                        EmptyTile.setOpaque(true);
                        this.add(EmptyTile);
                    } else {
                        JButton tileButton = new JButton();
                        ImageIcon tileIcon = new ImageIcon(this.getClass().getResource((ReturnImageTile(board[i][j]))).getPath());
                        tileButton.setIcon(tileIcon);
                        livingroomBoard.add(tileButton);
                        tileButton.addActionListener(this);
                    }
                }
            }
        }catch(NullPointerException e){
            currentLivingRoom = null;
        }

    }

     public String ReturnImageTile(Tile tile) {
        return "/Tile/" + switch(tile){
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

*/
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
            //TODO

        }

    }


