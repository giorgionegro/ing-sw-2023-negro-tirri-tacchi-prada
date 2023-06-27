package view.GUI.panels;

import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * This class extends JPanel and represents a graphical component that allows to create or join a game
 */
public class HomePanel extends JPanel {
    /** Construct an {@link CreateGamePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param listener the ActionListener to be notified when a button is pressed
     */
    public HomePanel(ActionListener listener){

        initializeLayout();

        joinButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_JOIN,""))
        );
        createButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE,""))
        );
        exitButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this, ViewLogic.EXIT,""))
        );
    }
    /**This method prints out an error message
     * @param message the error message to be displayed.
     */
    public void setMessage(String message){
        if(!message.isBlank()){
            messageLabel.setVisible(true);
            messageLabel.setText(message);
        }else{
            messageLabel.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }


    /*---------------------- GRAPHICS COMPONENTS and GRAPHICS INITIALIZATION -------------------------*/
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image backgroundImage = new ImageIcon (Objects.requireNonNull(getClass().getResource("/desktop.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image messageBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/filterWinnerPanel.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image titleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/title.png"))).getImage();
    /**
     *  This is the background image of {@link #joinButton} {@link #exitButton}{@link #createButton}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    /**
     * This is the label with the written "MY SHELFIE"
     */
    private final JLabel titleLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(titleImage, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };
    /**
     * This is the button that allows the user to join a game
     */
    private final JButton joinButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to create a new game
     */
    private final JButton createButton = new JButton() {
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
    private final JLabel messageLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(messageBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

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
        constraints.weighty=1;

        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth=3;
        this.add(new Container(),constraints);

        constraints.gridy=6;
        this.add(new Container(),constraints);

        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 5;
        this.add(new Container(),constraints);

        constraints.gridx = 2;
        constraints.gridheight=1;
        this.add(new Container(),constraints);
    }

    /**
     This method initializes the contents of the panel: 3 buttons (JOIN, CREATE, EXIT) and the Title label.
     * The positions and dimensions of components within the grid are set.
     */
    private void initializeContents(){
        Font textFont = new Font("Century", Font.BOLD, 20);
        final Dimension zeroDimension = new Dimension(0,0);
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
        constraints.weighty=3;
        constraints.weightx=2;

        titleLabel.setFont(textFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(zeroDimension);
        this.add(titleLabel,constraints);

        constraints.weighty=1;
        constraints.gridy++;
        joinButton.setFont(textFont);
        joinButton.setText("JOIN GAME");
        joinButton.setHorizontalAlignment(SwingConstants.CENTER);
        joinButton.setBackground(new Color(0,0,0,0));
        joinButton.setPreferredSize(zeroDimension);
        this.add(joinButton,constraints);

        constraints.gridy++;
        createButton.setFont(textFont);
        createButton.setText("CREATE GAME");
        createButton.setHorizontalAlignment(SwingConstants.CENTER);
        createButton.setBackground(new Color(0,0,0,0));
        createButton.setPreferredSize(zeroDimension);
        this.add(createButton,constraints);

        constraints.gridy++;
        exitButton.setFont(textFont);
        exitButton.setText("EXIT");
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setBackground(new Color(0,0,0,0));
        exitButton.setPreferredSize(zeroDimension);
        this.add(exitButton,constraints);

        constraints.gridy++;
        messageLabel.setFont(textFont);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setPreferredSize(zeroDimension);
        messageLabel.setForeground(Color.LIGHT_GRAY);
        messageLabel.setVisible(false);
        this.add(messageLabel,constraints);
    }

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImage!=null){
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
