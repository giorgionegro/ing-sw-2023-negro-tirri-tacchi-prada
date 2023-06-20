package view.GUI.panels;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import modelView.LoginInfo;
import modelView.UserInfo;

import util.TimedLock;
import view.interfaces.UserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Objects;

public class JoinGamePanel extends JPanel implements ActionListener, UserView {
    private final Image CreateGame = new ImageIcon(getClass().getResource("/desktop.png")).getImage();
    private final ImageIcon filter = new ImageIcon(getClass().getResource("/filterError.png"));
    private final ActionListener listener;
    private final JButton PlayButton;
    private final JButton exitButton;
    private final JTextField PlayerId;
    private final JTextField GameId;
    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    private final TimedLock<Boolean> serverWaiter = new TimedLock<>(false);
    public JoinGamePanel(ActionListener listener, ServerInterface serverInterface, ClientInterface clientInterface){
        this.listener = listener;
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;

        ImageIcon button = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png")));
        ImageIcon buttonIdG = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GameID.png")));
        ImageIcon buttonIdP = new ImageIcon(Objects.requireNonNull(getClass().getResource("/PlayerID.png")));
        Font font1 = new Font("Century", Font.BOLD, 24);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 10, 0, 10);

        this.setBounds(0, 0, 962, 545);
        this.setLayout(new BorderLayout());

        JPanel ButtonsJoinPanel = new JPanel();

        ButtonsJoinPanel.setOpaque(false);
        ButtonsJoinPanel.setBackground(new Color(0, 0, 0));
        ButtonsJoinPanel.setLayout(new GridBagLayout());


        PlayerId = new JTextField();
        PlayerId.setPreferredSize(new Dimension(150,40));
        JLabel PlayerIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdP.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        PlayerIdField.setPreferredSize(new Dimension(165,41));

        GameId = new JTextField();
        GameId.setPreferredSize(new Dimension(150,40));
        JLabel GameIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdG.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        GameIdField.setPreferredSize(new Dimension(165,41));

        JPanel ErrorPanel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(filter.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        ErrorPanel.setPreferredSize(new Dimension(224,60));
        ErrorPanel.setOpaque(false);


        PlayButton = new JButton();
        PlayButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("PLAY");
        PlayButtonLabel.setFont(font1);
        PlayButton.add(PlayButtonLabel);

        exitButton = new JButton();
        exitButton.setIcon(button);
        JLabel exitButtonLabel = new JLabel("BACK");
        exitButtonLabel.setFont(font1);
        exitButton.add(exitButtonLabel);

        ButtonsJoinPanel.add(PlayerIdField,c);
        c.gridx++;
        ButtonsJoinPanel.add(PlayerId,c);
        c.gridx--;
        c.gridy++;
        ButtonsJoinPanel.add(GameIdField,c);
        c.gridx++;
        ButtonsJoinPanel.add(GameId,c);
        c.gridy++;
        ButtonsJoinPanel.add(PlayButton,c);
        c.gridx--;
        ButtonsJoinPanel.add(exitButton,c);
        c.gridy++;
        c.gridwidth = 2;
        ButtonsJoinPanel.add(ErrorPanel, c);

        PlayButton.addActionListener(this);
        exitButton.addActionListener(this);

        this.add(ButtonsJoinPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(CreateGame!=null){
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
            g.drawImage(CreateGame, 0, 0, width, height, null);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == PlayButton) {
            try {
                String IdPlayer = PlayerId.getText();
                String IdGame = GameId.getText();

                serverWaiter.reset();
                listener.actionPerformed(new ActionEvent(this,e.getID(),"ID\n"+IdPlayer));
                serverInterface.joinGame(clientInterface, new LoginInfo(IdPlayer,IdGame, System.currentTimeMillis()));

                if (!serverWaiter.hasBeenNotified()) {
                    try {
                        serverWaiter.setValue(true);
                        serverWaiter.lock(6000);
                    } catch (InterruptedException err) {
                        throw new RemoteException("Login timeout error");
                    }
                }

                if (!serverWaiter.getValue()) {
                    listener.actionPerformed(new ActionEvent(this, e.getID(), "JOINED"));
                }else{
                    //TODO show error
                }
            }catch(RemoteException re){
                //TODO errore generico di connessione
            }
        } else if (e.getSource() == exitButton) {
            listener.actionPerformed(new ActionEvent(this,e.getID(),"EXIT"));
        }
    }

    private UserInfo user;
    @Override
    public void update(UserInfo o, User.Event evt) throws RemoteException {
        user = o;

        if (evt == null) {
            serverWaiter.notify(false);
            return;
        }

        switch (evt){
            case GAME_JOINED -> serverWaiter.notify(false);
            case ERROR_REPORTED -> serverWaiter.notify(true);
        }
    }
}
