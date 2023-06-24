package view.TUI;

import model.abstractModel.Message;
import view.ViewLogic;
import view.graphicInterfaces.GameGraphics;
import view.graphicInterfaces.AppGraphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.*;
import model.abstractModel.Game.GameStatus;

import model.Tile;
import model.Token;

import static view.TUI.TUIdraw.*;
import static view.TUI.TUIutils.*;

public class TUI implements AppGraphics, GameGraphics {

    record Pair(String string, int colour) {}
    private enum Scene{
        CONNECTION,
        INTERACTION,
        JOIN,
        CREATE,
        GAME,
        LEADERBOARD,
    }
    private String cursor = "";

    static final int renderHeight = 53;
    static final int renderWidth = 140;

    static final int cursorX = 3;
    static final int cursorY = 51;

    private final PrintStream out = System.out;

    private final Scanner scanner = new Scanner(System.in);

    private final List<Pair> oldCmds = new ArrayList<>();

    private final char[][] canvas = new char[renderHeight][renderWidth];

    private final int[][] canvasColors = new int[renderHeight][renderWidth];

    private final Object renderLock = new Object();
    private final Object updateLock = new Object();

    private Scene scene;

    private int updated = 0; //TODO to remove

    /*------------------ INPUT UTILITY --------------------*/
    private final Thread inputThread = new Thread(() -> {
        while(true) {
            String cmd = this.scanner.nextLine();
            printCommandLine(this.cursor + " " + cmd);
            dispatchInput(cmd);
        }
    });

    private void dispatchInput(String cmd){
        switch (scene){
            case CONNECTION -> {
                if(cmd.isBlank())
                    actionListener.actionPerformed(new ActionEvent(this,ViewLogic.EXIT,""));
                else {
                    if (cmd.equals("rmi") || cmd.equals("r"))
                        cmd = ViewLogic.CONNECT_RMI;
                    else if (cmd.equals("socket") || cmd.equals("s")) {
                        cmd = ViewLogic.CONNECT_SOCKET;
                    }
                    actionListener.actionPerformed(new ActionEvent(this, ViewLogic.CONNECT,cmd));
                }
            }
            case INTERACTION -> {
                switch (cmd){
                    case "h" -> {
                        showHints();
                        render();
                    }
                    case "c" -> actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE,""));
                    case "j" -> actionListener.actionPerformed(new ActionEvent(this,ViewLogic.ROUTE_JOIN,""));
                    case "e" -> actionListener.actionPerformed(new ActionEvent(this,ViewLogic.EXIT,""));
                    default -> render();
                }
            }
            case CREATE -> {
                if(cmd.isBlank()){
                    actionListener.actionPerformed(new ActionEvent(this,ViewLogic.ROUTE_HOME,""));
                }else{
                    if(cmd.split(" ").length!=2){
                        actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE, "Wrong number of parameter (2 required)"));
                    }else {
                        actionListener.actionPerformed(new ActionEvent(this, ViewLogic.CREATE, "STANDARD " + cmd));
                    }
                }
            }
            case JOIN -> {
                if(cmd.isBlank()){
                    actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
                }else {
                    actionListener.actionPerformed(new ActionEvent(this, ViewLogic.JOIN,cmd));
                }
            }
            case GAME ->{
                switch (cmd){
                    case "h" -> {
                        showHints();
                        render();
                    }
                    case "p" -> doMoveRoutine();
                    case "s" -> sendMessageRoutine();
                    case "e" -> actionListener.actionPerformed(new ActionEvent(this,ViewLogic.LEAVE_GAME,""));
                    default -> render();
                }
            }
            case LEADERBOARD -> actionListener.actionPerformed(new ActionEvent(this,ViewLogic.ROUTE_HOME,""));
        }
    }

    private void sendMessageRoutine(){
        String subject;
        String content;
        synchronized (renderLock){
            this.cursor = "To (empty for everyone): ";
            render();
            subject = this.scanner.nextLine();
            printCommandLine(this.cursor+" "+subject);
            this.cursor = "Message content (empty to abort): ";
            render();
            content = this.scanner.nextLine();
            this.cursor = "(h for commands) ->";
            render();
        }
        if(!content.isBlank())
            actionListener.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MESSAGE,
                    playerId+"\n"+subject+"\n"+content
            ));
    }

    private void doMoveRoutine(){
        String pickedTiles;
        String sCol = "";
        boolean choosing = true;
        boolean exit = false;
        synchronized (renderLock) {
            do {
                printCommandLine("Write row,col r2,c2 ... to pick up to three tiles (empty to abort)");
                this.cursor = "->";
                render();
                pickedTiles = this.scanner.nextLine();

                if (pickedTiles.isBlank()) {
                    exit = true;
                    break;
                }

                String[] split = pickedTiles.split(" ");
                //if more than 3 tiles are picked ignores the rest

                try {
                    for (int i = 0; i < split.length && i < 3; i++) {
                        String[] split1 = split[i].split(",");
                        Integer.parseInt(split1[0]);
                        Integer.parseInt(split1[1]);
                    }
                    choosing = false;
                } catch (NumberFormatException e) {
                    printCommandLine("Illegal character in the sequence", RED);
                }
            } while (choosing);
        }
        if(!exit) {
            synchronized (renderLock) {
                choosing = true;
                do {
                    try {
                        printCommandLine("Write the column of the shelf (right-starts from 0) (empty to abort)");
                        render();
                        sCol = this.scanner.nextLine();

                        if (sCol.isBlank()) {
                            exit = true;
                            break;
                        }

                        Integer.parseInt(sCol);

                        choosing = false;
                    } catch (NumberFormatException e) {
                        printCommandLine("Shelf chosen column is not a number", RED);
                    }
                } while (choosing);
            }
        }

        if(!exit){
            actionListener.actionPerformed(new ActionEvent(this,ViewLogic.SEND_MOVE,pickedTiles+"\n"+sCol));
        }

        synchronized (renderLock){
            this.cursor = "(h for commands)->";
            render();
        }
    }

    private void showHints(){
        synchronized (updateLock) {
            switch (scene) {
                case INTERACTION -> this.printCommandLine("c: Create a game\nj: Join a game\ne: Exit\nu: Update view");
                case GAME -> this.printCommandLine("p: Pick tiles\ns: Send message\ne: Leave game\nu: Update view");
            }
        }
    }

    /*-----------------  GRAPHIC UTILITY --------------------*/

    private void render(){
        /* Current values of gameplay info */
        Tile[][] currentBoardState;
        List<Pair> currentOldCmd;
        Map<String,Tile[][]> currentPlayerShelves;
        String currentPlayerId;
        String currentFirstPlayer;
        String currentPlayerOnTurn;
        Map<String,Integer> currentPointsValues;
        String currentCursor;
        boolean currentIsLastTurn;
        List<Message> currentChat;
        Map<String,Token> currentCommonGoals;
        Map<String,Token> currentAchievedCommonGoals;
        Tile[][] currentPersonalGoals;
        GameStatus currentGameStatus;

        /* Retrieve current values of gameplay info */
        synchronized (updateLock){
            currentCursor = cursor;
            currentOldCmd = new ArrayList<>(oldCmds);
            currentPlayerId = playerId;
            currentBoardState = boardState;
            currentPlayerShelves = new HashMap<>(playerShelves);
            currentPlayerOnTurn = playerOnTurn;
            currentFirstPlayer = firstTurnPlayer;
            currentPointsValues = new HashMap<>(pointsValues);
            currentIsLastTurn = isLastTurn;
            currentChat = new ArrayList<>(chat);
            currentCommonGoals = new HashMap<>(commonGoals);
            currentAchievedCommonGoals = new HashMap<>(achievedCommonGoals);
            currentPersonalGoals = personalGoalsDescription;
            currentGameStatus = status;
        }

        /* Then update graphics on retrieved values */
        synchronized (renderLock) {

            Arrays.stream(this.canvas).forEach(a -> Arrays.fill(a, ' '));
            Arrays.stream(this.canvasColors).forEach(a -> Arrays.fill(a, DEFAULT));

            if(scene == Scene.GAME){
                if(currentGameStatus == GameStatus.MATCHMAKING || currentGameStatus == GameStatus.STARTED || currentGameStatus == GameStatus.SUSPENDED){
                    drawChat(playerId,currentChat, this.canvas, this.canvasColors);
                    drawShelves(currentPlayerShelves, currentFirstPlayer, currentPlayerId, currentPlayerOnTurn, currentPointsValues, this.canvas, this.canvasColors);
                }
                if(currentGameStatus == GameStatus.MATCHMAKING || currentGameStatus == GameStatus.SUSPENDED){
                    drawCenteredString("WAITING FOR OTHER PLAYERS TO JOIN",0,32,80,GREEN,canvas,canvasColors);
                }
                if(currentGameStatus== GameStatus.STARTED){
                    drawLastTurn(currentIsLastTurn, this.canvas, this.canvasColors);
                    drawLivingRoom(currentBoardState, this.canvas, this.canvasColors);
                    drawCommonGoals(currentCommonGoals, currentAchievedCommonGoals, this.canvas, this.canvasColors);
                    drawPersonalGoal(currentPersonalGoals, this.canvas, this.canvasColors);
                }
            } else if (scene == Scene.LEADERBOARD) {
                drawGameEnd(currentPointsValues, this.canvas, this.canvasColors);
            }

            drawBox(0, 0, renderHeight, renderWidth, DEFAULT, this.canvas, this.canvasColors);
            drawCommandLine(currentCursor, currentOldCmd, this.canvas, this.canvasColors);

            this.updated++;
            drawString(this.updated + " ", 0, 0, GREEN, 20, this.canvas, this.canvasColors);

            ClearScreen(s -> printCommandLine(s,RED));

            for (int i = 0; i < this.canvas.length; i++) {
                for (int j = 0; j < this.canvas[0].length; j++) {
                    out.print(this.renderPixel(i, j));
                }
                out.println();
            }

            out.print("\033[" + (cursorY) + ";" + (cursorX + this.cursor.length() + 2) + "H");
        }
    }

    private String renderPixel(int x, int y) {
        return "\u001B[" + this.canvasColors[x][y] + "m" + this.canvas[x][y] + "\u001B[0m";
    }

    private void printCommandLine(String toPrint) {
        this.printCommandLine(toPrint, DEFAULT);
    }

    private void printCommandLine(String toPrint, int colour) {
        String[] lines = toPrint.split("\n");

        synchronized (updateLock) {
            for (String s : lines)
                this.oldCmds.add(new Pair(s, colour));

            while (this.oldCmds.size() > 8)
                this.oldCmds.remove(0);
        }
    }

    /*----------------- CONSTRUCTOR ------------------------*/

    public TUI(){
        inputThread.start();
        resetGameGraphics("");
    }

    /*----------------- APP GRAPHICS ------------------------*/

    private ActionListener actionListener;
    @Override
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public GameGraphics getGameGraphics(){
        return this;
    }

    @Override
    public void showConnection(String error) {
        synchronized (renderLock) {
            scene = Scene.CONNECTION;
            this.cursor = "Connect with RMI (r) or SOCKET (s)?, empty to exit:";

            if (!error.isBlank())
                printCommandLine(error, RED);
        }
        render();
    }

    @Override
    public void showServerInteraction(String message){
        synchronized (renderLock) {
            this.scene = Scene.INTERACTION;
            this.cursor = "(h for commands)->";

            if(!message.isBlank())
                printCommandLine(message);
        }
        render();
    }

    @Override
    public void showJoin(String error) {
        synchronized (renderLock) {
            this.scene = Scene.JOIN;
            this.cursor = "Write playerId and gameId (empty to exit):";

            if(!error.isBlank())
                printCommandLine(error,RED);
        }
        render();
    }

    @Override
    public void showCreate(String error) {
        synchronized (renderLock){
            this.scene = Scene.CREATE;
            this.cursor = "Write gameId and playerNumber (empty to exit):";

            if(!error.isBlank())
                printCommandLine(error,RED);
        }
        render();
    }

    @Override
    public void showGame(String message){
        synchronized (updateLock) {
            this.scene = Scene.GAME;
            this.cursor = "(h for commands)->";
        }

        if(!message.isBlank())
            printCommandLine(message);

        render();
    }

    @Override
    public void exit(){
        inputThread.interrupt();
        System.exit(0);
    }


    /*------------------- OTHER GRAPHICS ----------------------*/

    private String playerId;
    @Override
    public void resetGameGraphics(String playerId){
        synchronized (updateLock) {
            this.playerId = playerId;
            this.playerOnTurn = "";
            boardState = new Tile[9][9];
            for (Tile[] row : boardState)
                Arrays.fill(row, Tile.EMPTY);
            personalGoalsDescription = new Tile[6][5];
            for (Tile[] row : personalGoalsDescription)
                Arrays.fill(row, Tile.EMPTY);
            playerShelves = new HashMap<>();
            pointsValues = new HashMap<>();
            isLastTurn = false;
            chat = new ArrayList<>();
            commonGoals = new HashMap<>();
            achievedCommonGoals = new HashMap<>();
            personalGoals = new HashMap<>();
            status = GameStatus.MATCHMAKING;
        }
    }

    private Tile[][] boardState;
    @Override
    public void updateBoardGraphics(Tile[][] board){
        synchronized (updateLock){
            boardState = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
        }
        render();
    }

    private Map<String,Tile[][]> playerShelves;

    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf){
        synchronized (updateLock){
            playerShelves.put(playerId, Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new));
        }
        render();
    }

    private String playerOnTurn;

    private String firstTurnPlayer;

    private Map<String,Integer> pointsValues;

    private boolean isLastTurn;

    private GameStatus status;

    @Override
    public void updateGameInfoGraphics(GameStatus status, String firstTurnPlayer, String playerOnTurn, boolean isLastTurn, Map<String,Integer> pointsValues){
        synchronized (updateLock){
            this.status = status;
            this.firstTurnPlayer = firstTurnPlayer;
            this.playerOnTurn = playerOnTurn;
            this.pointsValues = new HashMap<>(pointsValues);
            this.isLastTurn = isLastTurn;

            if(status == GameStatus.ENDED){
                this.cursor = "Press enter to go back to the home page";
                scene = Scene.LEADERBOARD;
            }
        }
        render();
    }

    private List<Message> chat;

    @Override
    public void updatePlayerChatGraphics(List<Message> chat){
        synchronized (updateLock){
            this.chat = chat;
        }
        render();
    }

    private Map<String,Token> commonGoals;

    @Override
    public void updateCommonGoalGraphics(String id, String description, Token tokenState){
        synchronized (updateLock){
            commonGoals.put(id,tokenState);
        }
        render();
    }

    private Map<String,Token> achievedCommonGoals;

    @Override
    public void updateAchievedCommonGoals(Map<String,Token> achievedCommonGoals){
        synchronized (updateLock){
            this.achievedCommonGoals = achievedCommonGoals;
        }
        render();
    }

    @Override
    public void updateErrorState(String reportedError){
        synchronized (updateLock){
            if(!reportedError.isBlank())
                printCommandLine(reportedError,RED);
        }
        render();
    }

    private Tile[][] personalGoalsDescription;

    private Map<Integer,Boolean> personalGoals;
    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description){
        synchronized (updateLock){
            if(!personalGoals.containsKey(id)){
                Tile[][] temp = new Tile[6][5];
                for(int i=0;i<description.length; i++)
                    for(int j=0;j<description[i].length;j++) {
                        if (description[i][j] != Tile.EMPTY) {
                            temp[i][j] = description[i][j];
                        } else {
                            temp[i][j] = personalGoalsDescription[i][j];
                        }
                    }
                personalGoalsDescription = temp;
            }

            personalGoals.put(id,hasBeenAchieved);
        }
        render();
    }
}
