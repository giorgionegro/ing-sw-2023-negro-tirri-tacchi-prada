package view.GUI.panels;

import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class JoinGamePanel extends JPanel implements ActionListener {
    private final ActionListener listener;
    public JoinGamePanel(ActionListener listener){
        this.listener = listener;

        initializeLayout();

        playButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            String IdPlayer = playerTextField.getText();
            String IdGame = gameTextField.getText();
            listener.actionPerformed(new ActionEvent(this, ViewLogic.JOIN,IdPlayer+" "+IdGame));
        } else if (e.getSource() == exitButton) {
            listener.actionPerformed(new ActionEvent(this,ViewLogic.ROUTE_HOME,""));
        }
    }

    /*---------------------- GRAPHICS COMPONENTS and GRAPHICS INITIALIZATION -------------------------*/
    private final Image backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/desktop.png"))).getImage();
    private final Image errorBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/filterWinnerPanel.png"))).getImage();
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    private final Font textFont = new Font("Century", Font.BOLD, 24);
    private final GridBagConstraints constraints = new GridBagConstraints(
            0,0,
            1,1,
            1,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(10,5,10,5),
            0,0
    );
    private final JButton playButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };
    private final JButton exitButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };
    private final JLabel errorLabel = new JLabel() {
        protected void paintComponent(Graphics g) {
            g.drawImage(errorBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    private final JLabel playerLabel = new JLabel(){
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };

    private final JLabel gameLabel = new JLabel(){
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };
    private final JTextField gameTextField = new JTextField();

    private final JTextField playerTextField = new JTextField();

    private void initializeLayout(){
        this.setLayout(new GridBagLayout());
        initializeBorders();
        initializeContents();
        this.revalidate();
        this.repaint();
    }

    private void initializeBorders(){
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

    private void initializeContents(){
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
