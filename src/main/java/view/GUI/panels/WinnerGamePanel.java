package view.GUI.panels;

import view.GUI.layouts.AspectRatioLayout;
import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class extends JPanel and represents a graphical component that shows the leaderboard at the end of the game
 */
public class WinnerGamePanel extends JPanel {

    /**
     * Construct an {@link WinnerGamePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param exitListener the ActionListener to be notified when the Exit button is pressed
     * @param points player's points
     * @param playerId id of the player
     */
    public WinnerGamePanel(ActionListener exitListener, Map<String,Integer> points, String playerId) {
        initializeLayout(points, playerId);

        exitButton.addActionListener(e ->
                exitListener.actionPerformed(new ActionEvent(this,ViewLogic.LEAVE_GAME,""))
        );
    }


    /*-------------------- GRAPHIC LAYOUT ------------------*/
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     */

    private final Image createGame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/leaderboardBackground.png"))).getImage();
    /**
     * This is the background image of components generated into {@link #createPointsContainer(String)} and {@link #createPlayerIdContainer(String, String)}
     */
    private final Image filter = new ImageIcon(Objects.requireNonNull(getClass().getResource("/filterWinnerPanel.png"))).getImage();
    /**
     * This is the background image of {@link #exitButton}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    /**
     *This is the main font used in this panel
     */
    private final Font font = new Font("Century", Font.BOLD, 20);
    private final JButton exitButton = new JButton("EXIT"){
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**This method initialize the layout of the Panel
     * @param points player's points
     * @param playerId id of the player
     */
    private void initializeLayout(Map<String,Integer> points, String playerId){
        this.setLayout(new GridBagLayout());
        ImageIcon titleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/title.png")));

        //set exitButton options and place it into a fixed-ratio container
        exitButton.setBackground(new Color(0,0,0,0));
        exitButton.setFont(font);
        Container exitContainer = new Container();
        exitContainer.setLayout(new AspectRatioLayout((float) 2));
        exitContainer.add(exitButton);

        //create a label that displays title image and place it into a fixed-ratio container
        JLabel title = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(titleImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        title.setOpaque(false);
        title.setPreferredSize(new Dimension(0,0));
        Container titleContainer = new Container();
        titleContainer.setLayout(new AspectRatioLayout((float)2000/618));
        titleContainer.add(title);

        //create leaderboard
        Container leaderboardPanel = createLeaderboardPanel(points, playerId);

        GridBagConstraints panelConstraints = new GridBagConstraints(
                0,0,
                1,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0,0,0,0),
                0,0
        );

        // topSpacer
        panelConstraints.weighty = 0.10;
        panelConstraints.gridwidth = 3;
        this.add(new Container(),panelConstraints);

        // title
        panelConstraints.gridy++;
        panelConstraints.weighty = 0.2;
        this.add(titleContainer,panelConstraints);

        // leftSpacer | leaderboard | rightSpacer
        panelConstraints.gridy++;
        panelConstraints.gridwidth = 1;
        panelConstraints.weighty = 0.4;
        panelConstraints.weightx = 0.1;
        this.add(new Container(), panelConstraints);
        panelConstraints.gridx++;
        panelConstraints.weightx = 0.3;
        this.add(leaderboardPanel, panelConstraints);
        panelConstraints.gridx++;
        panelConstraints.weightx = 0.1;
        this.add(new Container(), panelConstraints);

        // exitButton
        panelConstraints.gridx=0;
        panelConstraints.gridy++;
        panelConstraints.gridwidth=3;
        panelConstraints.weighty = 0.04;
        this.add(exitContainer, panelConstraints);

        // bottomSpacer
        panelConstraints.gridy++;
        panelConstraints.weighty = 0.1;
        this.add(new Container(), panelConstraints);
    }

    /** This method creates the leaderboard Panel when the game ends
     * @param points player's points
     * @param thisPlayerId id of the player
     * @return final leaderboard
     */
    private Container createLeaderboardPanel(Map<String, Integer> points, String thisPlayerId) {
        Image firstCup = new ImageIcon(Objects.requireNonNull(getClass().getResource("/firstCup.png"))).getImage();
        Image secondCup = new ImageIcon(Objects.requireNonNull(getClass().getResource("/secondCup.png"))).getImage();
        Image thirdCup = new ImageIcon(Objects.requireNonNull(getClass().getResource("/thirdCup.png"))).getImage();

        GridBagConstraints leaderboardConstraints = new GridBagConstraints(
                0,0,
                1,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(5,5,5,5),
                0,0
        );

        //create a table that displays the leaderboard: POSITION | PLAYER_ID | POINTS
        Container leaderboardPanel = new Container();
        leaderboardPanel.setLayout(new GridBagLayout());

        //Subdivide the points-map into groups by amount of points and generate a sorted list of groups
        Map<Integer, List<Map.Entry<String, Integer>>> grouped = points.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));
        List<Map.Entry<Integer, List<Map.Entry<String, Integer>>>> sorted = new ArrayList<>(grouped.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList());
        Collections.reverse(sorted);

        //foreach groups, foreach player into groups create and display a row into leaderboard
        int position = 1;
        for (Map.Entry<Integer, List<Map.Entry<String, Integer>>> group : sorted) {
            Image cupImage = switch (position) {
                case 1 -> firstCup;
                case 2 -> secondCup;
                case 3 -> thirdCup;
                default -> null;
            };

            for (Map.Entry<String, Integer> player : group.getValue()) {

                // create a cupContainer for this player
                Container cupContainer = createCupContainer(cupImage);
                // create a playerIdContainer for this player
                JPanel playerIdTextContainer = createPlayerIdContainer(thisPlayerId, player.getKey());
                // create a pointsContainer for this player
                JPanel playerIdPointsContainer = createPointsContainer(String.valueOf(player.getValue()));

                // add to leaderboardPanel grid the created components
                leaderboardConstraints.weightx=1;
                leaderboardConstraints.gridx=0;
                leaderboardPanel.add(cupContainer, leaderboardConstraints);
                leaderboardConstraints.gridx++;
                leaderboardConstraints.weightx=3;
                leaderboardPanel.add(playerIdTextContainer, leaderboardConstraints);
                leaderboardConstraints.gridx++;
                leaderboardPanel.add(playerIdPointsContainer, leaderboardConstraints);
                leaderboardConstraints.gridy++;

                position++;
            }
        }
        return leaderboardPanel;
    }


    /**
     * This method creates a Panel that shows all the final players' score
     * @param points player's points
     * @return Panel that contains players' points
     */
    private JPanel createPointsContainer(String points) {
        //Create a label that displays player points ...
        JLabel playerPoints = new JLabel(points,SwingConstants.CENTER);
        playerPoints.setForeground(Color.WHITE);
        playerPoints.setFont(font);
        // ... and place it into a container with colored background
        JPanel playerIdPointsContainer = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(filter, 0, 0, getWidth(), getHeight(), null);
            }
        };
        playerIdPointsContainer.setOpaque(false);
        playerIdPointsContainer.setLayout(new AspectRatioLayout(1));
        playerIdPointsContainer.add(playerPoints);
        return playerIdPointsContainer;
    }

    /**
     * This method creates a Panel where to place the names of the players in order of score
     * @param thisPlayerId id of the player that is using the ui
     * @param playerId id of generic player
     * @return Panel that contains players' IDs
     */
    private JPanel createPlayerIdContainer(String thisPlayerId, String playerId) {
        //Create a label that displays the playerId ....
        JLabel playerIdText = new JLabel(playerId.equals(thisPlayerId) ? "YOU" : playerId, SwingConstants.CENTER);
        playerIdText.setFont(font);
        playerIdText.setForeground(Color.WHITE);
        // ....  and place it into a container with colored background
        JPanel playerIdTextContainer = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(filter, 0, 0, getWidth(), getHeight(), null);
            }
        };
        playerIdTextContainer.setOpaque(false);
        playerIdTextContainer.setLayout(new AspectRatioLayout(2));
        playerIdTextContainer.add(playerIdText);
        return playerIdTextContainer;
    }

    /**
     * This method creates a Panel where to place the images of the cups in order of score
     * @param cupImage image of a cup
     * @return Container that contains the cup's image
     */
    private Container createCupContainer(Image cupImage) {
        //Create a Label that displays the cup image if needed ...
        JLabel cup = new JLabel();
        if(cupImage !=null) {
            cup = new JLabel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(cupImage, 0, 0, getWidth(), getHeight(), null);
                }
            };
        }
        cup.setOpaque(false);
        cup.setPreferredSize(new Dimension(0, 0));
        // ... and place it into a fixed-ratio container
        Container cupContainer = new Container();
        cupContainer.setLayout(new AspectRatioLayout(1));
        cupContainer.add(cup);
        return cupContainer;
    }

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
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


