package view.GUI.panels;

import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * This class extends JPanel and represents a graphical component that allows to create a new game inserting a GameId and a PlayerId
 */
public class CreateGamePanel extends JPanel {

    /** Construct an {@link CreateGamePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param listener the ActionListener to be notified when a button is pressed
     */
    public CreateGamePanel(ActionListener listener){

        this.initializeLayout();

        this.createButton.addActionListener(e -> {
            String gameId = this.gameIdTextField.getText();
            String k = (String) this.playerNumberCombo.getSelectedItem();
            if(k==null)
                k = "";

            listener.actionPerformed(new ActionEvent(this, ViewLogic.CREATE,"STANDARD "+gameId+" "+k));
        });
        this.exitButton.addActionListener(e ->  listener.actionPerformed(new ActionEvent(this,ViewLogic.ROUTE_HOME,"")));
    }

    /**This method prints out an error message
     * @param error the error message to be displayed.
     */
    public void setMessage(String error){
        if(!error.isBlank()){
            this.errorLabel.setVisible(true);
            this.errorLabel.setText(error);
        }else{
            this.errorLabel.setVisible(false);
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
     *  This is the background image of this component, it is used into {@link #paintComponent(Graphics)}
     *
     */
    private final Image errorBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BackgroundFilter.png"))).getImage();
    /**
     *  This is the background image of {@link #createButton} {@link #exitButton}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();
    /**
     * This is the label with the written "PLAYER ID:"
     */
    private final JLabel playerNumberLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(CreateGamePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the label with the written "GAME ID:"
     */
    private final JLabel gameIDLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(CreateGamePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };
    /**
     * This is the TextField where the user inserts the GameId
     */
    private final JTextField gameIdTextField = new JTextField();

    /**
     * This is the ComboBox where the user chooses the number of players' game
     */
    private final JComboBox<String> playerNumberCombo = new JComboBox<>(new String[]{"2","3","4"});

    /**
     * This is the button that allows the user to create the new game
     */
    private final JButton createButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(CreateGamePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to exit from the application
     */
    private final JButton exitButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(CreateGamePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This label shows an error if occurred
     */
    private final JLabel errorLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(CreateGamePanel.this.errorBackground, 0, 0, this.getWidth(), this.getHeight(), null);
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
     This method initializes the contents of the panel: 2 buttons (EXIT, CREATE), 2 labels, the comboBox and the TextField
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

        final Dimension zeroDimension = new Dimension(0,0);
        constraints.gridy=1;
        constraints.gridx=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        constraints.weighty=1;
        constraints.weightx=1;

        this.gameIDLabel.setText("GameID");
        this.gameIDLabel.setFont(textFont);
        this.gameIDLabel.setPreferredSize(zeroDimension);
        this.gameIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.gameIDLabel,constraints);

        constraints.gridx++;
        this.gameIdTextField.setFont(textFont);
        this.gameIdTextField.setPreferredSize(zeroDimension);
        this.gameIdTextField.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.gameIdTextField,constraints);

        constraints.gridy++;
        this.playerNumberCombo.setFont(textFont);
        this.playerNumberCombo.setPreferredSize(zeroDimension);
        this.add(this.playerNumberCombo,constraints);

        constraints.gridx--;

        this.playerNumberLabel.setText("Player N°");
        this.playerNumberLabel.setFont(textFont);
        this.playerNumberLabel.setPreferredSize(zeroDimension);
        this.playerNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.playerNumberLabel,constraints);

        constraints.gridy++;
        this.exitButton.setText("EXIT");
        this.exitButton.setFont(textFont);
        this.exitButton.setPreferredSize(zeroDimension);
        this.exitButton.setBackground(new Color(0,0,0,0));
        this.exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.exitButton,constraints);

        constraints.gridx++;
        this.createButton.setText("CREATE");
        this.createButton.setFont(textFont);
        this.createButton.setPreferredSize(zeroDimension);
        this.createButton.setBackground(new Color(0,0,0,0));
        this.createButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.createButton,constraints);


        constraints.gridy++;
        constraints.gridx--;
        constraints.gridwidth=2;
        this.errorLabel.setFont(textFont);
        this.errorLabel.setPreferredSize(zeroDimension);
        this.errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.errorLabel.setForeground(Color.RED);
        this.errorLabel.setVisible(false);
        this.add(this.errorLabel,constraints);
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
