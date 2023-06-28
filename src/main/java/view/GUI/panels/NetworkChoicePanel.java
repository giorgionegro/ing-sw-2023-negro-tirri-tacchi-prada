package view.GUI.panels;

import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


/**
 * This class extends JPanel and represents a graphical component that allows to choose the type of connection to use
 */
public class NetworkChoicePanel extends JPanel {

    /**
     * Construct an {@link NetworkChoicePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param listener the ActionListener to be notified when a button on this panel is pressed
     */

    public NetworkChoicePanel(ActionListener listener) {
        this.initializeLayout();
        this.socketButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this, ViewLogic.CONNECT,ViewLogic.CONNECT_SOCKET)));
        this.RMIButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,ViewLogic.CONNECT,ViewLogic.CONNECT_RMI)));
        this.exitButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,ViewLogic.EXIT,"")));
    }

    /**This method prints out an error message
     * @param errorMessage the error message to be displayed.
     */
    public void setErrorMessage(String errorMessage){
        if(!errorMessage.isBlank()){
            this.errorLabel.setVisible(true);
            this.errorLabel.setText(errorMessage);
        }else{
            this.errorLabel.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }

    /*---------------------- GRAPHICS COMPONENTS and GRAPHICS INITIALIZATION -------------------------*/
    /**
     * This is the background image of {@link #errorLabel}
     */
    private final Image errorBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BackgroundFilter.png"))).getImage();
    /**
     * This is the background image of {@link #RMIButton} {@link #socketButton} {@link #exitButton}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();

    /**
     * This is the button that allows the user to choose RMI connection
     */
    private final JButton RMIButton = new JButton("RMI") {
        protected void paintComponent(Graphics g) {
            g.drawImage(NetworkChoicePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to choose Socket connection
     */
    private final JButton socketButton = new JButton("SOCKET") {
        protected void paintComponent(Graphics g) {
            g.drawImage(NetworkChoicePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This is the button that allows the user to exit from the application
     */
    private final JButton exitButton = new JButton("EXIT") {
        protected void paintComponent(Graphics g) {
            g.drawImage(NetworkChoicePanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This label shows an error if occurred
     */
    private final JLabel errorLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(NetworkChoicePanel.this.errorBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * This method initializes the layout of the component
     */
    private void initializeLayout(){
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());
        this.initializeBorders();
        this.initializeContents();
        this.revalidate();
        this.repaint();
    }

    /**
     *This method initializes the Borders of the Panel
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
        constraints.gridwidth=3;
        this.add(new Container(),constraints);

        constraints.gridy=6;
        this.add(new Container(),constraints);

        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 5;
        constraints.weighty = 1;
        this.add(new Container(),constraints);

        constraints.gridx = 2;
        constraints.gridheight=1;
        this.add(new Container(),constraints);
    }

    /**
     * This method initializes the contents of the panel: 3 buttons (RMI, SOCKET, EXIT) and the text label
     * The positions and dimensions of components within the grid are set.
     */
    private void initializeContents(){
        Font textFont = new Font("Century", Font.BOLD, 20);

        Dimension zeroDimension = new Dimension(0,0);

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
        constraints.weightx=2;

        JLabel titleLabel = new JLabel("HOW DO YOU WANT TO CONNECT?");
        titleLabel.setFont(textFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(zeroDimension);
        this.add(titleLabel,constraints);

        constraints.gridy++;
        this.RMIButton.setFont(textFont);
        this.RMIButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.RMIButton.setBackground(new Color(0,0,0,0));
        this.RMIButton.setPreferredSize(zeroDimension);
        this.add(this.RMIButton,constraints);

        constraints.gridy++;
        this.socketButton.setFont(textFont);
        this.socketButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.socketButton.setBackground(new Color(0,0,0,0));
        this.socketButton.setPreferredSize(zeroDimension);
        this.add(this.socketButton,constraints);

        constraints.gridy++;
        this.exitButton.setFont(textFont);
        this.exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.exitButton.setBackground(new Color(0,0,0,0));
        this.exitButton.setPreferredSize(zeroDimension);
        this.add(this.exitButton,constraints);

        constraints.gridy++;
        this.errorLabel.setFont(textFont);
        this.errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.errorLabel.setPreferredSize(zeroDimension);
        this.errorLabel.setForeground(Color.RED);
        this.errorLabel.setVisible(false);
        this.add(this.errorLabel,constraints);
    }
}
