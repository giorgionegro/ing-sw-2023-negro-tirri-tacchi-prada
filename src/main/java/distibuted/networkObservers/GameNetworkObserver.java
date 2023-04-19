package distibuted.networkObservers;

import model.abstractModel.Game;
import modelView.GameInfo;
import util.Observer;
import view.interfaces.GameView;

import java.rmi.RemoteException;

public class GameNetworkObserver implements Observer<Game, Game.GameEvent> {

    private final GameView view;
    public GameNetworkObserver(GameView view){
        this.view = view;
    }

    @Override
    public void update(Game o, Game.GameEvent arg) {
        try {
            if (arg == null) {
                view.update(new GameInfo(o.getGameStatus(), o.isLastTurn(), o.getTurnPlayerId()), arg);
            } else {
                switch (arg) {
                    case GAME_STARTED ->
                            view.update(new GameInfo(o.getGameStatus(), o.isLastTurn(), o.getTurnPlayerId()), arg);
                    case NEXT_TURN ->
                            view.update(new GameInfo(o.getGameStatus(), o.isLastTurn(), o.getTurnPlayerId()), arg);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
