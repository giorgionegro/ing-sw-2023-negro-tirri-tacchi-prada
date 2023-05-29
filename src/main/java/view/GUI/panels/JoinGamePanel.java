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

public class JoinGamePanel extends JPanel implements ActionListener, UserView {
    Image CreateGame;
    private final ActionListener listener;
    JButton PlayButton;
    JTextField PlayerId;
    JTextField GameId;
    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    public JoinGamePanel(ActionListener listener, ServerInterface serverInterface, ClientInterface clientInterface){
        this.listener = listener;
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;
        CreateGame = new ImageIcon(getClass().getResource("/desktop.png")).getImage();
        ImageIcon button = new ImageIcon(getClass().getResource("/img.png"));
        ImageIcon buttonIdG = new ImageIcon(getClass().getResource("/GameID.png"));
        ImageIcon buttonIdP = new ImageIcon(getClass().getResource("/PlayerID.png"));
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



        PlayButton = new JButton();
        PlayButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("PLAY");
        PlayButtonLabel.setFont(font1);
        PlayButton.add(PlayButtonLabel);

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

        PlayButton.addActionListener(this);
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

        TimedLock<Boolean> serverWaiter = new TimedLock<>(false);
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
