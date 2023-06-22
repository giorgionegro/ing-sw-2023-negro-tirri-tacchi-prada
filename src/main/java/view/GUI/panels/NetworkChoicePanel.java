package view.GUI.panels;

import view.ViewLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NetworkChoicePanel extends JPanel implements ActionListener {
    private final ActionListener listener;

    public  NetworkChoicePanel(ActionListener listener) {
        this.listener = listener;

        initializeLayout();

        socketButton.addActionListener(this);
        RMIButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    public void setErrorMessage(String errorMessage){
        if(!errorMessage.isBlank()){
            errorLabel.setVisible(true);
            errorLabel.setText(errorMessage);
        }else{
            errorLabel.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == socketButton) {
            listener.actionPerformed(new ActionEvent(this, ViewLogic.CONNECT,ViewLogic.CONNECT_SOCKET));
        } else if (e.getSource() == RMIButton) {
            listener.actionPerformed(new ActionEvent(this,ViewLogic.CONNECT,ViewLogic.CONNECT_RMI));
        } else if (e.getSource() == exitButton) {
            listener.actionPerformed(new ActionEvent(this,ViewLogic.EXIT,""));
        }
    }

    /*---------------------- GRAPHICS COMPONENTS and GRAPHICS INITIALIZATION -------------------------*/
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
    private final JLabel titleLabel = new JLabel();

    private final JButton RMIButton = new JButton() {
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };;

    private final JButton socketButton = new JButton() {
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
    
    private final Dimension zeroDimension = new Dimension(0,0);
    private void initializeLayout(){
        this.setBackground(Color.BLACK);
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

    private void initializeContents(){
        constraints.gridy=1;
        constraints.gridx=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        constraints.weighty=1;
        constraints.weightx=2;

        titleLabel.setFont(textFont);
        titleLabel.setText("HOW DO YOU WANT TO CONNECT?");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(zeroDimension);
        this.add(titleLabel,constraints);

        constraints.gridy++;
        RMIButton.setFont(textFont);
        RMIButton.setText("RMI");
        RMIButton.setHorizontalAlignment(SwingConstants.CENTER);
        RMIButton.setBackground(new Color(0,0,0,0));
        RMIButton.setPreferredSize(zeroDimension);
        this.add(RMIButton,constraints);

        constraints.gridy++;
        socketButton.setFont(textFont);
        socketButton.setText("SOCKET");
        socketButton.setHorizontalAlignment(SwingConstants.CENTER);
        socketButton.setBackground(new Color(0,0,0,0));
        socketButton.setPreferredSize(zeroDimension);
        this.add(socketButton,constraints);

        constraints.gridy++;
        exitButton.setFont(textFont);
        exitButton.setText("EXIT");
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setBackground(new Color(0,0,0,0));
        exitButton.setPreferredSize(zeroDimension);
        this.add(exitButton,constraints);

        constraints.gridy++;
        errorLabel.setFont(textFont);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setPreferredSize(zeroDimension);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        this.add(errorLabel,constraints);
    }
}
