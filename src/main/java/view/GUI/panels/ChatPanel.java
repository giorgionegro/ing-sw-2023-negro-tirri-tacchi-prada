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


/**
 * This class extends JPanel and represents a graphical component that shows the chat
 */
public class ChatPanel extends JPanel implements PlayerChatGraphics {

    /**
     * This map contains associations between player names and player ids
     */
    private final Map<String, String> subjects = new HashMap<>() {{
        this.put("Everyone", "");
    }};

    /**
     * The player ID of this client
     */
    private final String playerId;
    /**
     * List of messages
     */
    private final List<Message> messages = new ArrayList<>();


    /*----------------- GRAPHICS LAYOUT ----------------*/


    /**
     * This is the background image of {@link #Send}
     */
    private final Image buttonBackground = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/img.png"))).getImage();
    /**
     * The scrollable text area component used for displaying the messages from the other players.
     */
    private final JScrollPane scrollTextArea = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    /**
     * The combo box component used for selecting the receiver of the message
     */
    private final JComboBox<String> subjectsCombo = new JComboBox<>(new String[]{"Everyone"});
    /**
     * This is the button that allows the user to send a message
     */
    private final JButton Send = new JButton("▶") {
        /**
         * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
         * @param g the <code>Graphics</code> object to protect
         */
        protected void paintComponent(Graphics g) {
            g.drawImage(ChatPanel.this.buttonBackground, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }
    };

    /**
     * The text field component used for entering text message to send
     */
    private final JTextField mex = new JTextField();


    /** Construct an {@link CreateGamePanel} instance that uses the given {@link ActionListener} as listener for buttons events
     * @param viewLogic the ActionListener to be notified when a button is pressed
     * @param playerId player id of the receiver of the message
     */
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

    /**
     * This method adds a subject to the list of subjects (which are the receiver of the messages) and updates the subjects combo box.
     * @param subjectID player id
     */
    public void addSubject(String subjectID) {
        if (!this.subjects.containsKey(subjectID) && !subjectID.equals(this.playerId)) {
            this.subjects.put(subjectID, subjectID);
            this.subjectsCombo.addItem(subjectID);
        }
    }
    /**
     * {@inheritDoc}
     * @param chat list of messages sent to a player.
     */
    @Override
    public void updatePlayerChatGraphics(List<? extends Message> chat) {
        this.messages.clear();
        this.messages.addAll(chat);
        this.updateText();
    }

    /**
     * This method updates the text in the JTextArea with the messages stored in the 'messages' list.
     * It scrolls to the end of the text area after updating.
     */
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


    /**
     *  This method initializes the layout of the component
     */
    private void initializeLayout() {
        GridBagConstraints constraints = new GridBagConstraints(
                0, 0,
                1, 1,
                1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0),
                0, 0
        );
        
        this.setLayout(new GridBagLayout());

        Dimension zeroDimension = new Dimension(0, 0);

        constraints.gridwidth = 2;
        constraints.weighty = 3;
        this.scrollTextArea.setPreferredSize(zeroDimension);
        this.add(this.scrollTextArea, constraints);

        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.weighty = 0;
        constraints.gridx++;
        constraints.gridy++;
        constraints.ipady=60;
        this.Send.setPreferredSize(zeroDimension);
        this.Send.setBackground(new Color(0, 0, 0, 0));
        this.add(this.Send, constraints);

        constraints.gridheight = 1;
        constraints.weightx = 6;
        constraints.gridx--;
        constraints.gridy++;
        constraints.ipady = 30;
        this.mex.setPreferredSize(zeroDimension);
        this.add(this.mex, constraints);

        constraints.gridy--;
        this.subjectsCombo.setPreferredSize(zeroDimension);
        this.add(this.subjectsCombo, constraints);
    }
}
