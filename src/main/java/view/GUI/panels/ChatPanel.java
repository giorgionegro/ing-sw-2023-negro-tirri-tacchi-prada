package view.GUI.panels;

import model.abstractModel.Message;
import view.ViewLogic;
import view.graphicInterfaces.PlayerChatGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import java.util.List;

public class ChatPanel extends JPanel implements PlayerChatGraphics {

    private final Map<String,String> subjects = new HashMap<>(){{
        put("Everyone","");
    }};
    private final String playerId;
    private final List<Message> messages = new ArrayList<>();

    public ChatPanel(ActionListener viewLogic, String playerId){
        this.playerId = playerId;

        initializeLayout();

        invia.addActionListener(e -> {
            String message =  mex.getText();
            String subject = subjects.get((String) subjectsCombo.getSelectedItem());
            viewLogic.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MESSAGE,playerId+"\n"+subject+"\n"+message));
        });
    }

    public void addSubject(String subjectID){
        if(!subjects.containsKey(subjectID) && !subjectID.equals(this.playerId)){
            subjects.put(subjectID,subjectID);
            subjectsCombo.addItem(subjectID);
        }
    }

    @Override
    public void updatePlayerChatGraphics(List<Message> chat) {
        messages.clear();
        messages.addAll(chat);
        updateText();
    }

    private void updateText(){
        JTextArea textarea = new JTextArea();

        textarea.setBackground(new Color(255,255,200));
        textarea.setEditable(false);

        textarea.setLineWrap(true);
        for(Message m : messages){
            String sender = m.getSender();
            String receiver = m.getReceiver();
            String text = (sender.equals(playerId) ? "YOU" : sender) + " to " + (receiver.isBlank() ? "Everyone" : receiver.equals(playerId) ? "YOU" : receiver) + ": " + m.getText()+"\n";
            textarea.append(text);
        }

        scrolltextarea.setViewportView(textarea);

        //Scrolls to the end
        scrolltextarea.revalidate();
        JScrollBar verticalScrollBar = scrolltextarea.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        scrolltextarea.revalidate();
        scrolltextarea.repaint();
    }


    /*----------------- GRAPHICS LAYOUT ----------------*/
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png"))).getImage();
    private final JScrollPane scrolltextarea = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final JComboBox<String> subjectsCombo = new JComboBox<>(new String[]{"Everyone"});
    private final JButton invia = new JButton("Send"){
        protected void paintComponent(Graphics g) {
            g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), null);
            super.paintComponent(g);
        }
    };
    private final JTextField mex = new JTextField();
    private final GridBagConstraints constraints = new GridBagConstraints(
            0,0,
            1,1,
            1,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private void initializeLayout(){
        this.setLayout(new GridBagLayout());

        constraints.gridwidth = 2;
        constraints.weighty = 12;
        scrolltextarea.setPreferredSize(new Dimension(0,0));
        this.add(scrolltextarea,constraints);

        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.weighty = 1;
        constraints.gridx++;
        constraints.gridy++;
        invia.setBackground(new Color(0,0,0,0));
        this.add(invia,constraints);

        constraints.gridheight = 1;
        constraints.weightx = 18;
        constraints.gridx--;
        constraints.gridy++;
        this.add(mex, constraints);

        constraints.gridy--;
        this.add(subjectsCombo,constraints);
    }
}
