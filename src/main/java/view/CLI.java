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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CLI {
    static final PrintStream out = System.out;
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;
    private String thisPlayerId;
    private final Map<String, ShelfInfo> currentShelfs;
    static final int renderHeight = 52;
    static final int renderWidth = 140;
    final char[][] cliPixel = new char[renderHeight][renderWidth];
    final int[][] cliPixelColor = new int[renderHeight][renderWidth];
    private final Map<String,CommonGoalInfo> commonGoals = new HashMap<>();
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
                    drawLivingRoom();
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

    /*----------LIVING ROOM--------------------------*/
    public void updateLivingRoom(LivingRoomInfo lR) {
        //set current living room
        this.currentLivingRoom = lR;
        drawLivingRoom();

        render();
    }
    final int livingRoomX = 1;
    final int livingRoomY = 1;
    private void drawLivingRoom() {
        Tile[][] board = currentLivingRoom.board();
        drawGrid(livingRoomX+2, livingRoomY+2,board[0].length, board.length);
        drawGridContents(livingRoomX+2, livingRoomY+2, board);

        //draw numbers on the top
        for (int i = 0; i < board.length; i++) {
            String number = String.valueOf(i);
            if (i < 10) {
                number = "  " + number + " ";
            } else {
                number = " " + number + " ";
            }
            for (int c = 0; c < number.length(); c++) {
                cliPixel[livingRoomY+1][livingRoomX + 2 + i * 4 + c] = number.charAt(c);
                cliPixelColor[livingRoomY+1][livingRoomX + 2 + i * 4 + c] = DEFAULT;
            }
        }
        //draw numbers on the side
        for (int i = 0; i < board.length; i++) {
            String number = String.valueOf(i);
            if (i < 10) {
                number = "0" + number;
            }
            for (int c = 0; c < number.length(); c++) {
                cliPixel[livingRoomY + 3 + i * 2][livingRoomX] = number.charAt(c);
                cliPixelColor[livingRoomY + 3 + i * 2][livingRoomX] = DEFAULT;
            }

        }

        StringBuilder title = new StringBuilder();
        String t = "LIVING ROOM BOARD";
        int spaceBefore = (board[0].length*4+1 - t.length())/2;
        title.append(" ".repeat(spaceBefore)).append(t);
        drawString(title.toString(),livingRoomY, livingRoomX+2, DEFAULT, title.length());
    }

    /*--------------------------------------------------*/

    /*-------------SHELFS-------------------------------*/
    public void updateShelf(ShelfInfo sV) {
        //set current shelf

        currentShelfs.put(sV.playerId(), sV);

        drawShelfs();

        render();
    }
    final int shelfsX = 43;
    final int shelfsY = 4;
    final int shelfsPadding = 3;
    private void drawShelfs() {

        int shelfsGridY = shelfsY+1;

        int shelfsHeigth = 0;
        int shelfsWidth = 0;

        int shelfDrawed = 0;

        StringBuilder playersName = new StringBuilder();
        StringBuilder playersPoints = new StringBuilder();

        for(String playerId : currentShelfs.keySet()){
            Tile[][] shelf = currentShelfs.get(playerId).shelf();

            shelfsHeigth = shelf.length;
            shelfsWidth = shelf[0].length*4+1;

            int shelfX = shelfsX + (shelfsWidth + shelfsPadding) * shelfDrawed;
            drawGrid( shelfX, shelfsGridY, shelf[0].length, shelf.length);
            drawGridContents(shelfX, shelfsGridY, shelf);

            String tempPlayerId = playerId;

            if(playerId.equals(thisPlayerId))
                tempPlayerId = "YOU";

            if(tempPlayerId.length() > shelfsWidth - 4)
                tempPlayerId = tempPlayerId.substring(0, shelfsWidth - 4);


            if(playerId.equals(currentGameState.playerOnTurn()))
                tempPlayerId = '>' + tempPlayerId + '<';

            int spaceBefore = (shelfsWidth - tempPlayerId.length())/2;
            int spaceAfter = shelfsWidth - spaceBefore - tempPlayerId.length();
            playersName.append(" ".repeat(spaceBefore));
            playersName.append(tempPlayerId);
            playersName.append(" ".repeat(spaceAfter));
            playersName.append(" ".repeat(shelfsPadding));

            String points = "Points: "+currentGameState.points().getOrDefault(playerId,0);
            spaceBefore = (shelfsWidth - points.length())/2;
            spaceAfter = shelfsWidth - spaceBefore - points.length();
            playersPoints.append(" ".repeat(spaceBefore));
            playersPoints.append(points);
            playersPoints.append(" ".repeat(spaceAfter));
            playersPoints.append(" ".repeat(shelfsPadding));

            shelfDrawed++;
        }

        drawString(playersName.toString(), shelfsGridY + shelfsHeigth*2 + 1, shelfsX, DEFAULT, playersName.length());
        drawString(playersPoints.toString(), shelfsGridY + shelfsHeigth*2 + 2, shelfsX, DEFAULT, playersPoints.length());

        StringBuilder title = new StringBuilder();
        String t = "PLAYERS SHELVES";
        int maxSize = currentShelfs.size() * shelfsWidth + shelfsPadding * (currentShelfs.size()-1);
        int spaceBefore = (maxSize - t.length())/2;
        title.append(" ".repeat(spaceBefore)).append(t);
        drawString(title.toString(),shelfsY, shelfsX, DEFAULT, title.length());
    }

    /*--------------AUXILIARY FUNCTIONS-----------------------------*/
    private void drawGrid(int startX, int startY, int gridRowDim, int gridColDim){
        String middle = "│   ".repeat(gridRowDim) + "│";

        for(int i=0; i<gridColDim; i++) {
            String pattern;
            if(i==0)
                pattern = "┬───";
            else
                pattern = "┼───";

            drawString(pattern.repeat(gridRowDim), startY + i*2, startX, DEFAULT,60);
            drawString(middle, startY + i*2 + 1, startX, DEFAULT,60);
        }

        drawString("┴───".repeat(gridRowDim), startY + gridColDim*2, startX, DEFAULT,60);

        char s;
        char t;
        for(int i=0; i<gridColDim+1; i++){
            if(i==0){
                s = '┌';
                t = '┐';
            } else if (i==gridColDim) {
                s = '└';
                t = '┘';
            }else{
                s = '├';
                t = '┤';
            }
            cliPixel[startY + i*2][startX] = s;
            cliPixelColor[startY + i*2][startX] = DEFAULT;
            cliPixel[startY + i*2][startX + gridRowDim * 4] = t;
            cliPixelColor[startY + i*2][startX + gridRowDim *4] = DEFAULT;
        }
    }

    private void drawGridContents(int startX, int startY, Tile[][] contents){
        startX = startX+1;
        startY = startY+1;

        for(int i=0; i<contents.length; i++){
            for(int j=0; j<contents[i].length; j++){
                int color = getColour(contents[i][j].getColor());
                char c = '█';
                if(color == DEFAULT)
                    c = ' ';

                for(int k=0; k<3; k++){
                    cliPixel[startY+i*2][k + startX + j*4] = c;
                    cliPixelColor[startY+i*2][k + startX + j*4] = color;
                }
            }
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
    private void drawBox(int y, int x, int height, int width, int colour) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) cliPixel[y + i][x + j] = '┌';
                else if (i == 0 && j == width - 1) cliPixel[y + i][x + j] = '┐';
                else if (i == height - 1 && j == 0) {
                    cliPixel[y + i][x + j] = '└';
                    cliPixelColor[y + i][x + j] = colour;
                } else if (i == height - 1 && j == width - 1) {
                    cliPixel[y + i][x + j] = '┘';
                    cliPixelColor[y + i][x + j] = colour;
                } else if (i == 0) {
                    cliPixel[y + i][x + j] = '─';
                    cliPixelColor[y + i][x + j] = colour;
                } else if (i == height - 1) {
                    cliPixel[y + i][x + j] = '─';
                    cliPixelColor[y + i][x + j] = colour;
                } else if (j == 0) {
                    cliPixel[y + i][x + j] = '│';
                    cliPixelColor[y + i][x + j] = colour;
                } else if (j == width - 1) {
                    cliPixel[y + i][x + j] = '│';
                    cliPixelColor[y + i][x + j] = colour;
                } else {
                    cliPixel[y + i][x + j] = ' ';
                    cliPixelColor[y + i][x + j] = DEFAULT;
                }
            }
        }
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

    /*-------------------------------------------------------------*/

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

    /*---------COMMON GOALS-------------------------------*/
    public void updateCommonGoal(CommonGoalInfo o) {
        commonGoals.put(o.id(), o);
        drawCommonGoals();
        render();
    }

    private final Map<String,String[]> commonGoalRes = getCommonGoalRes();

    private Map<String,String[]> getCommonGoalRes(){
        Map<String,String[]> ris = new HashMap<>();
        File dir = new File(this.getClass().getResource("/commonGoals/CLI").getPath());
        if(dir.isDirectory()) {
            File[] res;
            if(dir.listFiles()!=null)
                res = dir.listFiles();
            else
                res = new File[0];

            for (File f : res) {
                if (!f.isDirectory() && f.getName().contains(".txt")) {
                    try(FileInputStream fr = new FileInputStream(f)){
                        String img = new String(fr.readAllBytes(),StandardCharsets.UTF_8);
                        ris.put(f.getName().replace(".txt",""),img.split("\r\n"));
                    } catch (IOException e) {
                        System.err.println("error while reading resources");
                    }
                }
            }

        }
        return ris;
    }

    final int commonGoalsX = 3;
    final int commonGoalsY = 23;
    final int commonGoalsPadding = 3;
    final int commonGoalBoxWidht = 23;
    final int commonGoalBoxHeight = 15;
    private void drawCommonGoals() {
        int boxesStartY = commonGoalsY +1;
        StringBuilder points = new StringBuilder();
        int drawedCommonGoals = 0;
        for(String id : commonGoals.keySet()){
            int boxStartX = commonGoalsX + drawedCommonGoals*(commonGoalBoxWidht + commonGoalsPadding);
            drawBox(boxesStartY, boxStartX, commonGoalBoxHeight, commonGoalBoxWidht, DEFAULT);

            String[] res = commonGoalRes.getOrDefault(id, new String[0]);
            for(int j=0; j<res.length;j++){
                drawString(res[j], boxesStartY+1+j,boxStartX+1, DEFAULT,60);
            }

            String temp = "Points: "+commonGoals.get(id).tokenState().getPoints();
            int spaceBefore = (commonGoalBoxWidht - temp.length())/2;
            int spaceAfter = commonGoalBoxWidht - spaceBefore - temp.length();
            points.append(" ".repeat(spaceBefore));
            points.append(temp);
            points.append(" ".repeat(spaceAfter));
            points.append(" ".repeat(commonGoalsPadding));

            drawedCommonGoals++;
        }
        drawString(points.toString(), boxesStartY + commonGoalBoxHeight, commonGoalsX, DEFAULT, points.length());

        StringBuilder title = new StringBuilder();
        String t = "COMMON GOALS";
        int maxSize = commonGoals.size() * commonGoalBoxWidht + commonGoalsPadding * (commonGoals.size()-1);
        int spaceBefore = (maxSize - t.length())/2;
        title.append(" ".repeat(spaceBefore)).append(t);
        drawString(title.toString(),commonGoalsY, commonGoalsX, DEFAULT, title.length());
    }

    /*-------------------------------------------------------*/

    /*---------------GAME STATE------------------------------*/
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
    /*------------------------------------------------------*/

    /*----------------PERSONAL GOALS------------------------*/
    public void updatePersonalGoal(PersonalGoalInfo o) {
        //check if personal goal is already present in current personal goals
        int index = currentPersonalGoals.stream().map(PersonalGoalInfo::description).toList().indexOf(o.description());

        if (index != -1)
            currentPersonalGoals.set(index, o);
        else
            currentPersonalGoals.add(o);
        drawPersonalGoal();
    }

    final int personalGoalsX = 55;
    final int personalGoalsY = 24;

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

        drawGrid(personalGoalsX, personalGoalsY+1, shelf[0].length, shelf.length);
        drawGridContents(personalGoalsX, personalGoalsY+1, shelf);

        StringBuilder title = new StringBuilder();
        String t = "PERSONAL GOAL";
        int spaceBefore = (shelf[0].length*4+1 - t.length())/2;
        title.append(" ".repeat(spaceBefore)).append(t);
        drawString(title.toString(),personalGoalsY, personalGoalsX, DEFAULT, title.length());
    }

    /*-----------------------------------------------------*/

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
                //TODO sistemare qua
//                //find if a common goal in o.achievedCommonGoals() is in availableCommonGoals
//                for (CommonGoalInfo commonGoalInfo : availableCommonGoals) {
//                    if (o.achievedCommonGoals().getOrDefault(commonGoalInfo.description(), null) != null) {
//                        //remove it from availableCommonGoals
//                        availableCommonGoals.remove(commonGoalInfo);
//                        //add it to the achieved list
//                        achievedCommonGoals.add(commonGoalInfo);
//                    }
//                }

            }
        }
        if (!login)
            drawCommonGoals();
    }

    private record Pair(String string, int colour) {}
}