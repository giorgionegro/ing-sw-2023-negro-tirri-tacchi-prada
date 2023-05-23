package modelView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @param error TODO Change definition of error
 */
public record ServerInfo(String error, List<GamesManagerInfo> lobbies) implements Serializable {

    public ServerInfo(String error, @NotNull List<GamesManagerInfo> lobbies) {
        this.error = error;
        this.lobbies = new ArrayList<>(lobbies);
    }
}
