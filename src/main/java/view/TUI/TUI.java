package view.TUI;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.StandardMessage;
import model.Tile;
import model.Token;
import model.User;
import model.abstractModel.*;
import modelView.*;
import org.jetbrains.annotations.NotNull;
import util.TimedLock;
import view.interfaces.UI;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.*;

import static view.TUI.TUIdraw.*;
import static view.TUI.TUIutils.*;


public class TUI implements UI {

    static final PrintStream out = System.out;


    /*-----------VIEW COMPONENTS--------------*/
    static final int renderHeight = 53;
    static final int renderWidth = 140;
    /*-----------------------------------------*/

    final char[][] canvas = new char[renderHeight][renderWidth];
    final int[][] canvasColors = new int[renderHeight][renderWidth];
    //old cmds to be shifted up
    final List<Pair> oldCmds = new ArrayList<>();
    final Object renderLock = new Object();

    /*----------------------------------------*/

    /*-----------------------------------------*/

    private final List<PersonalGoalInfo> currentPersonalGoals = new ArrayList<>();
    private final Map<String, CommonGoalInfo> commonGoals = new HashMap<>();

    /*------------------------------------------*/

    /*--------SERVER INTERACTION FUNCTIONS--------*/
    private final Map<String, ShelfInfo> currentShelves;
    /*------------VIEW UTILITIES---------------*/
    private final TimedLock<Boolean> serverWaiter = new TimedLock<>(false);
    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Integer> points = new HashMap<>();

    /*-----------------------------------------------*/


    String cursor = "";

    int updated = 0;
    private long currentSessionTime = -1;
    private GameInfo currentGameState;
    private UserInfo user;
    private Map<String, Token> achievedCommonGoals = new HashMap<>();
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;


    private String thisPlayerId;
    /*--------DISTRIBUTION OBJECTS-------------*/
    private ServerInterface server;
    private ClientInterface client;
    private boolean viewLock = false;
    private boolean GameRunning;
    private View currentView;

    public TUI() {
        super();
        this.currentView = View.SERVER_INTERACTION;
        this.currentShelves = new HashMap<>();
        drawBox(0, 0, renderHeight, renderWidth, DEFAULT, this.canvas, this.canvasColors);
        drawCommandLine(this.cursor, this.oldCmds, this.canvas, this.canvasColors);
        this.updateView(false);
    }

    //move cursor to arbitrary position
    public static void moveCursor(int y, int x) {
        System.out.print("\033[" + y + ";" + x + "H");
    }


    /*-------------UI--------------------------*/
    public String askRMIorSocket() {
        return this.readCommandLine("Connect with RMI (r) or SOCKET (s)?, empty to exit: ");
    }

    public void showError(String error) {
        this.printCommandLine(error, RED);
    }

    public void run(ServerInterface server, ClientInterface client) {
        this.server = server;
        this.client = client;

        this.currentView = View.SERVER_INTERACTION;

        if (!this.serverWaiter.hasBeenNotified()) {
            try {
                this.serverWaiter.setValue(true);
                this.serverWaiter.lock(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Boolean exit = this.serverWaiter.getValue();

        this.serverWaiter.reset();

        if (!exit) {
            this.printCommandLine("CONNECTED", GREEN);
        }

        while (!exit) {
            String readLine = this.readCommandLine("(h for commands)-> ");
            switch (readLine) {
                case "h" -> this.printCommandLine("1: Create a game\n2: Join a game\nexit: Close this window");
                case "1" -> {
                    try {
                        this.createGame();
                    } catch (RemoteException e) {
                        this.printCommandLine(e.getMessage(), RED);
                    }
                }
                case "2" -> {
                    try {
                        this.joinGame();
                    } catch (RemoteException e) {
                        this.printCommandLine("Error while joining a game", RED);
                    }
                }
                case "exit" -> exit = true;
                default -> this.printCommandLine("Wrong command", RED);

            }
        }
    }

    private void createGame() throws RemoteException {
        String gameId = this.readCommandLine("GameId: ");
        String p = this.readCommandLine("PlayerNumber (between 2 and 4): ");
        int k = Integer.parseInt(p);
        if (k > 1 && k < 5) {

            this.currentSessionTime = System.currentTimeMillis();
            this.serverWaiter.reset();
            this.server.createGame(this.client, new NewGameInfo(gameId, "STANDARD", k, this.currentSessionTime));

            if (!this.serverWaiter.hasBeenNotified()) {
                this.serverWaiter.setValue(true);
                try {
                    this.serverWaiter.lock(6000);
                } catch (InterruptedException e) {
                    throw new RemoteException("Connection timeout error");
                }
            }

            if (this.serverWaiter.getValue())
                throw new RemoteException(this.user.eventMessage());
        } else {
            throw new RemoteException("Wrong parameters (number between 2 and 4)");
        }
    }

    private void joinGame() throws RemoteException {
        String gameId = this.readCommandLine("GameId: ");
        String playerId = this.readCommandLine("Write playerId (empty to exit): ");
        if (!playerId.equals("")) {
            this.serverWaiter.reset();
            this.currentSessionTime = System.currentTimeMillis();
            this.server.joinGame(this.client, new LoginInfo(playerId, gameId, this.currentSessionTime));
        } else
            return;

        if (!this.serverWaiter.hasBeenNotified()) {
            try {
                this.serverWaiter.setValue(true);
                this.serverWaiter.lock(6000);
            } catch (InterruptedException e) {
                throw new RemoteException("Login timeout error");
            }
        }

        if (!this.serverWaiter.getValue()) {
            this.thisPlayerId = playerId;
            this.gameRoutine();
        }
    }

    private void gameRoutine() throws RemoteException {
        this.currentView = View.GAME_INTERACTION;
        this.updateView(true);
        this.GameRunning = true;
        while (this.GameRunning) {
            String readLine = this.readCommandLine("(h for commands)-> ");
            if (this.GameRunning)
                switch (readLine) {
                    case "h" -> this.printCommandLine("1: Update view status\n2: Pick tiles\n3: Send message\n4: Leave game");
                    case "1" -> this.updateView(false);
                    case "2" -> this.pickTiles();
                    case "3" -> this.sendMessage();
                    case "4" -> this.leave();
                    default -> this.printCommandLine("Wrong command", RED);
                }
        }
        this.resetInfo();
        this.currentView = View.SERVER_INTERACTION;
        this.updateView(true);
    }

    /*--------------------------------------------------*/

    public void resetInfo() {
        this.currentPersonalGoals.clear();
        this.currentGameState = null;
        this.commonGoals.clear();
        this.achievedCommonGoals.clear();
        this.currentShelves.clear();
        this.currentLivingRoom = null;
        this.currentPlayerChat = null;
        this.thisPlayerId = null;
    }


    private void sendMessage() throws RemoteException {
        String receiver;
        String content;
        synchronized (this.renderLock) {
            this.viewLock = true;
            receiver = this.readCommandLine("To (empty for everyone): ");
            content = this.readCommandLine("Message content: ");
            this.viewLock = false;
        }
        this.server.sendMessage(this.client, new StandardMessage(this.thisPlayerId, receiver, content));

    }

    private void pickTiles() throws RemoteException {
        final int pickableNum = 3;
        List<PickedTile> tiles = new ArrayList<>();
        boolean choosing = true;
        do {
            this.viewLock = true;
            this.printCommandLine("Remaining pickable tiles: " + pickableNum + "\nWrite row,col r2,c2 to pick up to three tiles");
            this.viewLock = false;
            String choice = this.readCommandLine("-> ");
            String[] split = choice.split(" ");
            //if more than 3 tiles are picked ignores the rest
            List<PickedTile> tTiles = new ArrayList<>();
            boolean formatError = false;
            for (int i = 0; i < split.length && i < pickableNum; i++) {
                try {
                    String[] split1 = split[i].split(",");
                    int x = Integer.parseInt(split1[0]);
                    int y = Integer.parseInt(split1[1]);
                    tTiles.add(new PickedTile(x, y));
                    this.printCommandLine("Tile " + x + "," + y + " picked", GREEN);
                } catch (NumberFormatException e) {
                    this.printCommandLine("Illegal character", RED);
                    formatError = true;
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.printCommandLine("Wrong format, Should be r1,c1 r2,c2 r3,c3", RED);
                    formatError = true;
                    break;
                }
            }
            if (formatError)
                continue;
            //check if the tiles are pick-able, pick-able if they are all in the same row or column and adjacent to each other
            boolean pickable = this.pickable(tTiles, this.currentLivingRoom.board());
            if (pickable) {
                tiles.addAll(tTiles);
                choosing = false;
            } else {
                this.printCommandLine("Tiles not pickable", RED);
            }
        } while (choosing);

        choosing = true;
        int sC = 0;
        do {
            try {
                this.viewLock = true;
                String sCol = this.readCommandLine("Shelf col: ");
                this.viewLock = false;
                sC = Integer.parseInt(sCol);
                this.printCommandLine("Shelf " + sC + " chosen", GREEN);
                //check if column has enough space for the tiles
                Tile[][] myShelf = this.currentShelves.get(this.thisPlayerId).shelf();
                int finalSC = sC;
                // count empty tiles in the column sC
                int emptyTiles = Arrays.stream(myShelf).mapToInt(row -> row[finalSC] == Tile.EMPTY ? 1 : 0).sum();//TODO test this
                if (emptyTiles < tiles.size()) {
                    this.printCommandLine("Not enough space in the shelf", RED);
                    continue;
                }
                choosing = false;
            } catch (NumberFormatException e) {
                this.printCommandLine("Not a number", RED);
            }
        } while (choosing);

        this.server.doPlayerMove(this.client, new PlayerMoveInfo(tiles, sC));
    }

    private boolean pickable(List<PickedTile> pickedTiles, Tile[][] board) {
        if (!this.areTilesDifferent(new ArrayList<>(pickedTiles))) {
            this.printCommandLine("Tiles are not different", RED);
            return false;
        }
        if (!this.areTilesAligned(new ArrayList<>(pickedTiles))) {
            this.printCommandLine("Tiles are not aligned", RED);
            return false;
        }
        for (PickedTile tile : pickedTiles)
            if (!this.isTilePickable(tile.row(), tile.col(), board)) {
                this.printCommandLine("Tile not pickable", RED);
                return false;
            }
        return true;
    }

    private boolean areTilesAligned(@NotNull List<PickedTile> pickedTiles) {

        boolean rowAligned = true;
        boolean colAligned = true;

        for (int i = 1; i < pickedTiles.size(); i++) {
            rowAligned = rowAligned && (pickedTiles.get(i - 1).row() == pickedTiles.get(i).row());
            colAligned = colAligned && (pickedTiles.get(i - 1).col() == pickedTiles.get(i).col());
        }

        if (rowAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::col));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).col() + 1 != pickedTiles.get(i + 1).col())
                    return false;
        }

        if (colAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::row));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).row() + 1 != pickedTiles.get(i + 1).row())
                    return false;
        }


        return rowAligned || colAligned;
    }


    /**
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(@NotNull List<PickedTile> pickedTiles) {
        for (int i = 0; i < pickedTiles.size() - 1; i++) {
            for (int j = i + 1; j < pickedTiles.size(); j++) {
                if (pickedTiles.get(i).row() == pickedTiles.get(j).row())
                    if (pickedTiles.get(i).col() == pickedTiles.get(j).col())
                        return false;
            }
        }
        return true;
    }

    /**
     * @param row    row of the tile
     * @param column column of the tile
     * @param board  board to check
     * @return true if tile is pickable, false otherwise
     */
    private boolean isTilePickable(int row, int column, Tile[] @NotNull [] board) {
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column] == Tile.EMPTY || board[row][column] == null)
            return false;

        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        return board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY;
    }

    private void leave() {
        try {
            this.server.leaveGame(this.client);
            this.GameRunning = false;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateView(boolean force) {
        if (!this.viewLock || force) {
            //clear the matrix
            Arrays.stream(this.canvas).forEach(a -> Arrays.fill(a, ' '));
            Arrays.stream(this.canvasColors).forEach(a -> Arrays.fill(a, DEFAULT));

            drawBox(0, 0, renderHeight, renderWidth, DEFAULT, this.canvas, this.canvasColors);
            drawCommandLine(this.cursor, this.oldCmds, this.canvas, this.canvasColors);

            switch (this.currentView) {
                case GAME_INTERACTION -> {
                    drawCommandLine(this.cursor, this.oldCmds, this.canvas, this.canvasColors);
                    drawGameState(this.currentGameState, this.canvas, this.canvasColors);
                    drawLivingRoom(this.currentLivingRoom, this.canvas, this.canvasColors);
                    drawShelves(this.currentShelves, this.thisPlayerId, this.currentGameState, this.canvas, this.canvasColors);
                    drawChat(this.currentPlayerChat, this.canvas, this.canvasColors);
                    drawCommonGoals(this.commonGoals, this.achievedCommonGoals, this.canvas, this.canvasColors);
                    drawPersonalGoal(this.currentPersonalGoals, this.canvas, this.canvasColors);
                }
                case GAME_ENDED -> drawGameEnd(this.points, this.canvas, this.canvasColors);
            }

            this.updated++;
            drawString(this.updated + " ", 0, 0, GREEN, 20, this.canvas, this.canvasColors);
            this.render();
        }

    }

    /*--------------------------------------------------*/

    private String renderPixel(int x, int y) {
        return "\u001B[" + this.canvasColors[x][y] + "m" + this.canvas[x][y] + "\u001B[0m";
    }



    /*-------------------------------------------------------------*/


    /*-----------------COMMAND LINE--------------------------------*/


    public String readCommandLine(String message) {
        this.cursor = message;

        out.print(" " + message);
        String cmd = this.scanner.nextLine();
        this.cursor = "";
        this.oldCmds.add(new Pair(message + " " + cmd, DEFAULT));
        //trim old commands to 8
        while (this.oldCmds.size() > 8)
            this.oldCmds.remove(0);

        this.updateView(true);
        return cmd;

    }

    public void printCommandLine(String toPrint) {
        this.printCommandLine(toPrint, DEFAULT);
    }

    public void printCommandLine(String toPrint, int colour) {
        String[] lines = toPrint.split("\n");
        for (String s : lines)
            this.oldCmds.add(new Pair(s, colour));

        while (this.oldCmds.size() > 8)
            this.oldCmds.remove(0);

        this.updateView(true);
    }

    public void render() {
        synchronized (this.renderLock) {
            ClearScreen(s -> {
                this.printCommandLine(s, DEFAULT);
            });
            if (this.currentGameState != null) {
                drawGameState(this.currentGameState, this.canvas, this.canvasColors);
            }
            for (int i = 0; i < this.canvas.length; i++) {
                for (int j = 0; j < this.canvas[0].length; j++) {
                    out.print(this.renderPixel(i, j));
                }
                out.println();
            }
            moveCursor(commandLineY + commandLineHeight, commandLineX + 2 + this.cursor.length());
        }
    }

    /**
     * receives updates for the @{@link PlayerChatInfo}
     *
     * @param pC  @{@link PlayerChatInfo}
     * @param evt @{@link PlayerChat.Event}
     */
    public void update(PlayerChatInfo pC, PlayerChat.Event evt) {
        //set current player chat
        this.currentPlayerChat = pC;
        this.updateView(false);
    }

    /*-----------------------------------------------------------*/


    /**
     * receives updates for the @{@link LivingRoomInfo}
     *
     * @param lR  @{@link LivingRoomInfo}
     * @param evt @{@link LivingRoom.Event}
     */
    public void update(LivingRoomInfo lR, LivingRoom.Event evt) {
        //set current living room
        this.currentLivingRoom = lR;
        this.updateView(false);
    }


    /**
     * receives updates for the @{@link ShelfInfo}
     *
     * @param sV  @{@link ShelfInfo}
     * @param evt @{@link Shelf.Event}
     */
    public void update(ShelfInfo sV, Shelf.Event evt) {
        //set current shelf

        this.currentShelves.put(sV.playerId(), sV);

        this.updateView(false);
    }


    /**
     * receives updates for the @{@link CommonGoalInfo}
     *
     * @param cG  @{@link CommonGoalInfo}
     * @param evt @{@link CommonGoal.Event}
     */
    public void update(CommonGoalInfo cG, CommonGoal.Event evt) {
        this.commonGoals.put(cG.id(), cG);
        this.updateView(false);
    }


    /**
     * receives updates for the @{@link GameInfo}
     *
     * @param g   @{@link GameInfo}
     * @param evt @{@link Game.Event}
     */
    public void update(GameInfo g, Game.Event evt) {
        this.currentGameState = g;

        if (g.status() == Game.GameStatus.ENDED) {
            this.GameRunning = false;
            this.currentView = View.GAME_ENDED;
            this.points.clear();
            this.points.putAll(g.points());
            this.printCommandLine("Game Ended, press enter to return to login menu");
        } else if (g.status() == Game.GameStatus.STARTED) {
            this.updateView(true);
        }
    }


    /**
     * receives updates for the @{@link PersonalGoalInfo}
     *
     * @param pG  @{@link PersonalGoalInfo}
     * @param evt @{@link PersonalGoal.Event}
     */
    public void update(PersonalGoalInfo pG, PersonalGoal.Event evt) {
        //check if personal goal is already present in current personal goals
        int index = this.currentPersonalGoals.stream().map(PersonalGoalInfo::description).toList().indexOf(pG.description());

        if (index != -1)
            this.currentPersonalGoals.set(index, pG);
        else
            this.currentPersonalGoals.add(pG);

        this.updateView(false);
    }


    /**
     * receives updates for the @{@link UserInfo}
     *
     * @param u   @{@link UserInfo}
     * @param evt @{@link User.Event}
     */
    public void update(UserInfo u, User.Event evt) {
        if (u.joinTime() != this.currentSessionTime)
            return;
        this.user = u;


        if (evt == null) {
            this.serverWaiter.notify(false);
            return;
        }

        switch (evt) {
            case GAME_JOINED, GAME_CREATED -> {
                this.printCommandLine(u.eventMessage(), GREEN);
                this.serverWaiter.notify(false);
            }
            case ERROR_REPORTED -> {
                this.printCommandLine(u.eventMessage(), RED);
                this.serverWaiter.notify(true);
            }
            case GAME_LEAVED -> this.printCommandLine(u.eventMessage(), RED);
        }
    }

    /**
     * receives updates for the @{@link PlayerInfo}
     *
     * @param p   @{@link PlayerInfo}
     * @param evt @{@link Player.Event}
     */
    public void update(PlayerInfo p, Player.Event evt) {
        if (evt == null) {
            return;
        }
        switch (evt) {
            case ERROR_REPORTED -> this.printCommandLine(p.errorMessage(), RED);
            case COMMONGOAL_ACHIEVED -> {
                this.achievedCommonGoals = p.achievedCommonGoals();
                this.updateView(false);
            }
        }
    }

    private enum View {
        SERVER_INTERACTION,
        GAME_INTERACTION,
        GAME_ENDED
    }

    record Pair(String string, int colour) {
    }
}