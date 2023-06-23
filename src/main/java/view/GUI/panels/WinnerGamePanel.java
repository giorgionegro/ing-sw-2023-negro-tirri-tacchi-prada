package view.GUI.panels;

import view.GUI.AspectRatioLayout;
import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class WinnerGamePanel extends JPanel {

    public WinnerGamePanel(ActionListener exitListener, Map<String,Integer> points, String playerId) {
        initializeLayout(points, playerId);

        exitButton.addActionListener(e ->
                exitListener.actionPerformed(new ActionEvent(this,ViewLogic.LEAVE_GAME,""))
        );
    }

    /*-------------------- GRAPHIC LAYOUT ------------------*/
    private final Image createGame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/leaderboardBackground.png"))).getImage();
    private final Image firstCup = new ImageIcon(Objects.requireNonNull(getClass().getResource("/firstCup.png"))).getImage();
    private final Image secondCup = new ImageIcon(Objects.requireNonNull(getClass().getResource("/secondCup.png"))).getImage();
    private final Image thirdCup = new ImageIcon(Objects.requireNonNull(getClass().getResource("/thirdCup.png"))).getImage();
    private final Image filter = new ImageIcon(Objects.requireNonNull(getClass().getResource("/filterWinnerPanel.png"))).getImage();
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    private final JButton exitButton = new JButton(){
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
        }
    };

    private final GridBagConstraints topSpacerConstraints = new GridBagConstraints(
            0,0,
            1,1,
            1,0.10,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints titleConstraints = new GridBagConstraints(
            0,1,
            3,1,
            1,0.20,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints exitButtonConstraints = new GridBagConstraints(
            0,3,
            3,1,
            1,0.04,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints leaderboardConstraints = new GridBagConstraints(
            -1,2,
            1,1,
            3,0.40,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );
    private final GridBagConstraints bottomSpacerConstraints = new GridBagConstraints(
            0,4,
            1,1,
            1,0.10,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final GridBagConstraints lateralSpacerConstraints = new GridBagConstraints(
            -1,2,
            1,1,
            1,0.10,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private void initializeLayout(Map<String,Integer> points, String playerId){
        Font font = new Font("Century", Font.BOLD, 20);

        JLabel exitLabel = new JLabel("EXIT",SwingConstants.CENTER);
        exitLabel.setFont(font);
        exitButton.setLayout(new BorderLayout());
        exitButton.add(exitLabel);


        //Title
        ImageIcon titleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/title.png")));
        JLabel title = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(titleImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        title.setOpaque(false);
        title.setPreferredSize(new Dimension(0,0));


        GridBagConstraints c = new GridBagConstraints();
        c.gridx = -1;
        c.gridy = 0;
        c.weightx=1;
        c.weighty=1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 10, 40, 0);

        this.setBounds(0, 0, 962, 545);
        this.setLayout(new BorderLayout());

        Container classificaWinnerPanel = new Container();
        classificaWinnerPanel.setLayout(new GridBagLayout());

        this.setLayout(new GridBagLayout());
        Container titleContainer = new Container();
        titleContainer.setLayout(new AspectRatioLayout((float)2000/618));
        titleContainer.add(title);
        this.add(titleContainer,titleConstraints);

        this.add(new Container(),lateralSpacerConstraints);
        this.add(classificaWinnerPanel,leaderboardConstraints);
        this.add(new Container(), lateralSpacerConstraints);

        Container exitContainer = new Container();
        exitContainer.setLayout(new AspectRatioLayout((float) 2));
        exitContainer.add(exitButton);
        this.add(exitContainer,exitButtonConstraints);

        this.add(new Container(),topSpacerConstraints);
        this.add(new Container(),bottomSpacerConstraints);


        Map<Integer, List<Map.Entry<String, Integer>>> grouped = points.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));
        List<Map.Entry<Integer, List<Map.Entry<String, Integer>>>> sorted = new ArrayList<>(grouped.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList());
        Collections.reverse(sorted);
        int position = 1;
        for (Map.Entry<Integer, List<Map.Entry<String, Integer>>> group : sorted) {
            Image cupImage = switch (position) {
                case 1 -> firstCup;
                case 2 -> secondCup;
                case 3 -> thirdCup;
                default -> null;
            };

            for (Map.Entry<String, Integer> player : group.getValue()) {
                JLabel cup;

                if(cupImage!=null) {
                    cup = new JLabel() {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.drawImage(cupImage, 0, 0, getWidth(), getHeight(), null);
                        }
                    };
                }else {
                    cup = new JLabel();
                }

                cup.setOpaque(false);
                cup.setPreferredSize(new Dimension(0, 0));


                JPanel playerIdTextContainer = new JPanel(){
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(filter, 0, 0, getWidth(), getHeight(), null);
                    }
                };

                playerIdTextContainer.setOpaque(false);
                playerIdTextContainer.setLayout(new AspectRatioLayout(2));

                String idText = player.getKey();
                if(idText.equals(playerId))
                    idText = "YOU";
                JLabel playerIdText = new JLabel(idText,SwingConstants.CENTER);

                playerIdText.setFont(font);
                playerIdText.setForeground(Color.WHITE);

                playerIdTextContainer.add(playerIdText);


                JPanel playerIdPointsContainer = new JPanel(){
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(filter, 0, 0, getWidth(), getHeight(), null);
                    }
                };

                playerIdPointsContainer.setOpaque(false);
                playerIdPointsContainer.setLayout(new AspectRatioLayout(1));
                JLabel playerPoints = new JLabel(String.valueOf(player.getValue()),SwingConstants.CENTER);
                playerPoints.setForeground(Color.WHITE);
                playerPoints.setFont(font);

                playerIdPointsContainer.add(playerPoints);

                Container cupContainer = new Container();
                cupContainer.setLayout(new AspectRatioLayout(1));
                cupContainer.add(cup);

                c.weightx=1;
                classificaWinnerPanel.add(cupContainer,c);
                c.weightx=3;
                classificaWinnerPanel.add(playerIdTextContainer,c);
                classificaWinnerPanel.add(playerIdPointsContainer,c);
                c.gridy++;

                position++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(createGame!=null){
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
            g.drawImage(createGame, 0, 0, width, height, null);
        }
    }
}


