package modelView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerInfo implements Serializable {

    private final String error; //TODO Change definition of error

    private final List<GamesManagerInfo> lobbies;

    public ServerInfo(String error, List<GamesManagerInfo> lobbies) {
        this.error = error;
        this.lobbies = new ArrayList<>(lobbies);
    }

    public String getError() {
        return error;
    }

    public List<GamesManagerInfo> getLobbies() {
        return lobbies;
    }
}
