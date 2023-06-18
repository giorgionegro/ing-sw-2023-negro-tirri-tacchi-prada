package view.GUI.panels;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.User;
import modelView.NewGameInfo;
import modelView.UserInfo;

import util.TimedLock;
import view.interfaces.UserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Objects;

public class CreateGamePanel extends JPanel implements ActionListener, UserView {
    private final Image CreateGame = new ImageIcon (Objects.requireNonNull(getClass().getResource("/desktop.png"))).getImage();
    private final JButton createButton;

    private final JButton exitButton;
    private final JTextField GameId;
    private final JComboBox<Integer> comboBox;
    private final ActionListener listener;
    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;

    public CreateGamePanel(ActionListener listener, ServerInterface serverInterface, ClientInterface clientInterface){
        this.listener = listener;
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;

        ImageIcon button = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png")));
        ImageIcon buttonIdG = new ImageIcon(Objects.requireNonNull(getClass().getResource("/GameID.png")));
        ImageIcon buttonIdN = new ImageIcon(Objects.requireNonNull(getClass().getResource("/nplayers.png")));
        Font font1 = new Font("Century", Font.BOLD, 24);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.insets = new Insets(20, 0, 0, 0);
        this.setBounds(0,0,962,545);
        this.setLayout(new BorderLayout());

        JPanel ButtonsCreatePanel = new JPanel ();

        ButtonsCreatePanel.setOpaque(false);
        ButtonsCreatePanel.setBackground(new Color(0,0,0));
        ButtonsCreatePanel.setLayout(new GridBagLayout());

        GameId = new JTextField();
        GameId.setPreferredSize(new Dimension(150,40));
        JLabel GameIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdG.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        GameIdField.setPreferredSize(new Dimension(165,41));


        Integer[] nPlayers = {2,3,4};
        comboBox = new JComboBox<>(nPlayers);
        comboBox.setPreferredSize(new Dimension(150,40));


        JLabel NumberIdField = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonIdN.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        NumberIdField.setPreferredSize(new Dimension(190,41));


        createButton = new JButton();
        createButton.setIcon(button);
        JLabel PlayButtonLabel = new JLabel("CREATE");
        PlayButtonLabel.setFont(font1);
        createButton.add(PlayButtonLabel);

        exitButton = new JButton();
        exitButton.setIcon(button);
        JLabel exitButtonLabel = new JLabel("BACK");
        exitButtonLabel.setFont(font1);
        exitButton.add(exitButtonLabel);

        ButtonsCreatePanel.add(GameIdField,c);
        c.gridx++;
        ButtonsCreatePanel.add(GameId,c);
        c.gridy++;
        c.gridx--;
        ButtonsCreatePanel.add(NumberIdField,c);
        c.gridx++;
        ButtonsCreatePanel.add(comboBox, c);
        c.gridy++;
        ButtonsCreatePanel.add(createButton,c);
        c.gridx--;
        ButtonsCreatePanel.add(exitButton,c);

        createButton.addActionListener(this);
        exitButton.addActionListener(this);

        this.add(ButtonsCreatePanel);
    }

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
        if (e.getSource() == createButton) {
            try {
                String gameId = GameId.getText();
                int k = (Integer) comboBox.getSelectedItem();

                long requestTime = System.currentTimeMillis();

                serverWaiter.reset();
                serverInterface.createGame(clientInterface, new NewGameInfo(gameId, "STANDARD", k, requestTime));

                if (!serverWaiter.hasBeenNotified()) {
                    serverWaiter.setValue(true);
                    try {
                        serverWaiter.lock(6000);
                    } catch (InterruptedException exception) {
                        throw new RemoteException("Connection timeout error");
                    }
                }

                if (!serverWaiter.getValue())
                    listener.actionPerformed(new ActionEvent(this,e.getID(),"CREATED"));
                else {
                    //TODO show errore
                    if(user!=null){
                        System.err.println(user.eventMessage());
                    }
                }
            }catch(RemoteException re){
                //TODO show errore generico di connessione
            }
        } else if (e.getSource()==exitButton) {
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
            case GAME_CREATED -> serverWaiter.notify(false);
            case ERROR_REPORTED -> serverWaiter.notify(true);
        }
    }
}
