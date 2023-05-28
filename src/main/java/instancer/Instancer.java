package instancer;

import model.abstractModel.Game;
import util.Observer;

import java.io.File;

public class Instancer implements Observer<Game, Game.Event> {

    final String subdir="\\instances\\";
    String instancesDir;
    public Instancer(){
        String path = this.getClass().getResource("./").getPath();
        File instancesDir = new File(path+subdir);
        //if(!instancesDir.exists())
    }

    @Override
    public void update(Game o, Game.Event arg) {
        if(arg == Game.Event.NEXT_TURN){
            o.getInstance();

        }
    }
}
