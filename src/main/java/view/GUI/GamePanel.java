package view.GUI;

import modelView.GameInfo;
import modelView.LivingRoomInfo;
import modelView.PlayerChatInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;



public class GamePanel extends JComponent implements ActionListener {
    Image parquet;
    private GameInfo currentGameState;
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;
    ActionListener listener;
    JButton invia;
    public GamePanel(ActionListener listener){
        this.listener = listener;
        this.setMaximumSize(new Dimension(1200,900));
        this.currentLivingRoom = currentLivingRoom;
        parquet = new ImageIcon (this.getClass().getResource("/parquet.jpg").getPath()).getImage();
        ImageIcon living = new ImageIcon(this.getClass().getResource(("/livingroom.png")).getPath());
        ImageIcon shelf = new ImageIcon(this.getClass().getResource(("/bookshelf.png")).getPath());
        ImageIcon tile = new ImageIcon(this.getClass().getResource(("/Tile/Giochi1.1.png")).getPath());
        Font font = new Font("Century", Font.BOLD, 20);
        Font font1 = new Font("Century", Font.BOLD, 12);


        this.setLayout(null);
        Insets insets = this.getInsets();


        //Game Board
        JPanel livingroomBoard = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(living.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        livingroomBoard.setMinimumSize(new Dimension(600,600));
        livingroomBoard.setLayout(null);
        livingroomBoard.setBounds(0,0,600, 600);
        this.add(livingroomBoard);

        //Shelf
        JPanel livingroomShelf1 = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        livingroomBoard.setMinimumSize(new Dimension(300,300));
        livingroomShelf1.setLayout(null);
        livingroomShelf1.setOpaque(false);
        livingroomShelf1.setBounds(603,300,300, 300);
        this.add(livingroomShelf1);


       JPanel livingroomShelf2 = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        livingroomShelf2.setOpaque(false);
        livingroomShelf2.setBounds(603,15,200, 200);
        this.add(livingroomShelf2);

        JPanel livingroomShelf3 = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        livingroomShelf3.setOpaque(false);
        livingroomShelf3.setBounds(810,15,200, 200);
        this.add(livingroomShelf3);
        JPanel livingroomShelf4 = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(shelf.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        livingroomShelf4.setOpaque(false);
        livingroomShelf4.setBounds(1020,15,200, 200);
        this.add(livingroomShelf4);


        //Buttons Shelf
        ImageIcon insertArrow = new ImageIcon(this.getClass().getResource(("/insert2.png")).getPath());

        JButton insert1 = new JButton(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        insert1.setBounds(640,260,30, 40);
        this.add(insert1);

        JButton insert2 = new JButton(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        insert2.setBounds(690,260,30, 40);
        this.add(insert2);

        JButton insert3 = new JButton(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        insert3.setBounds(740,260,30, 40);
        this.add(insert3);

        JButton insert4 = new JButton(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        insert4.setBounds(790,260,30, 40);
        this.add(insert4);

        JButton insert5 = new JButton(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(insertArrow.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        insert5.setBounds(840,260,30, 40);
        this.add(insert5);


       //CommonGoals + PersonalGoal Panel

        ImageIcon commonGoal_1 = new ImageIcon(this.getClass().getResource(("/commonGoals.GUI/1.jpg")).getPath());
        ImageIcon commonGoal_2 = new ImageIcon(this.getClass().getResource(("/commonGoals.GUI/2.jpg")).getPath());
        ImageIcon personalGoal_1 = new ImageIcon(this.getClass().getResource(("/front_EMPTY.jpg")).getPath());

        JLabel commongoals = new JLabel("Common Goals:");
        commongoals.setFont(font);
        commongoals.setBounds(170,550,226, 150);
        this.add(commongoals);

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

        JLabel personalgoallabel = new JLabel("Personal Goal:");
        personalgoallabel.setFont(font);
        personalgoallabel.setBounds(915,250,226, 150);
        this.add(personalgoallabel);

        commonGoal1.setBounds(10,650,226, 150);
        this.add(commonGoal1);

        commonGoal2.setBounds(246,650,220, 150);
        this.add(commonGoal2);

        personalGoal.setBounds(910,350,154, 200);

        this.add(personalGoal);





        //CHAT
        JPanel chatPanel = new JPanel();
        chatPanel.setBackground(Color.BLACK);
        SpringLayout layoutChat = new SpringLayout();
        chatPanel.setLayout(layoutChat);
        int margine = 5;

        JTextArea textarea = new JTextArea();
        textarea.setBackground(Color.WHITE);
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


        chatPanel.setBounds(1220,620,300, 200);
        this.add(chatPanel);


        JButton[][] tiles = new JButton[9][9];
        for (int i = 0; i <tiles.length ; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                JButton tileButton = new JButton();/*{protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(tile.getImage(), 0, 0, getWidth(), getHeight(), null);
                    }
                };
                */
                tiles[i][j] = tileButton;
                tiles[i][j].setContentAreaFilled(false);
                tiles[i][j].setBounds(27+60*i,30+60*j,60,60);
                livingroomBoard.add(tiles[i][j]);
            }
        }



        JPanel[][] matrix = new JPanel[5][6];
        int k = 7;
        int h = 14;
        for (int i = 0; i <matrix.length ; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
            JPanel imagetile = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(tile.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };

            JPanel space = new JPanel();
            space.setBackground(Color.black);

            matrix[i][j] = imagetile;

            matrix[i][j].setBounds(642 + 33 * i + i*h , 324 + 33 * j + j*k , 35, 35);
            this.add(matrix[i][j]);


            }
        }

        JPanel[][] matrix2 = new JPanel[5][6];
        double b = 2;
        double a = 6.5;
        for (int i = 0; i <matrix2.length ; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                JPanel imagetile = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(tile.getImage(), 0, 0, getWidth(), getHeight(), null);
                    }
                };

                JPanel space = new JPanel();
                space.setBackground(Color.black);

                matrix[i][j] = imagetile;

                matrix[i][j].setBounds((int) (627 + 25 * i + i*a), (int) (28 + 25 * j + j*b), 25, 25);
                this.add(matrix[i][j]);

            }
        }


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
