package view.GUI;

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

    JComboBox<String> subjectsCombo = new JComboBox<>();
    JButton invia;
    JTextField mex;
    private String playerId;
    private final ServerInterface serverInterface;
    private final ClientInterface clientInterface;
    private final JScrollPane scrolltextarea;

    private final List<Message> messages = new ArrayList<>();

    public ChatPanel(ServerInterface serverInterface, ClientInterface clientInterface){
        this.serverInterface = serverInterface;
        this.clientInterface = clientInterface;



        this.setBackground(new Color(80,55,40));
        SpringLayout layoutChat = new SpringLayout();
        this.setLayout(layoutChat);
        int margine = 5;

        scrolltextarea = new JScrollPane();

        updateText();

        mex = new JTextField();
        invia = new JButton("Send");
        for(String s : subjects.keySet())
            subjectsCombo.add(s,new JLabel(s));

        invia.addActionListener(this);


        this.add(scrolltextarea);
        this.add(subjectsCombo);
        this.add(mex);
        this.add(invia);

        layoutChat.putConstraint(BorderLayout.WEST, scrolltextarea, margine, SpringLayout.WEST, this);
        layoutChat.putConstraint(BorderLayout.NORTH, scrolltextarea, margine, SpringLayout.NORTH, this);
        layoutChat.putConstraint(BorderLayout.EAST, scrolltextarea, -margine, SpringLayout.EAST, this);

        layoutChat.putConstraint(BorderLayout.NORTH, subjectsCombo, margine, SpringLayout.SOUTH, scrolltextarea);
        layoutChat.putConstraint(BorderLayout.SOUTH,subjectsCombo, 0, SpringLayout.NORTH, mex);
        layoutChat.putConstraint(BorderLayout.EAST,subjectsCombo, -margine, SpringLayout.WEST, invia);
        layoutChat.putConstraint(BorderLayout.WEST,subjectsCombo, margine, SpringLayout.WEST, this);


        layoutChat.putConstraint(BorderLayout.WEST, mex, margine, SpringLayout.WEST, this);
        layoutChat.putConstraint(BorderLayout.EAST, mex, -margine, SpringLayout.WEST, invia);
        layoutChat.putConstraint(BorderLayout.SOUTH, mex, -margine, SpringLayout.SOUTH, this);

        layoutChat.putConstraint(BorderLayout.NORTH, invia, margine, SpringLayout.SOUTH, scrolltextarea);
        layoutChat.putConstraint(BorderLayout.EAST, invia, -margine, SpringLayout.EAST, this);
        layoutChat.putConstraint(BorderLayout.SOUTH, invia, -margine, SpringLayout.SOUTH, this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == invia){
            String message =  mex.getText();
            String subject = (String) subjectsCombo.getSelectedItem();

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

        for(Message m : messages)
            textarea.append(m.getSubject()+": "+m.getText()+"\n");

        scrolltextarea.setViewportView(textarea);
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
            subjectsCombo.add(o.playerId(),new JLabel(o.playerId()));
        }
    }
}
