package view.GUI.panels;

import model.Token;
import model.abstractModel.CommonGoal;
import model.abstractModel.Player;
import modelView.CommonGoalInfo;
import modelView.PlayerInfo;

import view.GUI.AspectRatioLayout;
import view.interfaces.CommonGoalView;
import view.interfaces.PlayerView;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonGoalsPanel extends JPanel implements PlayerView, CommonGoalView {

    private final GridBagConstraints cardConstraints = new GridBagConstraints(
            -1,-1,
            1,1,
            4,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );

    private final GridBagConstraints spacerConstraints = new GridBagConstraints(
            -1,-1,
            1,1,
            1,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            new Insets(0,0,0,0),
            0,0
    );


    private final Map<String, Token> commonGoals = new HashMap<>();
    private final Map<String, Token> achieved = new HashMap<>();

    public CommonGoalsPanel(){
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
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
        this.add(new Container(),spacerConstraints);

        for(String id : commonGoals.keySet()){
            Image commonGoalImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/CommonGoals/GUI/" + id + ".jpg"))).getImage();
            Image tokenImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Token/" + commonGoals.get(id).name() + ".png"))).getImage();
            Image achievedImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/CommonGoals/GUI/achieved.png"))).getImage();

            JLabel commonGoal1 = new JLabel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(commonGoalImg, 0, 0, getWidth(), getHeight(), null);
                    g.drawImage(tokenImg, 0, 0, getWidth(), getHeight(), null);
                    if(achieved.containsKey(id)){
                        g.drawImage(achievedImg, 0, 0, getWidth(), getHeight(), null);
                    }
                }
            };

            Container c = new Container();
            c.setLayout(new AspectRatioLayout((float)1385/913));
            c.add(commonGoal1);
            this.add(c,cardConstraints);
            this.add(new Container(),spacerConstraints);
        }
        this.revalidate();
        this.repaint();
    }
}
