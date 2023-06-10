package view.GUI.panels;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.StandardMessage;
import model.abstractModel.Message;
import model.abstractModel.PlayerChat;
import model.abstractModel.Shelf;
import modelView.PlayerChatInfo;
import modelView.ShelfInfo;
import view.interfaces.PlayerChatView;
import view.interfaces.ShelfView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChatPanel extends JPanel implements ActionListener, PlayerChatView, ShelfView {

    Map<String,String> subjects = new HashMap<>(){{
        put("Everyone","");
    }};

    JComboBox<String> subjectsCombo = new JComboBox<>(new String[]{"Everyone"});
    JButton invia = new JButton("Send");
    JTextField mex = new JTextField();
    private String playerId;
    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    private final JScrollPane scrolltextarea = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    private final List<Message> messages = new ArrayList<>();

    GridBagConstraints comboConstraints = new GridBagConstraints(
            0,1,
            1,1,
            18,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    GridBagConstraints textareaConstraints = new GridBagConstraints(
            0,0,
            2,1,
            1,12,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    GridBagConstraints buttonConstraints = new GridBagConstraints(
            1,1,
            1,2,
            1,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    GridBagConstraints textfieldConstraints = new GridBagConstraints(
            0,2,
            1,1,
            18,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    public ChatPanel(ServerInterface serverInterface, ClientInterface clientInterface){
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;

        this.setLayout(new GridBagLayout());
        scrolltextarea.setPreferredSize(new Dimension(0,0));

        invia.addActionListener(this);
        this.add(scrolltextarea,textareaConstraints);
        this.add(invia,buttonConstraints);
        this.add(mex, textfieldConstraints);
        this.add(subjectsCombo,comboConstraints);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == invia){
            String message =  mex.getText();
            String subject = subjects.get((String) subjectsCombo.getSelectedItem());
            try {
                serverInterface.sendMessage(clientInterface,new StandardMessage(playerId, subject,message));
            } catch (RemoteException ex) {
                //TODO mostrare errore di invio
            }
        }
    }

    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }

    private void updateText(){
        JTextArea textarea = new JTextArea();

        textarea.setBackground(new Color(255,255,200));
        textarea.setEditable(false);

        textarea.setLineWrap(true);
        for(Message m : messages){
            String subject = m.getReceiver();
            if(subject.equals(playerId))
                subject = "YOU";
            if(subject.equals(""))
                subject = "Everyone";

            textarea.append(m.getSender()+" to "+subject+": "+m.getText()+"\n");
        }

        scrolltextarea.setViewportView(textarea);

        //Scrolls to the end
        scrolltextarea.revalidate();
        JScrollBar verticalScrollBar = scrolltextarea.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        scrolltextarea.revalidate();
        scrolltextarea.repaint();
    }

    @Override
    public void update(PlayerChatInfo o, PlayerChat.Event evt) throws RemoteException {
        messages.clear();
        messages.addAll(o.messages());
        updateText();
    }

    @Override
    public void update(ShelfInfo o, Shelf.Event evt) throws RemoteException {
        if(!subjects.containsKey(o.playerId()) && !o.playerId().equals(playerId)){
            subjects.put(o.playerId(),o.playerId());
            subjectsCombo.addItem(o.playerId());
        }
    }
}
