package view.GUI;

import model.Tile;
import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;
import view.ResourceProvider;
import view.interfaces.PersonalGoalView;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PersonalGoalPanel extends JPanel implements PersonalGoalView {
    private final ImageIcon personalGoalImage;

    private final Map<Tile[][],Boolean> personalGoals = new HashMap<>();

    private final int x = 20;
    private final int y = 19;
    private final int size = 19;
    private final double h = 5.7;
    private final double k = 5;
    public PersonalGoalPanel(){
        this.setLayout(null);
        this.setOpaque(false);

        personalGoalImage = new ImageIcon(ResourceProvider.getResourcePath()+"/front_EMPTY.jpg");
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(personalGoalImage.getImage(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) throws RemoteException {
        boolean found = false;
        for(Tile[][] k : personalGoals.keySet())
            if(isTheSame(k,o.description())){
                personalGoals.put(k,o.achieved());
                found = true;
                break;
            }

        if(!found)
            personalGoals.put(o.description(),o.achieved());

        printPersonalGoal();
    }

    private void printPersonalGoal(){

        Tile[][] merged = new Tile[6][5];
        for(Tile[] row : merged)
            Arrays.fill(row, Tile.EMPTY);

        for(Tile[][] k : personalGoals.keySet()){
            for(int i = 0; i<k.length; i++)
                for(int j=0; j<k[i].length; j++)
                    if(k[i][j]!=Tile.EMPTY)
                        merged[i][j]=k[i][j];
        }

        this.removeAll();

        for (int i = 0; i < merged.length; i++) {
            for (int j = 0; j < merged[0].length; j++) {
                double v = x + size * j + j * h;
                double v1 = y + size * i + i * k;
                if (merged[i][j] == Tile.EMPTY) {
                    JLabel EmptyTile = new JLabel();
                    EmptyTile.setOpaque(false);
                    EmptyTile.setBounds((int) v, (int) v1, size, size);
                    this.add(EmptyTile);
                } else {
                    ImageIcon tile = new ImageIcon(GamePanel.ReturnImageTile(merged[i][j]));
                    JPanel imagetile = new JPanel() {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.drawImage(tile.getImage(), 0, 0, getWidth(), getHeight(), null);
                        }
                    };
                    imagetile.setBounds((int) v, (int) v1, size, size);
                    this.add(imagetile);
                }
            }
        }
    }

    private boolean isTheSame(Tile[][] o1, Tile[][] o2){
        for(int i=0;i<o1.length; i++)
            for(int j=0;j<o1[i].length;j++)
                if(o1[i][j]!=o2[i][j])
                    return false;
        return true;
    }
}
