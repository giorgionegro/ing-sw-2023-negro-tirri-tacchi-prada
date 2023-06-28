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

        this.initializeLayout();

        this.joinButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_JOIN,""))
        );
        this.createButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE,""))
        );
        this.exitButton.addActionListener(e ->
            listener.actionPerformed(new ActionEvent(this, ViewLogic.EXIT,""))
        );
    }
    /**This method prints out an error message
     * @param message the error message to be displayed.
     */
    public void setMessage(String message){
        if(!message.isBlank()){
            this.messageLabel.setVisible(true);
            this.messageLabel.setText(message);
        }else{
            this.messageLabel.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }


    /*---------------------- GRAPHICS COMPONENTS and GRAPHICS INITIALIZATION -------------------------*/
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image backgroundImage = new ImageIcon (Objects.requireNonNull(this.getClass().getResource("/desktop.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image messageBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BackgroundFilter.png"))).getImage();
    /**
     * This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     **/
    private final Image titleImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/title.png"))).getImage();
    /**
     *  This is the background image of {@link #joinButton} {@link #exitButton}{@link #createButton}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();
    /**
     * This is the label with the written "MY SHELFIE"
     */
    private final JLabel titleLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(HomePanel.this.titleImage, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };
    /**
     * This is the button that allows the user to join a game
     */
    private final JButton joinButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(HomePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to create a new game
     */
    private final JButton createButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(HomePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to exit from the application
     */
    private final JButton exitButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(HomePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This label shows an error if occurred
     */
    private final JLabel messageLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(HomePanel.this.messageBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     *  This method initializes the layout of the component
     */
    private void initializeLayout(){
        this.setLayout(new GridBagLayout());
        this.initializeBorders();
        this.initializeContents();
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

        this.titleLabel.setFont(textFont);
        this.titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.titleLabel.setPreferredSize(zeroDimension);
        this.add(this.titleLabel,constraints);

        constraints.weighty=1;
        constraints.gridy++;
        this.joinButton.setFont(textFont);
        this.joinButton.setText("JOIN GAME");
        this.joinButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.joinButton.setBackground(new Color(0,0,0,0));
        this.joinButton.setPreferredSize(zeroDimension);
        this.add(this.joinButton,constraints);

        constraints.gridy++;
        this.createButton.setFont(textFont);
        this.createButton.setText("CREATE GAME");
        this.createButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.createButton.setBackground(new Color(0,0,0,0));
        this.createButton.setPreferredSize(zeroDimension);
        this.add(this.createButton,constraints);

        constraints.gridy++;
        this.exitButton.setFont(textFont);
        this.exitButton.setText("EXIT");
        this.exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.exitButton.setBackground(new Color(0,0,0,0));
        this.exitButton.setPreferredSize(zeroDimension);
        this.add(this.exitButton,constraints);

        constraints.gridy++;
        this.messageLabel.setFont(textFont);
        this.messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.messageLabel.setPreferredSize(zeroDimension);
        this.messageLabel.setForeground(Color.LIGHT_GRAY);
        this.messageLabel.setVisible(false);
        this.add(this.messageLabel,constraints);
    }

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.backgroundImage !=null){
            double ratio = 1.765;
            double windowRatio = (double) this.getWidth()/ this.getHeight();
            int width;
            int height;
            if(windowRatio>ratio) {
                width = this.getWidth();
                height = (int) (this.getWidth()/ratio);
            }else{
                height = this.getHeight();
                width = (int) (this.getHeight()*ratio);
            }
            g.drawImage(this.backgroundImage, 0, 0, width, height, null);

        }
    }
}
