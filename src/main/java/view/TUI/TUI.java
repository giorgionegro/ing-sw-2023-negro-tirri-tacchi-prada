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
    static final int commandLineX = 1;
    static int commandLineY = 41;
    static int commandLineHeight = 10;
    final char[][] cliPixel = new char[renderHeight][renderWidth];
    final int[][] cliPixelColor = new int[renderHeight][renderWidth];
    //old cmds to be shifted up
    final List<Pair> oldCmds = new ArrayList<>();

    /*----------------------------------------*/

    /*-----------------------------------------*/

    private final List<PersonalGoalInfo> currentPersonalGoals = new ArrayList<>();
    private final List<GamesManagerInfo> games = new ArrayList<>();
    private final Map<String, CommonGoalInfo> commonGoals = new HashMap<>();

    /*------------------------------------------*/

    /*--------SERVER INTERACTION FUNCTIONS--------*/
    private final Map<String, ShelfInfo> currentShelves;
    /*------------VIEW UTILITIES---------------*/
    final private TimedLock<Boolean> serverWaiter = new TimedLock<>(false);
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
        currentView = View.SERVER_INTERACTION;
        currentShelves = new HashMap<>();
        drawBox(0, 0, renderHeight, renderWidth, DEFAULT, cliPixel, cliPixelColor);
        drawCommandLine(cursor, oldCmds, cliPixel, cliPixelColor);
        updateView(false);
    }

    //move cursor to arbitrary position
    public static void moveCursor(int y, int x) {
        System.out.print("\033[" + y + ";" + x + "H");
    }


    /*-------------UI--------------------------*/
    public String askRMIorSocket() {
        return readCommandLine("Connect with RMI (r) or SOCKET (s)?, empty to exit: ");
    }

    public void showError(String error) {
        printCommandLine(error, RED);
    }

    public void run(ServerInterface server, ClientInterface client) {
        this.server = server;
        this.client = client;

        this.currentView = View.SERVER_INTERACTION;

        if (!serverWaiter.hasBeenNotified()) {
            try {
                serverWaiter.setValue(true);
                serverWaiter.lock(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Boolean exit = serverWaiter.getValue();

        serverWaiter.reset();

        if (!exit) {
            printCommandLine("CONNECTED", GREEN);
        }

        while (!exit) {
            String readLine = readCommandLine("(h for commands)-> ");
            switch (readLine) {
                case "h" -> printCommandLine("1: Create a game\n2: Join a game\nexit: Close this window");
                case "1" -> {
                    try {
                        createGame();
                    } catch (RemoteException e) {
                        printCommandLine(e.getMessage(), RED);
                    }
                }
                case "2" -> {
                    try {
                        joinGame();
                    } catch (RemoteException e) {
                        printCommandLine("Error while joining a game", RED);
                    }
                }
                case "exit" -> exit = true;
                default -> printCommandLine("Wrong command", RED);

            }
        }
    }

    private void createGame() throws RemoteException {
        String gameId = readCommandLine("GameId: ");
        String p = readCommandLine("PlayerNumber (between 2 and 4): ");
        int k = Integer.parseInt(p);
        if (k > 1 && k < 5) {

            currentSessionTime = System.currentTimeMillis();
            serverWaiter.reset();
            server.createGame(client, new NewGameInfo(gameId, "STANDARD", k, currentSessionTime));

            if (!serverWaiter.hasBeenNotified()) {
                serverWaiter.setValue(true);
                try {
                    serverWaiter.lock(6000);
                } catch (InterruptedException e) {
                    throw new RemoteException("Connection timeout error");
                }
            }

            if (serverWaiter.getValue())
                throw new RemoteException(user.eventMessage());
        } else {
            throw new RemoteException("Wrong parameters (number between 2 and 4)");
        }
    }

    private void joinGame() throws RemoteException {
        String gameId = readCommandLine("GameId: ");
        String playerId = readCommandLine("Write playerId (empty to exit): ");
        if (!playerId.equals("")) {
            serverWaiter.reset();
            currentSessionTime = System.currentTimeMillis();
            server.joinGame(client, new LoginInfo(playerId, gameId, currentSessionTime));
        } else
            return;

        if (!serverWaiter.hasBeenNotified()) {
            try {
                serverWaiter.setValue(true);
                serverWaiter.lock(6000);
            } catch (InterruptedException e) {
                throw new RemoteException("Login timeout error");
            }
        }

        if (!serverWaiter.getValue()) {
            this.thisPlayerId = playerId;
            gameRoutine();
        }
    }

    private void gameRoutine() throws RemoteException {
        currentView = View.GAME_INTERACTION;
        updateView(true);
        GameRunning = true;
        while (this.GameRunning) {
            String readLine = readCommandLine("(h for commands)-> ");
            if (this.GameRunning)
                switch (readLine) {
                    case "h" -> printCommandLine("1: Update view status\n2: Pick tiles\n3: Send message");
                    case "1" -> updateView(false);
                    case "2" -> pickTiles();
                    case "3" -> sendMessage();
                    case "4" -> leave();
                    default -> printCommandLine("Wrong command", RED);
                }
        }
        resetInfo();
        currentView = View.SERVER_INTERACTION;
        updateView(true);
    }

    /*--------------------------------------------------*/

    public void resetInfo() {
        currentPersonalGoals.clear();
        currentGameState = null;
        commonGoals.clear();
        achievedCommonGoals.clear();
        currentShelves.clear();
        currentLivingRoom = null;
        currentPlayerChat = null;
        thisPlayerId = null;
    }


    private void sendMessage() throws RemoteException {

        viewLock = true;

        String subject = readCommandLine("Message Subject (empty for everyone): ");
        String content = readCommandLine("Message content: ");
        viewLock = false;

        server.sendMessage(client, new StandardMessage(thisPlayerId, subject, content));

    }

    private void pickTiles() throws RemoteException {
        final int pickableNum = 3;
        List<PickedTile> tiles = new ArrayList<>();
        boolean choosing = true;
        do {
            viewLock = true;
            printCommandLine("Remaining pickable tiles: " + pickableNum + "\nWrite row,col r2,c2 to pick up to three tiles");
            viewLock = false;
            String choice = readCommandLine("-> ");
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
                    printCommandLine("Tile " + x + "," + y + " picked", GREEN);
                } catch (NumberFormatException e) {
                    printCommandLine("Illegal character", RED);
                    formatError = true;
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    printCommandLine("Wrong format, Should be r1,c1 r2,c2 r3,c3", RED);
                    formatError = true;
                    break;
                }
            }
            if (formatError)
                continue;
            //check if the tiles are pick-able, pick-able if they are all in the same row or column and adjacent to each other
            boolean pickable = pickable(tTiles, currentLivingRoom.board());
            if (pickable) {
                tiles.addAll(tTiles);
                choosing = false;
            } else {
                printCommandLine("Tiles not pickable", RED);
            }
        } while (choosing);

        choosing = true;
        int sC = 0;
        do {
            try {
                viewLock = true;
                String sCol = readCommandLine("Shelf col: ");
                viewLock = false;
                sC = Integer.parseInt(sCol);
                printCommandLine("Shelf " + sC + " chosen", GREEN);
                //check if column has enough space for the tiles
                Tile[][] myShelf = currentShelves.get(thisPlayerId).shelf();
                int finalSC = sC;
                // count empty tiles in the column sC
                int emptyTiles = Arrays.stream(myShelf).mapToInt(row -> row[finalSC] == Tile.EMPTY ? 1 : 0).sum();//TODO test this
                if (emptyTiles < tiles.size()) {
                    printCommandLine("Not enough space in the shelf", RED);
                    continue;
                }
                choosing = false;
            } catch (NumberFormatException e) {
                printCommandLine("Not a number", RED);
            }
        } while (choosing);

        server.doPlayerMove(client, new PlayerMoveInfo(tiles, sC));
    }

    private boolean pickable(List<PickedTile> pickedTiles, Tile[][] board) {
        if (!areTilesDifferent(new ArrayList<>(pickedTiles))) {
            printCommandLine("Tiles are not different", RED);
            return false;
        }
        if (!areTilesAligned(new ArrayList<>(pickedTiles))) {
            printCommandLine("Tiles are not aligned", RED);
            return false;
        }
        for (PickedTile tile : pickedTiles)
            if (!isTilePickable(tile.row(), tile.col(), board)) {
                printCommandLine("Tile not pickable", RED);
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
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column].equals(Tile.EMPTY) || board[row][column] == null)
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
            server.leaveGame(client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateView(boolean force) {
        if (!viewLock || force) {
            //clear the matrix
            Arrays.stream(cliPixel).forEach(a -> Arrays.fill(a, ' '));
            Arrays.stream(cliPixelColor).forEach(a -> Arrays.fill(a, DEFAULT));

            drawBox(0, 0, renderHeight, renderWidth, DEFAULT, cliPixel, cliPixelColor);
            drawCommandLine(cursor, oldCmds, cliPixel, cliPixelColor);

            switch (currentView) {
                case GAME_INTERACTION -> {
                    drawCommandLine(cursor, oldCmds, cliPixel, cliPixelColor);
                    drawGameState(currentGameState, cliPixel, cliPixelColor);
                    drawLivingRoom(currentLivingRoom, cliPixel, cliPixelColor);
                    drawShelves(currentShelves, thisPlayerId, currentGameState, cliPixel, cliPixelColor);
                    drawChat(currentPlayerChat, cliPixel, cliPixelColor);
                    drawCommonGoals(commonGoals, achievedCommonGoals, cliPixel, cliPixelColor);
                    drawPersonalGoal(currentPersonalGoals, cliPixel, cliPixelColor);
                }
                case GAME_ENDED -> drawGameEnd(points, cliPixel, cliPixelColor);
            }

            updated++;
            drawString(updated + " ", 0, 0, GREEN, 20, cliPixel, cliPixelColor);
            render();
        }

    }

    /*--------------------------------------------------*/

    private String renderPixel(int x, int y) {
        return "\u001B[" + cliPixelColor[x][y] + "m" + cliPixel[x][y] + "\u001B[0m";
    }



    /*-------------------------------------------------------------*/


    /*-----------------COMMAND LINE--------------------------------*/


    public String readCommandLine(String message) {
        cursor = message;
        out.print(message);
        String cmd = scanner.nextLine();
        cursor = "";
        oldCmds.add(new Pair(message + " " + cmd, DEFAULT));
        //trim old commands to 8
        while (oldCmds.size() > 8)
            oldCmds.remove(0);

        updateView(true);
        return cmd;
    }

    public void printCommandLine(String toPrint) {
        printCommandLine(toPrint, DEFAULT);
    }

    public void printCommandLine(String toPrint, int colour) {
        String[] lines = toPrint.split("\n");
        for (String s : lines)
            oldCmds.add(new Pair(s, colour));

        while (oldCmds.size() > 8)
            oldCmds.remove(0);

        updateView(true);
    }

    public synchronized void render() {
        ClearScreen(s -> {
            printCommandLine(s, DEFAULT);
            return null;
        });
        if (currentGameState != null) {
            drawGameState(currentGameState, cliPixel, cliPixelColor);
        }
        for (int i = 0; i < cliPixel.length; i++) {
            for (int j = 0; j < cliPixel[0].length; j++) {
                out.print(renderPixel(i, j));
            }
            out.println();
        }
        moveCursor(commandLineY + commandLineHeight, commandLineX + 2 + cursor.length() + 1);
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
        updateView(false);
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
        updateView(false);
    }


    /**
     * receives updates for the @{@link ShelfInfo}
     *
     * @param sV  @{@link ShelfInfo}
     * @param evt @{@link Shelf.Event}
     */
    public void update(ShelfInfo sV, Shelf.Event evt) {
        //set current shelf

        currentShelves.put(sV.playerId(), sV);

        updateView(false);
    }


    /**
     * receives updates for the @{@link CommonGoalInfo}
     *
     * @param cG  @{@link CommonGoalInfo}
     * @param evt @{@link CommonGoal.Event}
     */
    public void update(CommonGoalInfo cG, CommonGoal.Event evt) {
        commonGoals.put(cG.id(), cG);
        updateView(false);
    }


    /**
     * receives updates for the @{@link GameInfo}
     *
     * @param g   @{@link GameInfo}
     * @param evt @{@link Game.Event}
     */
    public void update(GameInfo g, Game.Event evt) {
        currentGameState = g;

        if (g.status() == Game.GameStatus.ENDED) {
            GameRunning = false;
            this.currentView = View.GAME_ENDED;
            points.clear();
            points.putAll(g.points());
            printCommandLine("Game Ended, press enter to return to login menu");
        } else if (g.status() == Game.GameStatus.STARTED) {
            updateView(true);
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
        int index = currentPersonalGoals.stream().map(PersonalGoalInfo::description).toList().indexOf(pG.description());

        if (index != -1)
            currentPersonalGoals.set(index, pG);
        else
            currentPersonalGoals.add(pG);

        updateView(false);
    }


    /**
     * receives updates for the @{@link UserInfo}
     *
     * @param u   @{@link UserInfo}
     * @param evt @{@link User.Event}
     */
    public void update(UserInfo u, User.Event evt) {
        if (u.joinTime() != currentSessionTime)
            return;
        user = u;


        if (evt == null) {
            serverWaiter.notify(false);
            return;
        }

        switch (evt) {
            case GAME_JOINED, GAME_CREATED -> {
                printCommandLine(u.eventMessage(), GREEN);
                serverWaiter.notify(false);
            }
            case ERROR_REPORTED -> {
                printCommandLine(u.eventMessage(), RED);
                serverWaiter.notify(true);
            }
            case GAME_LEAVED -> printCommandLine(u.eventMessage(), RED);
        }
    }


    /**
     * receives updates for the @{@link GamesManagerInfo}
     *
     * @param gM  @{@link GamesManagerInfo}
     * @param evt @{@link GamesManager.Event}
     */
    public void update(GamesManagerInfo gM, GamesManager.Event evt) {
        switch (evt) {
            case GAME_CREATED -> games.add(gM);
            case GAME_REMOVED -> games.remove(gM);
        }
        //drawGameList();
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
            case ERROR_REPORTED -> printCommandLine(p.errorMessage(), RED);
            case COMMONGOAL_ACHIEVED -> {
                achievedCommonGoals = p.achievedCommonGoals();
                updateView(false);
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