package view.GUI.panels;

import model.Tile;
import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;

import view.interfaces.PersonalGoalView;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PersonalGoalPanel extends JPanel implements PersonalGoalView {
    private final Image personalGoalImage = new ImageIcon(getClass().getResource("/PersonalGoalBackground.jpg")).getImage();

    private final Map<Tile[][],Boolean> personalGoals = new HashMap<>();

    private final Tile[][] merged = new Tile[6][5];

    public PersonalGoalPanel(){
        this.setOpaque(false);
        for(Tile[] r : merged)
            Arrays.fill(r,Tile.EMPTY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double ratio = (double)getWidth()/personalGoalImage.getWidth(null);
        int topPadding = (int)Math.round(86*ratio);
        int leftPadding = (int)Math.round(90*ratio);
        int size = (int) Math.round(102*ratio);
        int vHorizontal = (int) Math.round(16*ratio);
        int vVertical = (int) Math.round(12*ratio);

        int horizontalSkip = size + vHorizontal;
        int verticalSkip = size + vVertical;

        g.drawImage(personalGoalImage,0,0,getWidth(),getHeight(),null);

        int y = topPadding;
        for(int i=0;i<6; i++){
            int x = leftPadding;
            for(int j=0;j<5;j++){
                if(merged[i][j]!=Tile.EMPTY) {
                    Image tileImage = new ImageIcon(getClass().getResource("/Tile/"+merged[i][j].name()+".png")).getImage();
                    g.drawImage(tileImage, x+1, y+1, size-1, size-1, null);
                }
                x+=horizontalSkip;
            }
            y+=verticalSkip;
        }
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

        for(Tile[] row : merged)
            Arrays.fill(row, Tile.EMPTY);

        for(Tile[][] k : personalGoals.keySet()){
            for(int i = 0; i<k.length; i++)
                for(int j=0; j<k[i].length; j++)
                    if(k[i][j]!=Tile.EMPTY)
                        merged[i][j]=k[i][j];
        }

        this.revalidate();
        this.repaint();
    }

    private boolean isTheSame(Tile[][] o1, Tile[][] o2){
        for(int i=0;i<o1.length; i++)
            for(int j=0;j<o1[i].length;j++)
                if(o1[i][j]!=o2[i][j])
                    return false;
        return true;
    }
}
