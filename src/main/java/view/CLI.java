package view;

import distibuted.interfaces.ClientInterface;
import distibuted.interfaces.ServerInterface;
import model.StandardMessage;
import model.Tile;
import model.Token;
import model.User;
import model.abstractModel.*;
import modelView.*;
import view.interfaces.UI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CLI implements UI{

    private enum View{
        SERVER_INTERACTION,
        GAME_INTERACTION
    }

    /*-------------COLORS---------------------*/
    public static final int WHITE = 37;
    public static final int GREEN = 32;
    public static final int YELLOW = 33;
    public static final int BLUE = 34;
    public static final int MAGENTA = 35;
    public static final int CYAN = 36;
    public static final int RED = 31;
    public static final int DEFAULT = 39;

    /*----------------------------------------*/

    /*---------------INFO--------------------*/
    private final List<PersonalGoalInfo> currentPersonalGoals = new ArrayList<>();
    private final List<GamesManagerInfo> games = new ArrayList<>();
    private GameInfo currentGameState;
    private UserInfo user;
    private final Map<String,CommonGoalInfo> commonGoals = new HashMap<>();
    private Map<String, Token> achievedCommonGoals = new HashMap<>();
    private final Map<String, ShelfInfo> currentShelves;
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;
    private String thisPlayerId;

    /*-----------------------------------------*/

    /*--------DISTRIBUTION OBJECTS-------------*/
    private ServerInterface server;
    private ClientInterface client;

    /*-----------------------------------------*/

    /*------------VIEW UTILITIES---------------*/
    final private TimedLock serverWaiter = new TimedLock();
    private boolean viewLock = false;
    private final Scanner scanner = new Scanner(System.in);
    private boolean GameRunning;
    private View currentView;
    static final PrintStream out = System.out;

    /*----------------------------------------*/

    /*-----------VIEW COMPONENTS--------------*/
    static final int renderHeight = 53;
    static final int renderWidth = 140;

    final char[][] cliPixel = new char[renderHeight][renderWidth];
    final int[][] cliPixelColor = new int[renderHeight][renderWidth];

    /*-----------------------------------------*/





    String cursor = "";

    public CLI() {
        currentView = View.SERVER_INTERACTION;
        currentShelves = new HashMap<>();
        drawBox(0, 0, renderHeight, renderWidth, DEFAULT);
        drawCommandLine();
        updateView(false);
    }

    /*-------------UI--------------------------*/
    public String askRMIorSocket() {
        return readCommandLine("Connect with RMI (r) or SOCKET (s)?, empty to exit: ");
    }

    public void showError(String error){
        printCommandLine(error, RED);
    }

    public void run(ServerInterface server, ClientInterface client){
        this.server = server;
        this.client = client;

        this.currentView = View.SERVER_INTERACTION;

        if(!serverWaiter.hasBeenNotified()) {
            try {
                serverWaiter.setValue(true);
                serverWaiter.lock(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        boolean exit = serverWaiter.getValue();

        serverWaiter.reset();

        if(!exit){
            printCommandLine("CONNECTED",GREEN);
        }

        while (!exit) {
            String readLine = readCommandLine("(h for commands)-> ");
            switch (readLine) {
                case "h" -> printCommandLine("1: Create a game\n2: Join a game\nexit: Close this window");
                case "1" -> {
                    try {
                        createGame();
                        printCommandLine("Game created", GREEN);
                    } catch (RemoteException e) {
                        printCommandLine(e.getMessage(), RED);
                    }
                }
                case "2" -> {
                    try {
                        joinGame();
                        printCommandLine("Player joined", GREEN);
                        gameRoutine();
                    } catch (RemoteException e) {
                        printCommandLine("Error while joining a game", RED);
                    }
                }
                case "exit" -> exit = true;
                default -> printCommandLine("Wrong command", RED);

            }
        }
    }

    /*------------------------------------------*/

    /*--------SERVER INTERACTION FUNCTIONS--------*/

    private void createGame() throws RemoteException {
        String gameId = readCommandLine("GameId: ");
        String p = readCommandLine("PlayerNumber (between 2 and 4): ");
        int k = Integer.parseInt(p);
        if (k > 1 && k < 5) {

            long requestTime = System.currentTimeMillis();
            server.createGame(client, new NewGameInfo(gameId, "STANDARD", k, requestTime));

            if(!serverWaiter.hasBeenNotified()){
                serverWaiter.setValue(true);
                try {
                    serverWaiter.lock(6000);
                } catch (InterruptedException e) {
                    throw new RemoteException("Connection timeout error");
                }
            }

            serverWaiter.reset();

            if(serverWaiter.getValue())
                throw new RemoteException(user.eventMessage());
        }
        else {
            throw new RemoteException("Wrong parameters (number between 2 and 4)");
        }
    }

    private void joinGame() throws RemoteException {
        String gameId = readCommandLine("GameId: ");
        String playerId = readCommandLine("Write playerId (empty to exit): ");
        if (!playerId.equals(""))
            server.joinGame(client, new LoginInfo(playerId, gameId,System.currentTimeMillis()));
        else
            return;

        if(!serverWaiter.hasBeenNotified()){
            try{
                serverWaiter.setValue(true);
                serverWaiter.lock(6000);
            } catch (InterruptedException e) {
                throw new RemoteException("Login timeout error");
            }
        }
        serverWaiter.reset();


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
            switch (readLine) {
                case "h" -> printCommandLine("1: Update view status\n2: Pick tiles\n3: Send message");
                case "1" -> updateView(false);
                case "2" -> pickTiles();
                case "3" -> sendMessage();
                case "4" -> leave();
                default -> printCommandLine("Wrong command", RED);
            }
        }
    }

    /*-----------------------------------------------*/


    /*---------------GAME ROUTINE FUNCTIONS---------------*/

    private void sendMessage() throws RemoteException {

        viewLock = true;

        String subject = readCommandLine("Message Subject (empty for everyone): ");
        String content = readCommandLine("Message content: ");
        viewLock = false;

        server.sendMessage(client, new StandardMessage(thisPlayerId, subject, content));

    }

    private void pickTiles() throws RemoteException {

            viewLock = true;

        final int pickableNum = 3;
        List<PickedTile> tiles = new ArrayList<>();
        boolean choosing = true;
        do {
            printCommandLine("Remaining pickable tiles: " + pickableNum+"\nWrite row,col r2,c2 to pick up to three tiles");
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
                    formatError=true;
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    printCommandLine("Wrong format, Should be r1,c1 r2,c2 r3,c3", RED);
                    formatError=true;
                    break;
                }
            }
            if(formatError)
                continue;
            //check if the tiles are pick-able, pick-able if they are all in the same row or column and adjacent to each other
            boolean pickable = isPickable(tTiles, currentLivingRoom.board());//TODO don't seams to work
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
                String sCol = readCommandLine("Shelf col: ");
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

            viewLock = false;

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

        if (sameRow) {
            int[] cols = pickedTiles.stream().mapToInt(PickedTile::col).toArray();
            Arrays.sort(cols);
            for (int i = 0; i < cols.length-1; i++) {
                if (cols[i+1] - cols[i] != 1) {
                    return false;
                }
            }
        }
        if (sameCol) {
            int[] rows = pickedTiles.stream().mapToInt(PickedTile::row).toArray();
            Arrays.sort(rows);
            for (int i = 0; i < rows.length-1; i++) {
                if (rows[i+1] - rows[i] != 1) {
                    return false;
                }
            }
        }

        return true;
    }

    private void leave(){
        try {
            server.leaveGame(client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /*---------------------------------------------------*/

    /*--------------RENDERING FUNCTIONS------------------*/

    int updated = 0;
    private void updateView(boolean force) {
        if(!viewLock || force) {
                //clear the matrix
                Arrays.stream(cliPixel).forEach(a -> Arrays.fill(a, ' '));
                Arrays.stream(cliPixelColor).forEach(a -> Arrays.fill(a, DEFAULT));

                switch (currentView){
                    case SERVER_INTERACTION -> {
                        drawBox(0, 0, renderHeight, renderWidth, DEFAULT);
                        drawCommandLine();
                    }
                    case GAME_INTERACTION -> {
                        drawBox(0, 0, renderHeight, renderWidth, DEFAULT);
                        drawCommandLine();
                        drawGameState();
                        drawLivingRoom();
                        drawShelves();
                        drawChat();
                        drawCommonGoals();
                        drawPersonalGoal();
                    }
                }

                updated++;
                drawString(String.valueOf(updated)+" ",0,0,GREEN, 20);
                render();
            }

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
    public static void moveCursor(int y, int x) {
        System.out.print("\033[" + y + ";" + x + "H");
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

    public String readCommandLine(String message) {
        cursor = message;
        System.out.print(message);
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
        for(String s : lines)
            oldCmds.add(new Pair(s, colour));

        while (oldCmds.size() > 8)
            oldCmds.remove(0);

        updateView(true);
    }

    public void render() {
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
        moveCursor(commandLineY+commandLineHeight, commandLineX+2+cursor.length()+1);
    }

    /*--------------------------------------------------*/


    /*---------------CHAT---------------------------*/
    public void update(PlayerChatInfo pC, PlayerChat.Event evt) {
        //set current player chat
        this.currentPlayerChat = pC;
        updateView(false);

    }

    final int chatX = 80;
    final int chatY = 23;

    final int chatBoxWidth = 58;
    final int chatBoxHeight = 28;

    private void drawChat() {
        if(currentPlayerChat!=null){
            List<Message> messages = currentPlayerChat.messages();
            Collections.reverse(messages);

            drawBox(chatY + 1, chatX, chatBoxHeight, chatBoxWidth, DEFAULT);

            int chatContentsX = chatX + 1;
            int chatContentsY = chatY + 2;
            int chatContentsHeight = chatBoxHeight - 2;
            int chatContentsWidth = chatBoxWidth - 4;

            String[] chatBuffer = new String[chatContentsHeight];
            Arrays.fill(chatBuffer, "");

            int pointer = chatContentsHeight - 1;
            for (Message m : messages) {
                String text = m.getSender() + " to " + ((m.getSubject().isBlank()) ? "Everyone" : m.getSubject()) + ": " + m.getText();
                List<String> temp = new ArrayList<>();

                do {
                    int size = Math.min(text.length(), chatContentsWidth);

                    String s = text.substring(0, size);
                    temp.add(s);


                    text = "    " + text.substring(size);
                } while (!text.isBlank());

                for (int i = temp.size() - 1; i >= 0; i--) {
                    chatBuffer[pointer] = temp.get(i);
                    pointer--;
                    if (pointer < 0)
                        break;
                }

                if (pointer < 0)
                    break;
            }

            for (int i = 0; i < chatBuffer.length; i++) {
                drawString(chatBuffer[i], chatContentsY + i, chatContentsX + 1, DEFAULT, chatBuffer[i].length());
            }

            StringBuilder title = new StringBuilder();
            String t = "CHAT";
            int spaceBefore = (chatBoxWidth - t.length()) / 2;
            title.append(" ".repeat(spaceBefore)).append(t);
            drawString(title.toString(), chatY, chatX, DEFAULT, title.length());
        }
    }


    /*------------------------------------------------------*/

    /*----------LIVING ROOM--------------------------*/
    public void update(LivingRoomInfo lR, LivingRoom.Event evt) {
        //set current living room
        this.currentLivingRoom = lR;
        updateView(false);
    }
    final int livingRoomX = 1;
    final int livingRoomY = 1;
    private void drawLivingRoom() {
        if(currentLivingRoom!=null){
            Tile[][] board = currentLivingRoom.board();
            drawGrid(livingRoomX + 2, livingRoomY + 2, board[0].length, board.length);
            drawGridContents(livingRoomX + 2, livingRoomY + 2, board);

            //draw numbers on the top
            for (int i = 0; i < board.length; i++) {
                String number = String.valueOf(i);
                if (i < 10) {
                    number = "  " + number + " ";
                } else {
                    number = " " + number + " ";
                }
                for (int c = 0; c < number.length(); c++) {
                    cliPixel[livingRoomY + 1][livingRoomX + 2 + i * 4 + c] = number.charAt(c);
                    cliPixelColor[livingRoomY + 1][livingRoomX + 2 + i * 4 + c] = DEFAULT;
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
            int spaceBefore = (board[0].length * 4 + 1 - t.length()) / 2;
            title.append(" ".repeat(spaceBefore)).append(t);
            drawString(title.toString(), livingRoomY, livingRoomX + 2, DEFAULT, title.length());
        }
    }

    /*--------------------------------------------------*/

    /*-------------SHELVES-------------------------------*/
    public void update(ShelfInfo sV, Shelf.Event evt) {
        //set current shelf

        currentShelves.put(sV.playerId(), sV);

        updateView(false);
    }
    final int shelvesX = 43;
    final int shelvesY = 4;
    final int shelvesPadding = 3;
    private void drawShelves() {
        if(!currentShelves.isEmpty()) {
            int shelvesGridY = shelvesY + 1;

            int shelvesHeight = 0;
            int shelvesWidth = 0;

            int shelfDrewed = 0;

            StringBuilder playersName = new StringBuilder();
            StringBuilder playersPoints = new StringBuilder();

            for (String playerId : currentShelves.keySet()) {
                Tile[][] shelf = currentShelves.get(playerId).shelf();

                shelvesHeight = shelf.length;
                shelvesWidth = shelf[0].length * 4 + 1;

                int shelfX = shelvesX + (shelvesWidth + shelvesPadding) * shelfDrewed;
                drawGrid(shelfX, shelvesGridY, shelf[0].length, shelf.length);
                drawGridContents(shelfX, shelvesGridY, shelf);

                String tempPlayerId = playerId;

                if (playerId.equals(thisPlayerId))
                    tempPlayerId = "YOU";

                if (tempPlayerId.length() > shelvesWidth - 4)
                    tempPlayerId = tempPlayerId.substring(0, shelvesWidth - 4);


                if (playerId.equals(currentGameState.playerOnTurn()))
                    tempPlayerId = '>' + tempPlayerId + '<';

                int spaceBefore = (shelvesWidth - tempPlayerId.length()) / 2;
                int spaceAfter = shelvesWidth - spaceBefore - tempPlayerId.length();
                playersName.append(" ".repeat(spaceBefore));
                playersName.append(tempPlayerId);
                playersName.append(" ".repeat(spaceAfter));
                playersName.append(" ".repeat(shelvesPadding));

                String points = "Points: " + currentGameState.points().getOrDefault(playerId, 0);
                spaceBefore = (shelvesWidth - points.length()) / 2;
                spaceAfter = shelvesWidth - spaceBefore - points.length();
                playersPoints.append(" ".repeat(spaceBefore));
                playersPoints.append(points);
                playersPoints.append(" ".repeat(spaceAfter));
                playersPoints.append(" ".repeat(shelvesPadding));

                shelfDrewed++;
            }

            drawString(playersName.toString(), shelvesGridY + shelvesHeight * 2 + 1, shelvesX, DEFAULT, playersName.length());
            drawString(playersPoints.toString(), shelvesGridY + shelvesHeight * 2 + 2, shelvesX, DEFAULT, playersPoints.length());

            StringBuilder title = new StringBuilder();
            String t = "PLAYERS SHELVES";
            int maxSize = currentShelves.size() * shelvesWidth + shelvesPadding * (currentShelves.size() - 1);
            int spaceBefore = (maxSize - t.length()) / 2;
            title.append(" ".repeat(spaceBefore)).append(t);
            drawString(title.toString(), shelvesY, shelvesX, DEFAULT, title.length());
        }
    }

    /*-------------------------------------------------------------*/


    /*-----------------COMMAND LINE--------------------------------*/

    final int commandLineX = 1;
    final int commandLineY = 41;

    final int commandLineWidth = 75;
    final int commandLineHeight = 10;

    private void drawCommandLine() {
        drawBox(commandLineY+1, commandLineX, commandLineHeight, commandLineWidth, DEFAULT);
        StringBuilder title = new StringBuilder();
        String t = "COMMAND LINE";
        int spaceBefore = (commandLineWidth - t.length())/2;
        title.append(" ".repeat(spaceBefore)).append(t);
        drawString(title.toString(),commandLineY, commandLineX, DEFAULT, title.length());

       drawString(cursor, commandLineY+commandLineHeight-1, commandLineX, DEFAULT, commandLineWidth-3);
        drawOldCmds();
    }

    //old cmds to be shifted up
    final List<Pair> oldCmds = new ArrayList<>();

    private void drawOldCmds() {
        while (oldCmds.size() > (commandLineHeight-4))
            oldCmds.remove(0);

        for (int i = 0; i < oldCmds.size(); i++) {
            drawString(oldCmds.get(i).string(), commandLineY+2+i, commandLineX+1, oldCmds.get(i).colour(), commandLineWidth-2);
        }
    }

    /*-----------------------------------------------------------*/

    /*---------COMMON GOALS-------------------------------*/
    public void update(CommonGoalInfo o, CommonGoal.Event evt) {
        commonGoals.put(o.id(), o);
        updateView(false);
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
    final int commonGoalBoxWidth = 23;
    final int commonGoalBoxHeight = 15;
    private void drawCommonGoals() {
        if(!commonGoals.isEmpty()){
            int boxesStartY = commonGoalsY + 1;
            StringBuilder points = new StringBuilder();
            int drewedCommonGoals = 0;
            for (String id : commonGoals.keySet()) {
                int boxStartX = commonGoalsX + drewedCommonGoals * (commonGoalBoxWidth + commonGoalsPadding);
                drawBox(boxesStartY, boxStartX, commonGoalBoxHeight, commonGoalBoxWidth, DEFAULT);

                String[] res = commonGoalRes.getOrDefault(id, new String[0]);
                for (int j = 0; j < res.length; j++) {
                    drawString(res[j], boxesStartY + 1 + j, boxStartX + 1, DEFAULT, 60);
                }

                String temp;
                if (achievedCommonGoals.containsKey(id))
                    temp = "ACHIEVED: " + achievedCommonGoals.get(id).getPoints();
                else
                    temp = "Points: " + commonGoals.get(id).tokenState().getPoints();

                int spaceBefore = (commonGoalBoxWidth - temp.length()) / 2;
                int spaceAfter = commonGoalBoxWidth - spaceBefore - temp.length();
                points.append(" ".repeat(spaceBefore));
                points.append(temp);
                points.append(" ".repeat(spaceAfter));
                points.append(" ".repeat(commonGoalsPadding));

                drewedCommonGoals++;
            }
            drawString(points.toString(), boxesStartY + commonGoalBoxHeight, commonGoalsX, DEFAULT, points.length());

            StringBuilder title = new StringBuilder();
            String t = "COMMON GOALS";
            int maxSize = commonGoals.size() * commonGoalBoxWidth + commonGoalsPadding * (commonGoals.size() - 1);
            int spaceBefore = (maxSize - t.length()) / 2;
            title.append(" ".repeat(spaceBefore)).append(t);
            drawString(title.toString(), commonGoalsY, commonGoalsX, DEFAULT, title.length());
        }
    }

    /*-------------------------------------------------------*/

    /*---------------GAME STATE------------------------------*/
    public void update(GameInfo o, Game.Event evt) {
        currentGameState = o;

        if (o.status()== Game.GameStatus.ENDED){
            printCommandLine("Game Ended, press enter to return to login menu");
            //get max points
            int maxPoints = o.points().values().stream().max(Integer::compareTo).orElse(0);
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
        } else if (o.status() == Game.GameStatus.CLOSED) {
            printCommandLine("Game closing");
            GameRunning=false;
            readCommandLine("");
            return;
        }

        updateView(false);
    }

    private void drawGameState() {
        if(currentGameState!=null) {
            //redraw shelves with new current player
            if (currentLivingRoom != null && currentShelves != null && currentLivingRoom.board() != null && currentLivingRoom.board().length > 0 && currentShelves.size() > 0 && currentLivingRoom.board()[0].length > 0)
                drawShelves();
            //draw is last turn if it is
            if (currentGameState.lastTurn())
                drawString("Last Turn", renderHeight - 10, 30, DEFAULT, 50 - 2);
        }
    }
    /*------------------------------------------------------*/

    /*----------------PERSONAL GOALS------------------------*/
    public void update(PersonalGoalInfo o, PersonalGoal.Event evt) {
        //check if personal goal is already present in current personal goals
        int index = currentPersonalGoals.stream().map(PersonalGoalInfo::description).toList().indexOf(o.description());

        if (index != -1)
            currentPersonalGoals.set(index, o);
        else
            currentPersonalGoals.add(o);

        updateView(false);
    }

    final int personalGoalsX = 55;
    final int personalGoalsY = 24;

    private void drawPersonalGoal() {
        if(currentPersonalGoals.size()==6) {
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

            drawGrid(personalGoalsX, personalGoalsY + 1, shelf[0].length, shelf.length);
            drawGridContents(personalGoalsX, personalGoalsY + 1, shelf);

            StringBuilder title = new StringBuilder();
            String t = "PERSONAL GOAL";
            int spaceBefore = (shelf[0].length * 4 + 1 - t.length()) / 2;
            title.append(" ".repeat(spaceBefore)).append(t);
            drawString(title.toString(), personalGoalsY, personalGoalsX, DEFAULT, title.length());
        }
    }

    /*-----------------------------------------------------*/

    public void update(UserInfo o, User.Event evt) {
        user = o;

        if(evt==null) {
            serverWaiter.notify(false);
            return;
        }

        switch (evt){
            case STATUS_CHANGED, GAME_CREATED -> serverWaiter.notify(false);
            case ERROR_REPORTED -> serverWaiter.notify(true);
        }
    }

    public void update(GamesManagerInfo o, GamesManager.Event evt) {
        switch (evt) {
            case GAME_CREATED -> games.add(o);
            case GAME_REMOVED -> games.remove(o);
        }
        //drawGameList();
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

    public void update(PlayerInfo o, Player.Event evt) {
        if (evt == null) {
            return;
        }
        switch (evt) {
            case ERROR_REPORTED -> printCommandLine(o.errorMessage(), RED);
            case COMMONGOAL_ACHIEVED -> {
                achievedCommonGoals = o.achievedCommonGoals();
                updateView(false);
            }
        }
    }

    private record Pair(String string, int colour) {}
}

class TimedLock{

    private boolean notified;
    private boolean value;
    public TimedLock(){
        notified = false;
        value = false;
    }

    public synchronized void reset(){
        notified = false;
    }

    @SuppressWarnings( "BooleanMethodIsAlwaysInverted")
    public synchronized boolean hasBeenNotified(){
        return notified;
    }

    public synchronized void setValue(boolean value){
        this.value = value;
    }

    public synchronized boolean getValue(){
        return value;
    }

    public synchronized void lock(long timeoutMillis) throws InterruptedException {
        this.notified = false;
        this.wait(timeoutMillis);
    }

    public synchronized void notify(boolean value){
        this.notified = true;
        this.value = value;
        this.notifyAll();
    }
}