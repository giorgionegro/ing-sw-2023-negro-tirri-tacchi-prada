package view.GUI.panels;

import model.Tile;
import model.abstractModel.PersonalGoal;
import modelView.PersonalGoalInfo;

import view.graphicInterfaces.PersonalGoalGraphics;
import view.interfaces.PersonalGoalView;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PersonalGoalPanel extends JPanel implements PersonalGoalGraphics {
    private final Image personalGoalImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/PersonalGoalBackground.jpg"))).getImage();

    private final Map<Integer,Boolean> personalGoals = new HashMap<>();

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
                    Image tileImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Tile/" + merged[i][j].name() + ".png"))).getImage();
                    g.drawImage(tileImage, x+1, y+1, size-1, size-1, null);
                }
                x+=horizontalSkip;
            }
            y+=verticalSkip;
        }
    }


    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description) {
        if(!personalGoals.containsKey(id)){
            for(int i=0;i<description.length; i++)
                for(int j=0;j<description[i].length;j++) {
                    if (description[i][j] != Tile.EMPTY) {
                        merged[i][j] = description[i][j];
                    }
                }
        }
        personalGoals.put(id,hasBeenAchieved);
        this.revalidate();
        this.repaint();
    }
}
