package view;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.StandardMessage;
import model.Tile;
import model.User;
import model.abstractModel.Game;
import model.abstractModel.GamesManager;
import model.abstractModel.Message;
import model.abstractModel.Player;
import modelView.*;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CLI {
    static final PrintStream out = System.out;
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;
    private String thisPlayerId;
    private final Map<String, ShelfInfo> currentShelfs;
    static final int renderHeight = 50;
    static final int renderWidth = 150;
    final char[][] cliPixel = new char[renderHeight][renderWidth];
    final int[][] cliPixelColor = new int[renderHeight][renderWidth];
    private final List<CommonGoalInfo> availableCommonGoals = new ArrayList<>();
    private final List<CommonGoalInfo> achievedCommonGoals = new ArrayList<>();
    private GameInfo currentGameState;
    private UserInfo user;
    private boolean GameRunning;
    private final Object lock = new Object();
    private final Scanner scanner = new Scanner(System.in);
    private final List<GamesManagerInfo> games = new ArrayList<>();
    private boolean error = false;
    //true if the user is logging in
    private boolean login = false;
    private final List<PersonalGoalInfo> currentPersonalGoals = new ArrayList<>();
    public static final int WHITE = 37;
    public static final int GREEN = 32;
    public static final int YELLOW = 33;
    public static final int BLUE = 34;
    public static final int MAGENTA = 35;
    public static final int CYAN = 36;
    public static final int RED = 31;
    public static final int DEFAULT = 39;

    public CLI() {
        currentShelfs = new HashMap<>();
        drawBox(0, 0, renderHeight, renderWidth, DEFAULT);
        drawCommandLine();
        render();
    }

    public void runLoginView(ClientInterface client, ServerInterface server) throws RemoteException {
        boolean exit = false;
        login = true;
        while (!exit) {
            String readLine = readCommandLine("(h for commands)-> ");
            render();
            switch (readLine) {
                case "h" -> {
                    printCommandLine("1: Create a game");
                    printCommandLine("2: Join a game");
                    printCommandLine("exit: Close this window");
                    render();
                }
                case "1" -> createGame(client, server);
                case "2" -> joinGame(client, server);
                case "exit" -> exit = true;
                default -> {
                    printCommandLine("Wrong command", RED);
                    render();
                }
            }
        }
    }

    private synchronized void createGame(ClientInterface client, ServerInterface server) throws RemoteException {

        String gameId = readCommandLine("GameId: ");
        render();
        String p = readCommandLine("PlayerNumber (between 2 and 4): ");
        render();
        int k = Integer.parseInt(p);
        if (k > 1 && k < 5)
            server.createGame(client, new NewGameInfo(gameId, "STANDARD", k));
        else {
            printCommandLine("Wrong parameters (number between 2 and 4)", RED);
            render();
        }
    }

    private void joinGame(ClientInterface client, ServerInterface server) throws RemoteException {
        error = false;
        String gameId;
        String playerId;
        gameId = readCommandLine("GameId: ");
        render();
        playerId = readCommandLine("Write playerId (empty to exit): ");
        render();
        if (!playerId.equals(""))
            server.joinGame(client, new LoginInfo(playerId, gameId));
        //timeout 5 seconds

        synchronized (lock) {
            while ((user == null || user.status() != User.Status.JOINED) && !error) {
                try {

                    lock.wait(5000);
                    error = true;
                    //TODO server message to notify the abort
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!error) {
            this.thisPlayerId = playerId;
            login = false;
            gameRoutine(this, client, server, playerId);
        }
    }

    private void gameRoutine(CLI cli, ClientInterface client, ServerInterface sInt, String playerId) throws RemoteException {
        this.GameRunning = true;
        while (this.GameRunning) {
            String readLine = readCommandLine("(h for commands)-> ");
            render();
            switch (readLine) {
                case "h" -> {
                    cli.printCommandLine("1: Update view status");
                    cli.printCommandLine("2: Pick tiles");
                    cli.printCommandLine("3: Send message");
                    render();
                }
                case "1" -> {//extract this in a method
                    ClearScreen();
                    drawBox(0, 0, renderHeight, renderWidth, DEFAULT);
                    drawCommandLine();
                    drawGameState();
                    drawBoard();
                    drawShelfs();
                    drawChat();
                    drawCommonGoals();
                    drawPersonalGoal();
                    render();
                }
                case "2" -> pickTiles(cli, client, sInt);
                case "3" -> {
                    String subject;
                    String content;
                    synchronized (this) {
                        subject = readCommandLine("Message Subject (empty for everyone): ");
                        render();
                        content = readCommandLine("Message content: ");
                        render();
                    }
                    sInt.sendMessage(client, new StandardMessage(playerId, subject, content));
                }
                default -> {
                    printCommandLine("Wrong command", RED);
                    render();
                }
            }
        }
    }

    private void pickTiles(CLI cli, ClientInterface client, ServerInterface sInt) throws RemoteException {
        final int pickableNum = 3;
        List<PickedTile> tiles = new ArrayList<>();
        boolean choosing = true;
        synchronized (this) {
            do {
                cli.printCommandLine("Remaining pickable tiles: " + pickableNum);
                cli.printCommandLine("Write row,col r2,c2 to pick up to three tiles");
                render();
                String choice = readCommandLine("-> ");
                render();
                String[] split = choice.split(" ");
                //if more than 3 tiles are picked ignores the rest
                List<PickedTile> tTiles = new ArrayList<>();
                for (int i = 0; i < split.length && i < pickableNum; i++) {
                    try {
                        String[] split1 = split[i].split(",");
                        int x = Integer.parseInt(split1[0]);
                        int y = Integer.parseInt(split1[1]);
                        tTiles.add(new PickedTile(x, y));
                        printCommandLine("Tile " + x + "," + y + " picked", GREEN);
                        render();
                    } catch (NumberFormatException e) {
                        printCommandLine("Illegal character", RED);
                        render();
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        printCommandLine("Wrong format, Should be r1,c1 r2,c2 r3,c3", RED);
                        render();
                        break;
                    }
                }
                //check if the tiles are pickable, pickable if they are all in the same row or column and adiacent to each other
                boolean pickable = isPickable(tTiles, currentLivingRoom.board());
                if (pickable) {
                    tiles.addAll(tTiles);
                    choosing = false;
                } else {
                    printCommandLine("Tiles not pickable", RED);
                    render();
                }
            } while (choosing);
        }
        choosing = true;
        int sC = 0;
        synchronized (this) {
            do {
                try {
                    String sCol = readCommandLine("Shelf col: ");
                    render();
                    sC = Integer.parseInt(sCol);
                    printCommandLine("Shelf " + sC + " chosen", GREEN);
                    //check if column has enough space for the tiles
                Tile[][] myShelf = currentShelfs.get(thisPlayerId).shelf();
                    int finalSC = sC;
                    // count empty tiles in the column sC
                    int emptyTiles = Arrays.stream(myShelf).mapToInt(row -> row[finalSC] == Tile.EMPTY ? 1 : 0).sum();//TODO test this
                    if (emptyTiles < tiles.size()) {
                        printCommandLine("Not enough space in the shelf", RED);
                        render();
                        continue;
                    }
                    choosing = false;
                    render();
                } catch (NumberFormatException e) {
                    printCommandLine("Not a number", RED);
                    render();
                }
            } while (choosing);
        }
        sInt.doPlayerMove(client, new PlayerMoveInfo(tiles, sC));
    }

    private boolean isPickable(List<PickedTile> pickedTiles, Tile[][] board) {//TODO test this extensively
        // Check if all picked tiles are adjacent to an empty tile
        boolean adjacentToEmptyTile = false;
        for (PickedTile pickedTile : pickedTiles) {
            int row = pickedTile.row();
            int col = pickedTile.col();
            if (row > 0 && board[row-1][col] == Tile.EMPTY) {
                adjacentToEmptyTile = true;
                break;
            }
            if (row < board.length-1 && board[row+1][col] == Tile.EMPTY) {
                adjacentToEmptyTile = true;
                break;
            }
            if (col > 0 && board[row][col-1] == Tile.EMPTY) {
                adjacentToEmptyTile = true;
                break;
            }
            if (col < board[0].length-1 && board[row][col+1] == Tile.EMPTY) {
                adjacentToEmptyTile = true;
                break;
            }
        }
        if (!adjacentToEmptyTile) {
            return false;
        }

        // Check if picked tiles are all in the same row or column
        int firstRow = pickedTiles.get(0).row();
        int firstCol = pickedTiles.get(0).col();
        boolean sameRow = true;
        boolean sameCol = true;
        for (PickedTile pickedTile : pickedTiles) {
            if (pickedTile.row() != firstRow) {
                sameRow = false;
            }
            if (pickedTile.col() != firstCol) {
                sameCol = false;
            }
            if (!sameRow && !sameCol) {
                return false;
            }
        }

        return true;
    }

    public void updateLivingRoom(LivingRoomInfo lR) {
        //set current living room
        this.currentLivingRoom = lR;
        drawBoard();

        render();
    }

    public void updateShelf(ShelfInfo sV) {
        //set current shelf

        currentShelfs.put(sV.playerId(), sV);

        drawShelfs();

        render();
    }

    public void updatePlayerChat(PlayerChatInfo pC) {
        //set current player chat
        this.currentPlayerChat = pC;
        drawChat();

        render();
    }


    private String renderPixel(int x, int y) {
        return "\u001B[" + cliPixelColor[x][y] + "m" + cliPixel[x][y] + "\u001B[0m";
    }

    private void ClearScreen() {

        try {
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor(10, TimeUnit.MILLISECONDS);
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor(10, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            printCommandLine("Error: " + e.getMessage(), RED);
        }
    }

    private void drawChat() {
        List<Message> messages = currentPlayerChat.messages();
        int startMessage = messages.size() - 5;
        if (startMessage < 0) {
            startMessage = 0;
        }

        List<Message> messagesToDraw = messages.subList(startMessage, messages.size());
        //reverse the list
        Collections.reverse(messagesToDraw);
        int currentLine = 0;
        for (Message rawMessage : messagesToDraw) {
            String idSender = rawMessage.getSender();
            String message = rawMessage.getText();
            String idSubject = rawMessage.getSubject();
            //crop
            if (idSender.length() > 10) {
                idSender = idSender.substring(0, 10);
            }
            //concatenate
            StringBuilder toDraw = new StringBuilder(idSender + " to " + ((idSubject.isBlank()) ? "Everyone" : idSubject) + ": " + message);
            //if subject and sender is not idPlayer color it in red
            int chatsize = 70;
            //extend the string to 30
            while (toDraw.length() < chatsize) {
                toDraw.append(" ");
            }
            //if too long  subdivide it in multiple lines
            List<String> lines = new ArrayList<>();
            do {
                lines.add(toDraw.substring(0, chatsize));
                toDraw = new StringBuilder(toDraw.substring(chatsize));
            } while (toDraw.length() > chatsize);
            if (toDraw.length() > 0) {
                lines.add(toDraw.toString());
            }
            Collections.reverse(lines);
            for (String line : lines) {
                drawString(line, renderHeight - 2 - currentLine, renderWidth - 2 - chatsize, DEFAULT, chatsize);
                currentLine++;
            }
        }
    }

    private void drawBoard() {
        final String topLeft = "┌───";
        final String topCenter = "┬───";
        final String topRight = "┬───┐";
        final String centerLeft = "├───";
        final String centerCenter = "┼───";
        final String centerRight = "┼───┤";
        Tile[][] board = currentLivingRoom.board();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                String tile = board[i][j].getColor();
                int colour = getColour(tile);
                String first = "";//first part of the block
                if (i == 0 && j == 0) {//tor right
                    first = topLeft;
                } else if (i == 0 && j != (board[0].length - 1)) {//top center
                    first = topCenter;
                } else if (i == 0 && j == (board[0].length - 1)) {
                    first = topRight;
                } else if (i != 0 && j == 0) {
                    first = centerLeft;
                } else if (i != 0 && j != (board[0].length - 1)) {
                    first = centerCenter;
                } else if (i != 0 && j == (board[0].length - 1)) {
                    first = centerRight;
                }
                String second = "│   ";
                if (!tile.equals("Empty"))
                    second = "│███";
                if (j == board[0].length - 1)//if last column add right border
                {
                    second += "│";
                }
                for (int c = 0; c < first.length(); c++) {
                    cliPixel[1 + 1 + i * 2][2 + 1 + j * 4 + c] = first.charAt(c);
                    cliPixelColor[1 + 1 + i * 2][2 + 1 + j * 4 + c] = DEFAULT;
                }
                for (int c = 0; c < second.length(); c++) {
                    cliPixel[1 + 2 + i * 2][2 + 1 + j * 4 + c] = second.charAt(c);
                    cliPixelColor[1 + 2 + i * 2][2 + 1 + j * 4 + c] = colour;
                }
                cliPixelColor[1 + 2 + i * 2][2 + 1 + j * 4] = DEFAULT;
                cliPixel[1 + board.length * 2 + 1][2 + j * 4 + 1] = '┴';
                cliPixel[1 + board.length * 2 + 1][2 + j * 4 + 2] = '─';
                cliPixel[1 + board.length * 2 + 1][2 + j * 4 + 3] = '─';
                cliPixel[1 + board.length * 2 + 1][2 + j * 4 + 4] = '─';
            }
        }
        //draw numbers on the top
        for (int i = 0; i < board.length; i++) {
            String number = String.valueOf(i);
            if (i < 10) {
                number = "  " + number + " ";
            } else {
                number = " " + number + " ";
            }
            for (int c = 0; c < number.length(); c++) {
                cliPixel[1][3 + i * 4 + c] = number.charAt(c);
                cliPixelColor[1][3 + i * 4 + c] = DEFAULT;
            }
        }
        //draw numbers on the side
        for (int i = 0; i < board.length; i++) {
            String number = String.valueOf(i);
            if (i < 10) {
                number = "0" + number;
            }
            for (int c = 0; c < number.length(); c++) {
                cliPixel[3 + i * 2][1] = number.charAt(c);
                cliPixelColor[3 + i * 2][1] = DEFAULT;
            }

        }
        cliPixel[1 + board.length * 2 + 1][3] = '└';
        cliPixel[1 + board.length * 2 + 1][2 + board[0].length * 4 + 1] = '┘';
    }


    private void drawShelfs() {

        final String tops = "    ";
        final String centerLeft = "├───";
        final String centerCenter = "┼───";
        final String centerRight = "┼───┤";

        int margin = 2 + 1 + 4 * currentLivingRoom.board()[0].length + 1 + 1;

        for (int s = 0; s < currentShelfs.size(); s++) {
            Tile[][] shelf = currentShelfs.values().stream().toList().get(s).shelf();
            if (shelf == null)
                continue;
            int start = margin + s * shelf[0].length * 4 + s;
            for (int i = 0; i < shelf.length; i++) {
                for (int j = 0; j < shelf[0].length; j++) {


                    int colour = getColour(shelf[i][j].getColor());
                    String first = "";
                    if (i == 0) {//tor right
                        first = tops;
                    } else if (j == 0) {
                        first = centerLeft;
                    } else if (j != (shelf[0].length - 1)) {
                        first = centerCenter;
                    } else if (j == (shelf[0].length - 1)) {
                        first = centerRight;
                    }
                    String second = "│   ";
                    if (colour != DEFAULT)
                        second = "│███";

                    if (j == shelf[0].length - 1)
                        second += "│";

                    for (int c = 0; c < first.length(); c++) {
                        cliPixel[1 + i * 2][start + 1 + j * 4 + c] = first.charAt(c);
                        cliPixelColor[1 + i * 2][start + 1 + j * 4 + c] = DEFAULT;
                    }
                    for (int c = 0; c < second.length(); c++) {
                        cliPixel[2 + i * 2][start + 1 + j * 4 + c] = second.charAt(c);
                        cliPixelColor[2 + i * 2][start + 1 + j * 4 + c] = colour;
                    }
                    cliPixelColor[2 + i * 2][start + 1 + j * 4] = DEFAULT;
                    cliPixel[shelf.length * 2 + 1][start + j * 4 + 1] = '┴';
                    cliPixel[shelf.length * 2 + 1][start + j * 4 + 2] = '─';
                    cliPixel[shelf.length * 2 + 1][start + j * 4 + 3] = '─';
                    cliPixel[shelf.length * 2 + 1][start + j * 4 + 4] = '─';
                }
            }
            cliPixel[shelf.length * 2 + 1][start + 1] = '└';
            cliPixel[shelf.length * 2 + 1][start + shelf[0].length * 4 + 1] = '┘';
        }
        for (int i = 0; i < (currentShelfs.values().stream().toList().get(0)).shelf()[0].length + 4 + 5; i++)
            cliPixel[1 + currentShelfs.values().stream().toList().get(0).shelf().length * 2 + 1][margin + i] = ' ';
        //draw under each shelf you if you are in that shelf or the number of player in the shelf
        for (int i = 0; i < currentShelfs.size(); i++) {
            ShelfInfo shelf = currentShelfs.values().stream().toList().get(i);
            Tile[][] shelfTile = shelf.shelf();

            String toDraw;
            if (shelf.playerId().equals(thisPlayerId)) {
                toDraw = "YOU";
            } else {
                toDraw = shelf.playerId();

            }
            if (currentGameState != null && currentGameState.playerOnTurn().equals(shelf.playerId())) {
                toDraw = ">" + toDraw + "<";
            } else {
                toDraw = " " + toDraw + " ";
            }
            if (currentGameState != null) {
                int points = currentGameState.points().getOrDefault(shelf.playerId(), 0);
                toDraw += ":" + points + " ";
            }
            drawString(toDraw, 1 + shelfTile.length * 2 + 1, margin + i * (4 * shelfTile[0].length + 1) + 1, DEFAULT, shelfTile[0].length * 4 + 1 - 2);


        }
    }

    //draw String from start coordinate
    private void drawString(String toDraw, int Row, int startCol, int colour, int size) {
        if (toDraw.length() > size)
            toDraw = toDraw.substring(0, size);

        for (int i = 0; (i < toDraw.length() && (i + startCol) < (renderWidth - 2)); i++) {
            cliPixel[Row][startCol + i] = toDraw.charAt(i);
            cliPixelColor[Row][startCol + i] = colour;
        }
    }

    //move cursor to arbitrary position
    public static void moveCursor(int x, int y) {
        System.out.print("\033[" + x + ";" + y + "H");
    }

    @SuppressWarnings("SameParameterValue")
    private void drawBox(int x, int y, int height, int width, int colour) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) cliPixel[x + i][y + j] = '┌';
                else if (i == 0 && j == width - 1) cliPixel[x + i][y + j] = '┐';
                else if (i == height - 1 && j == 0) {
                    cliPixel[x + i][y + j] = '└';
                    cliPixelColor[x + i][y + j] = colour;
                } else if (i == height - 1 && j == width - 1) {
                    cliPixel[x + i][y + j] = '┘';
                    cliPixelColor[x + i][y + j] = colour;
                } else if (i == 0) {
                    cliPixel[x + i][y + j] = '─';
                    cliPixelColor[x + i][y + j] = colour;
                } else if (i == height - 1) {
                    cliPixel[x + i][y + j] = '─';
                    cliPixelColor[x + i][y + j] = colour;
                } else if (j == 0) {
                    cliPixel[x + i][y + j] = '│';
                    cliPixelColor[x + i][y + j] = colour;
                } else if (j == width - 1) {
                    cliPixel[x + i][y + j] = '│';
                    cliPixelColor[x + i][y + j] = colour;
                } else {
                    cliPixel[x + i][y + j] = ' ';
                    cliPixelColor[x + i][y + j] = DEFAULT;
                }
            }
        }
    }

    private void drawCommandLine() {
        drawBox(renderHeight - 11, 2, 10, 50, DEFAULT);
        drawString("Command Line", renderHeight - 10, 3, DEFAULT, 50 - 2);
        drawString(">", renderHeight - 3, 3, DEFAULT, 50 - 2);
        drawOldCmds();
    }

    //old cmds to be shifted up
    final List<Pair> oldCmds = new ArrayList<>();

    private void drawOldCmds() {
        while (oldCmds.size() > 5)
            oldCmds.remove(0);
        for (int i = 0; i < oldCmds.size(); i++) {
            drawString(oldCmds.get(i).string(), renderHeight - 9 + i, 3, oldCmds.get(i).colour(), 50 - 2);
        }
    }

    public String readCommandLine(String message) {
        moveCursor(renderHeight - 2, 4);
        System.out.print(message + " ");
        String cmd = scanner.nextLine();
        oldCmds.add(new Pair(message + " " + cmd, DEFAULT));
        //trim old cmds to 8
        while (oldCmds.size() > 8)
            oldCmds.remove(0);
        drawCommandLine();
        //move cursor back to command line
        moveCursor(renderHeight - 2, 5);
        return cmd;

    }

    public void printCommandLine(String toPrint) {
        oldCmds.add(new Pair(toPrint, DEFAULT));
        while (oldCmds.size() > 8)
            oldCmds.remove(0);
        drawCommandLine();
        moveCursor(renderHeight - 2, 5);
    }

    public void printCommandLine(String toPrint, int colour) {
        oldCmds.add(new Pair(toPrint, colour));
        while (oldCmds.size() > 8)
            oldCmds.remove(0);
        drawCommandLine();
        moveCursor(renderHeight - 2, 5);
    }

    public synchronized void render() {
        ClearScreen();
        if (currentGameState != null) {
            drawGameState();
        }
        for (int i = 0; i < cliPixel.length; i++) {
            for (int j = 0; j < cliPixel[0].length; j++) {
                out.print(renderPixel(i, j));
            }
            out.println();
        }
        moveCursor(renderHeight - 2, 5);
    }

    public void updateCommonGoal(CommonGoalInfo o) {

        boolean present = availableCommonGoals.stream().anyMatch(commonGoalInfo -> commonGoalInfo.description().equals(o.description())) || achievedCommonGoals.stream().anyMatch(commonGoalInfo -> commonGoalInfo.description().equals(o.description()));

        if (!present)
            availableCommonGoals.add(o);
        else {
            CommonGoalInfo o1 = (CommonGoalInfo) availableCommonGoals.stream().filter(commonGoalInfo -> commonGoalInfo.description().equals(o.description())).toArray()[0];
            availableCommonGoals.set(availableCommonGoals.indexOf(o1), o);
        }
        drawCommonGoals();
        render();
    }


    private void drawCommonGoals() {//TODO: hardCode presentation based on description

        Tile[][] twoColumns = new Tile[2][];


        int i = 0;
        //draw: AVAILABLE COMMON GOALS
        //drawString("Available Common Goals", renderHeight-90-i, renderWidth-60, DEFAULT, 50 - 2);
        for (CommonGoalInfo c : availableCommonGoals) {
            // c.description()  points: c.Token().points()

            drawString(c.description() + " points: " + c.tokenState().getPoints(), renderHeight - 30 - i, 40, DEFAULT, 110 - 2);
            i++;
        }
        //draw: ACHIEVED COMMON GOALS
        //drawString("Achieved Common Goals", renderHeight-90-i, renderWidth-60, DEFAULT, 50 - 2);
        for (CommonGoalInfo c : achievedCommonGoals) {
            // c.description()  points: c.Token().points()
            drawString(c.description() + " points: " + c.tokenState().getPoints(), renderHeight - 30 - i, 40, DEFAULT, 110 - 2);
            i++;
        }
    }

    public void updateGameState(GameInfo o) {
        currentGameState = o;

        if (o.status()== Game.GameStatus.ENDED){
            printCommandLine("Game Ended, press enter to return to login menu");
            //get max points
            int maxPoints = o.points().values().stream().max(Integer::compareTo).get();
            //for each player print points and the winner
            for (var entry : o.points().entrySet()){
                if (entry.getValue() == maxPoints)
                    printCommandLine(entry.getKey() + " has " + entry.getValue() + " points and is a winner");
                else
                    printCommandLine(entry.getKey() + " has " + entry.getValue() + " points");
            }
            GameRunning = false;
            readCommandLine("");
            return;
        }

        drawGameState();
        render();
    }

    private void drawGameState() {
        //redraw shelfs with new current player
        if (currentLivingRoom != null && currentShelfs != null && currentLivingRoom.board() != null && currentLivingRoom.board().length > 0 && currentShelfs.size() > 0 && currentLivingRoom.board()[0].length > 0)
            drawShelfs();
        //draw is last turn if it is
        if (currentGameState.lastTurn())
            drawString("Last Turn", renderHeight - 10, 30, DEFAULT, 50 - 2);
    }

    public void updatePersonalGoal(PersonalGoalInfo o) {
        //check if personal goal is already present in current personal goals
        int index = currentPersonalGoals.stream().map(PersonalGoalInfo::description).toList().indexOf(o.description());

        if (index != -1)
            currentPersonalGoals.set(index, o);
        else
            currentPersonalGoals.add(o);
        drawPersonalGoal();


    }

    private void drawPersonalGoal() {
        Tile[][] shelf = new Tile[6][5];
        Arrays.stream(shelf).forEach(tiles -> Arrays.fill(tiles, Tile.EMPTY));

        for (PersonalGoalInfo c : currentPersonalGoals) {
            for (int i = 0; i < c.description().length; i++) {
                for (int j = 0; j < c.description()[0].length; j++) {
                    if (c.description()[i][j] != Tile.EMPTY)
                        shelf[i][j] = c.description()[i][j];
                }
            }
        }

        final String tops = "    ";
        final String centerLeft = "├───";
        final String centerCenter = "┼───";
        final String centerRight = "┼───┤";
        //draw personal goal under the board
        final int topMargin = currentLivingRoom.board().length * 2 + 4;
        final int leftMargin = 3;
        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[0].length; j++) {
                int colour = getColour(shelf[i][j].getColor());
                String first = "";
                if (i == 0) {//top right
                    first = tops;
                } else if (j == 0) {
                    first = centerLeft;
                } else if (j != (shelf[0].length - 1)) {
                    first = centerCenter;
                } else if (j == (shelf[0].length - 1)) {
                    first = centerRight;
                }
                String second;
                if (colour != DEFAULT)
                    second = "│███";
                else
                    second = "│   ";
                if (j == shelf[0].length - 1)
                    second += "│";

                for (int c = 0; c < first.length(); c++) {
                    cliPixel[topMargin + 1 + i * 2][leftMargin + 1 + j * 4 + c] = first.charAt(c);
                    cliPixelColor[topMargin + 1 + i * 2][leftMargin + 1 + j * 4 + c] = DEFAULT;
                }
                for (int c = 0; c < second.length(); c++) {
                    cliPixel[topMargin + 2 + i * 2][leftMargin + 1 + j * 4 + c] = second.charAt(c);
                    cliPixelColor[topMargin + 2 + i * 2][leftMargin + 1 + j * 4 + c] = colour;
                }
                cliPixelColor[topMargin + 2 + i * 2][leftMargin + 1 + j * 4] = DEFAULT;
                cliPixelColor[topMargin + 2 + i * 2][leftMargin + 1 + j * 4 + 4] = DEFAULT;
                cliPixel[topMargin + shelf.length * 2 + 1][leftMargin + j * 4 + 1] = '┴';
                cliPixel[topMargin + shelf.length * 2 + 1][leftMargin + j * 4 + 2] = '─';
                cliPixel[topMargin + shelf.length * 2 + 1][leftMargin + j * 4 + 3] = '─';
                cliPixel[topMargin + shelf.length * 2 + 1][leftMargin + j * 4 + 4] = '─';
            }
        }
        cliPixel[topMargin + shelf.length * 2 + 1][leftMargin + 1] = '└';
        cliPixel[topMargin + shelf.length * 2 + 1][leftMargin + shelf[0].length * 4 + 1] = '┘';


    }

    private static int getColour(String color) {
        int colour;
        switch (color) {
            case "Green" -> colour = GREEN;
            case "White" -> colour = WHITE;
            case "Yellow" -> colour = YELLOW;
            case "Blue" -> colour = BLUE;
            case "LightBlue" -> colour = CYAN;
            case "Magenta" -> colour = MAGENTA;
            default -> colour = DEFAULT;
        }
        return colour;
    }

    public void updateUserInfo(UserInfo o, User.Event event) {
        if (event == User.Event.STATUS_CHANGED) {
            user = o;
        } else if (event == User.Event.ERROR_REPORTED) {
            printCommandLine(o.errorMessage(), RED);
            error = true;

        }
        synchronized (lock) {
            lock.notify();
        }
        render();
    }

    public void updateGamesManager(GamesManagerInfo o, GamesManager.Event evt) {
        switch (evt) {
            case GAME_CREATED -> games.add(o);
            case GAME_REMOVED -> games.remove(o);
        }
        if (login)
            drawGameList();

    }

    private void drawGameList() {
        // a game will be | game name | max players | current players | joinable |
        // a game will be | 10 chars   | 5 chars      | 5 chars          | 10 chars |
        for (int i = 0; i < games.size(); i++) {
            String toDraw = games.get(i).gameId();
            toDraw += " ".repeat(10 - toDraw.length());
            toDraw += games.get(i).maxPlayers();
            toDraw += " ".repeat(5 - toDraw.length() + 10);
            toDraw += games.get(i).connectedPlayers();
            toDraw += " ".repeat(5 - toDraw.length() + 15);
            if (games.get(i).connectedPlayers() < games.get(i).maxPlayers())
                toDraw += "joinable";
            else
                toDraw += "not joinable";
            toDraw += " ".repeat(10 - toDraw.length() + 25);
            drawString(toDraw, 10 + i, 10, DEFAULT, 50 - 2);

        }
    }

    public void updatePlayerInfo(PlayerInfo o, Player.Event evt) {
        if (evt == null) {
            if (!login)
                drawCommonGoals();

            return;
        }
        switch (evt) {
            case ERROR_REPORTED -> {
                printCommandLine(o.errorMessage(), RED);
                render();
                return;
            }
            case COMMONGOAL_ACHIEVED -> {
                //find if a common goal in o.achievedCommonGoals() is in availableCommonGoals
                for (CommonGoalInfo commonGoalInfo : availableCommonGoals) {
                    if (o.achievedCommonGoals().getOrDefault(commonGoalInfo.description(), null) != null) {
                        //remove it from availableCommonGoals
                        availableCommonGoals.remove(commonGoalInfo);
                        //add it to the achieved list
                        achievedCommonGoals.add(commonGoalInfo);
                    }
                }

            }
        }
        if (!login)
            drawCommonGoals();
    }

    private record Pair(String string, int colour) {
    }
}