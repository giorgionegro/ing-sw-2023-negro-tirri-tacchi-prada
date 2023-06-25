package view.GUI.panels;

import model.abstractModel.Message;
import view.ViewLogic;
import view.graphicInterfaces.PlayerChatGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;


public class ChatPanel extends JPanel implements PlayerChatGraphics {

    private final Map<String, String> subjects = new HashMap<>() {{
        this.put("Everyone", "");
    }};
    private final String playerId;
    private final List<Message> messages = new ArrayList<>();
    /*----------------- GRAPHICS LAYOUT ----------------*/
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();
    private final JScrollPane scrollTextArea = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final JComboBox<String> subjectsCombo = new JComboBox<>(new String[]{"Everyone"});
    private final JButton Send = new JButton("Send") {
        protected void paintComponent(Graphics g) {
            g.drawImage(ChatPanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };
    private final JTextField mex = new JTextField();
    private final GridBagConstraints constraints = new GridBagConstraints(
            0, 0,
            1, 1,
            1, 1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0, 0
    );

    public ChatPanel(ActionListener viewLogic, String playerId) {
        super();
        this.playerId = playerId;

        this.initializeLayout();

        this.Send.addActionListener(e -> {
            String message = this.mex.getText();
            String subject = this.subjects.get((String) this.subjectsCombo.getSelectedItem());
            viewLogic.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MESSAGE, playerId + "\n" + subject + "\n" + message));
        });
    }

    public void addSubject(String subjectID) {
        if (!this.subjects.containsKey(subjectID) && !subjectID.equals(this.playerId)) {
            this.subjects.put(subjectID, subjectID);
            this.subjectsCombo.addItem(subjectID);
        }
    }

    @Override
    public void updatePlayerChatGraphics(List<? extends Message> chat) {
        this.messages.clear();
        this.messages.addAll(chat);
        this.updateText();
    }

    private void updateText() {
        JTextArea textarea = new JTextArea();
        textarea.setBackground(new Color(255, 255, 200));
        textarea.setEditable(false);

        textarea.setLineWrap(true);
        for (Message m : this.messages) {
            String sender = m.getSender();
            String receiver = m.getReceiver();
            String text = (sender.equals(this.playerId) ? "YOU" : sender) + " to " + (receiver.isBlank() ? "Everyone" : receiver.equals(this.playerId) ? "YOU" : receiver) + ": " + m.getText() + "\n";
            textarea.append(text);
        }

        this.scrollTextArea.setViewportView(textarea);

        //Scrolls to the end
        this.scrollTextArea.revalidate();
        JScrollBar verticalScrollBar = this.scrollTextArea.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        this.scrollTextArea.revalidate();
        this.scrollTextArea.repaint();
    }

    private void initializeLayout() {
        this.setLayout(new GridBagLayout());

        Dimension zeroDimension = new Dimension(0, 0);

        this.constraints.gridwidth = 2;
        this.constraints.weighty = 3;
        this.scrollTextArea.setPreferredSize(zeroDimension);
        this.add(this.scrollTextArea, this.constraints);

        this.constraints.gridwidth = 1;
        this.constraints.gridheight = 2;
        this.constraints.weighty = 1;
        this.constraints.gridx++;
        this.constraints.gridy++;
        this.Send.setPreferredSize(zeroDimension);
        this.Send.setBackground(new Color(0, 0, 0, 0));
        this.add(this.Send, this.constraints);

        this.constraints.gridheight = 1;
        this.constraints.weightx = 6;
        this.constraints.gridx--;
        this.constraints.gridy++;
        this.mex.setPreferredSize(zeroDimension);
        this.add(this.mex, this.constraints);

        this.constraints.gridy--;
        this.subjectsCombo.setPreferredSize(zeroDimension);
        this.add(this.subjectsCombo, this.constraints);
    }
}
