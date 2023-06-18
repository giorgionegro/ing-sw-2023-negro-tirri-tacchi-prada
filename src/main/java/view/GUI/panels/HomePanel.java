package view.GUI.panels;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class HomePanel extends JPanel implements ActionListener {
    JButton CreateButton;
    JButton JoinButton;
    Image desktop;
    ActionListener listener;
    public HomePanel(ActionListener listener){//JFrame = a GUI window to add components to
        this.listener = listener;

        desktop = new ImageIcon (Objects.requireNonNull(getClass().getResource("/desktop.png"))).getImage();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.insets =  new Insets(20,0,0,0);

        JPanel ButtonsHomePanel = new JPanel();

        this.setBounds(0,0,1000,700);
        this.setLayout(new BorderLayout());


        ButtonsHomePanel.setOpaque(false);
        ButtonsHomePanel.setBackground(new Color(0,0,0));
        ButtonsHomePanel.setLayout(new GridBagLayout());
        this.add(ButtonsHomePanel);
        //Title


        ImageIcon titleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/title.png")));
        JLabel title = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(titleImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        title.setOpaque(false);
        title.setPreferredSize(new Dimension(600,185));


        //Buttons
        Font font = new Font("Century", Font.BOLD, 20);
        JoinButton = new JButton();
        ImageIcon button = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img.png")));
        JoinButton.setIcon(button);
        JLabel join = new JLabel("Join Game");
        join.setFont(font);
        JoinButton.add(join);


        CreateButton= new JButton();
        CreateButton.setIcon(button);
        JLabel create = new JLabel("Create Game");
        create.setFont(font);
        CreateButton.add(create);

        //TODO
        CreateButton.setName("CREATE");
        //

        ButtonsHomePanel.add(title);
        c.gridy++;
        ButtonsHomePanel.add(JoinButton, c);
        c.gridy++;
        c.gridy++;
        ButtonsHomePanel.add(CreateButton, c);

        JoinButton.addActionListener(this);
        CreateButton.addActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(desktop!=null){
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
            g.drawImage(desktop, 0, 0, width, height, null);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == CreateButton) {
           listener.actionPerformed(new ActionEvent(this, e.getID(),"CREATE"));
        } else if (e.getSource() == JoinButton) {
            listener.actionPerformed(new ActionEvent(this, e.getID(),"JOIN"));
        }

    }
}
