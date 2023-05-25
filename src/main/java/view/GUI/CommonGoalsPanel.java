package view.GUI;

import model.Token;
import model.abstractModel.CommonGoal;
import model.abstractModel.Player;
import modelView.CommonGoalInfo;
import modelView.PlayerInfo;
import view.ResourceProvider;
import view.interfaces.CommonGoalView;
import view.interfaces.PlayerView;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class CommonGoalsPanel extends JPanel implements PlayerView, CommonGoalView {

    private final Map<String, Token> commonGoals = new HashMap<>();
    private final Map<String, Token> achieved = new HashMap<>();
    public CommonGoalsPanel(){
        this.setOpaque(false);
        this.setLayout(null);
    }


    @Override
    public void update(PlayerInfo o, Player.Event evt) throws RemoteException {
        achieved.putAll(o.achievedCommonGoals());
        refreshCommonGoalState();
    }

    @Override
    public void update(CommonGoalInfo o, CommonGoal.Event evt) throws RemoteException {
        commonGoals.put(o.id(),o.tokenState());
        refreshCommonGoalState();
    }

    private void refreshCommonGoalState(){
        this.removeAll();
        int h = 0;
        int k = 0;
        for(String id : commonGoals.keySet()){
            ImageIcon commonGoal_1 = new ImageIcon(GamePanel.returnImageCommonGoal(id));
            ImageIcon token = new ImageIcon(GamePanel.returnImageToken(commonGoals.get(id)));
            ImageIcon achievedImage = new ImageIcon(ResourceProvider.getResourcePath()+"/Scoring/achieved.png");
            JLabel commonGoal1 = new JLabel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(commonGoal_1.getImage(), 0, 0, getWidth(), getHeight(), null);
                    g.drawImage(token.getImage(), 0, 0, getWidth(), getHeight(), null);
                    if(achieved.containsKey(id)){
                        g.drawImage(achievedImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                    }
                }
            };
            commonGoal1.setBounds(k, h, 203 , 140);
            this.add(commonGoal1);
            k = 220;
        }
        this.revalidate();
        this.repaint();
    }
}
