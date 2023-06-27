package view.GUI.panels;

import model.Tile;

import view.graphicInterfaces.PersonalGoalGraphics;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * This class extends JPanel and represents a graphical component that represents the player personal goal
 */
public class PersonalGoalPanel extends JPanel implements PersonalGoalGraphics {

    /**
     * This map contains information whether a personalGoal has been achieved or not
     * <p>
     * It associates the ID of a personalGoal to its achieved status
     */
    private final Map<Integer,Boolean> personalGoals = new HashMap<>();


    /**
     * Matrix of tiles that represents the player's personal goal
     */
    private final Tile[][] merged = new Tile[6][5];

    /**
     * Construct an {@link PersonalGoalPanel} instance that sets the layout of the panel
     */
    public PersonalGoalPanel(){
        this.setOpaque(false);
        for(Tile[] r : merged)
            Arrays.fill(r,Tile.EMPTY);
    }

    /**
     * {@inheritDoc}
     * @param id              the id of this goal, unique among others player personal goal
     * @param hasBeenAchieved true if the goal is achieved
     * @param description     matrix representation of the goal
     */
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

    /**
     * This method overrides {@link  JComponent#paintComponent(Graphics)} drawing an image as background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image personalGoalImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/PersonalGoalBackground.jpg"))).getImage();
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
}
