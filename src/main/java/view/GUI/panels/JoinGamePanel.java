package view.GUI.panels;

import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
/**
 * This class extends JPanel and represents a graphical component that allows to join a game inserting a GameId and a PlayerId
 */
public class JoinGamePanel extends JPanel {
    /** Construct an {@link CreateGamePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param listener the ActionListener to be notified when a button is pressed
     */
    public JoinGamePanel(ActionListener listener){

        initializeLayout();

        playButton.addActionListener(e -> {
                    String IdPlayer = playerTextField.getText();
                    String IdGame = gameTextField.getText();
                    listener.actionPerformed(new ActionEvent(this, ViewLogic.JOIN, IdPlayer + " " + IdGame));
                });
        exitButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,ViewLogic.ROUTE_HOME,"")));
    }
    /**This method prints out an error message
     * @param error the error message to be displayed.
     */
    public void setMessage(String error){
        if(!error.isBlank()){
            errorLabel.setVisible(true);
            errorLabel.setText(error);
        }else{
            errorLabel.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }


    /*---------------------- GRAPHICS COMPONENTS and GRAPHICS INITIALIZATION -------------------------*/
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/desktop.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image errorBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/filterWinnerPanel.png"))).getImage();
    /**
     *  This is the background image of {@link #playButton} {@link #exitButton}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    /**
     * This is the button that allows the user to play a game
     */
    private final JButton playButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to exit from the application
     */
    private final JButton exitButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This label shows an error if occurred
     */
    private final JLabel errorLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(errorBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the label with the written "PLAYER ID:"
     */
    private final JLabel playerLabel = new JLabel(){
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the label with the written "GAME ID:"
     */
    private final JLabel gameLabel = new JLabel(){
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };
    /**
     * This is the TextField where the user inserts the GameId
     */
    private final JTextField gameTextField = new JTextField();
    /**
     * This is the TextField where the user inserts the PlayerId
     */
    private final JTextField playerTextField = new JTextField();

    /**
     *  This method initializes the layout of the component
     */
    private void initializeLayout(){
        this.setLayout(new GridBagLayout());
        initializeBorders();
        initializeContents();
        this.revalidate();
        this.repaint();
    }


    /**
     *  This method initializes the Borders of the panel
     */
    private void initializeBorders(){

        GridBagConstraints constraints = new GridBagConstraints(
                0,0,
                1,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(10,5,10,5),
                0,0
        );
        constraints.weightx=1;
        constraints.weighty=2;

        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth=4;
        this.add(new Container(),constraints);

        constraints.gridy=5;
        this.add(new Container(),constraints);

        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 4;
        constraints.weighty = 1;
        this.add(new Container(),constraints);

        constraints.gridx = 3;
        constraints.gridheight = 1;
        this.add(new Container(),constraints);
    }


    /**
     This method initializes the contents of the panel: 2 buttons (EXIT, JOIN) and 2 labels.
     * The positions and dimensions of components within the grid are set.
     */
    private void initializeContents(){
        Font textFont = new Font("Century", Font.BOLD, 20);
        GridBagConstraints constraints = new GridBagConstraints(
                0,0,
                1,1,
                1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(10,5,10,5),
                0,0
        );
        constraints.gridy=1;
        constraints.gridx=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        constraints.weighty=1;
        constraints.weightx=1;

        playerLabel.setText("PlayerID");
        playerLabel.setFont(textFont);
        playerLabel.setPreferredSize(new Dimension(0,0));
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(playerLabel,constraints);

        constraints.gridx++;
        playerTextField.setFont(textFont);
        playerTextField.setPreferredSize(new Dimension(0,0));
        playerTextField.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(playerTextField,constraints);

        constraints.gridy++;
        gameTextField.setFont(textFont);
        gameTextField.setPreferredSize(new Dimension(0,0));
        gameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(gameTextField,constraints);

        constraints.gridx--;
        gameLabel.setText("GameID");
        gameLabel.setFont(textFont);
        gameLabel.setPreferredSize(new Dimension(0,0));
        gameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(gameLabel,constraints);

        constraints.gridy++;
        exitButton.setText("EXIT");
        exitButton.setFont(textFont);
        exitButton.setBackground(new Color(0,0,0,0));
        exitButton.setPreferredSize(new Dimension(0,0));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(exitButton,constraints);

        constraints.gridx++;
        playButton.setText("PLAY");
        playButton.setFont(textFont);
        playButton.setBackground(new Color(0,0,0,0));
        playButton.setPreferredSize(new Dimension(0,0));
        playButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(playButton,constraints);

        constraints.gridy++;
        constraints.gridx--;
        constraints.gridwidth=2;
        errorLabel.setFont(textFont);
        errorLabel.setPreferredSize(new Dimension(0,0));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        this.add(errorLabel,constraints);
    }


    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImage !=null){
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
            g.drawImage(backgroundImage, 0, 0, width, height, null);

        }
    }
}
