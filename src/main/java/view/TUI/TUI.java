package view.TUI;

import model.Tile;
import model.Token;
import model.abstractModel.Game.GameStatus;
import model.abstractModel.Message;
import view.ViewLogic;
import view.graphicInterfaces.AppGraphics;
import view.graphicInterfaces.GameGraphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.*;

import static view.TUI.TUIdraw.*;
import static view.TUI.TUIutils.*;

public class TUI implements AppGraphics, GameGraphics {

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
    private String cursor = "";
    private Scene scene;
    private ActionListener actionListener;
    private String playerId;
    private Tile[][] boardState;
    private Map<String, Tile[][]> playerShelves;
    private String playerOnTurn;
    private String firstTurnPlayer;
    private Map<String, Integer> pointsValues;

    /*-----------------  GRAPHIC UTILITY --------------------*/
    private boolean isLastTurn;
    private GameStatus status;
    private List<Message> chat;
    private Map<String, Token> commonGoals;

    /*----------------- CONSTRUCTOR ------------------------*/
    private Map<String, Token> achievedCommonGoals;

    /*----------------- APP GRAPHICS ------------------------*/
    private Tile[][] personalGoalsDescription;
    /*------------------ INPUT UTILITY --------------------*/
    private final Thread inputThread = new Thread(() -> {
        while (true) {
            String cmd = this.scanner.nextLine();
            this.printCommandLine(this.cursor + " " + cmd);
            this.dispatchInput(cmd);
        }
    });
    private Map<Integer, Boolean> personalGoals;

    public TUI() {
        this.inputThread.start();
        this.resetGameGraphics("");
    }

    private void dispatchInput(String cmd) {
        switch (this.scene) {
            case CONNECTION -> {
                if (cmd.isBlank())
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.EXIT, ""));
                else {
                    if (cmd.equals("rmi") || cmd.equals("r"))
                        cmd = ViewLogic.CONNECT_RMI;
                    else if (cmd.equals("socket") || cmd.equals("s")) {
                        cmd = ViewLogic.CONNECT_SOCKET;
                    }
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.CONNECT, cmd));
                }
            }
            case INTERACTION -> {
                switch (cmd) {
                    case "h" -> {
                        this.showHints();
                        this.render();
                    }
                    case "c" -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE, ""));
                    case "j" -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_JOIN, ""));
                    case "e" -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.EXIT, ""));
                    default -> this.render();
                }
            }
            case CREATE -> {
                if (cmd.isBlank()) {
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
                } else {
                    if (cmd.split(" ").length != 2) {
                        this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE, "Wrong number of parameter (2 required)"));
                    } else {
                        this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.CREATE, "STANDARD " + cmd));
                    }
                }
            }
            case JOIN -> {
                if (cmd.isBlank()) {
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
                } else {
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.JOIN, cmd));
                }
            }
            case GAME -> {
                switch (cmd) {
                    case "h" -> {
                        this.showHints();
                        this.render();
                    }
                    case "p" -> this.doMoveRoutine();
                    case "s" -> this.sendMessageRoutine();
                    case "e" -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.LEAVE_GAME, ""));
                    default -> this.render();
                }
            }
            case LEADERBOARD -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
        }
    }

    private void sendMessageRoutine() {
        String subject;
        String content;
        synchronized (this.renderLock) {
            this.cursor = "To (empty for everyone): ";
            this.render();
            subject = this.scanner.nextLine();
            this.printCommandLine(this.cursor + " " + subject);
            this.cursor = "Message content (empty to abort): ";
            this.render();
            content = this.scanner.nextLine();
            this.cursor = "(h for commands) ->";
            this.render();
        }
        if (!content.isBlank())
            this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MESSAGE,
                    this.playerId + "\n" + subject + "\n" + content
            ));
    }

    private void doMoveRoutine() {
        String pickedTiles;
        String sCol = "";
        boolean choosing = true;
        boolean exit = false;
        synchronized (this.renderLock) {
            do {
                this.printCommandLine("Write row,col r2,c2 ... to pick up to three tiles (empty to abort)");
                this.cursor = "->";
                this.render();
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
                    this.printCommandLine("Illegal character in the sequence", RED);
                }
            } while (choosing);
        }
        if (!exit) {
            synchronized (this.renderLock) {
                choosing = true;
                do {
                    try {
                        this.printCommandLine("Write the column of the shelf (right-starts from 0) (empty to abort)");
                        this.render();
                        sCol = this.scanner.nextLine();

                        if (sCol.isBlank()) {
                            exit = true;
                            break;
                        }

                        Integer.parseInt(sCol);

                        choosing = false;
                    } catch (NumberFormatException e) {
                        this.printCommandLine("Shelf chosen column is not a number", RED);
                    }
                } while (choosing);
            }
        }

        if (!exit) {
            this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MOVE, pickedTiles + "\n" + sCol));
        }

        synchronized (this.renderLock) {
            this.cursor = "(h for commands)->";
            this.render();
        }
    }

    private void showHints() {
        synchronized (this.updateLock) {
            switch (this.scene) {
                case INTERACTION -> this.printCommandLine("c: Create a game\nj: Join a game\ne: Exit\nu: Update view");
                case GAME -> this.printCommandLine("p: Pick tiles\ns: Send message\ne: Leave game\nu: Update view");
            }
        }
    }

    private void render() {
        /* Current values of gameplay info */
        Tile[][] currentBoardState;
        List<Pair> currentOldCmd;
        Map<String, Tile[][]> currentPlayerShelves;
        String currentPlayerId;
        String currentFirstPlayer;
        String currentPlayerOnTurn;
        Map<String, Integer> currentPointsValues;
        String currentCursor;
        boolean currentIsLastTurn;
        List<Message> currentChat;
        Map<String, Token> currentCommonGoals;
        Map<String, Token> currentAchievedCommonGoals;
        Tile[][] currentPersonalGoals;
        GameStatus currentGameStatus;

        /* Retrieve current values of gameplay info */
        synchronized (this.updateLock) {
            currentCursor = this.cursor;
            currentOldCmd = new ArrayList<>(this.oldCmds);
            currentPlayerId = this.playerId;
            currentBoardState = this.boardState;
            currentPlayerShelves = new HashMap<>(this.playerShelves);
            currentPlayerOnTurn = this.playerOnTurn;
            currentFirstPlayer = this.firstTurnPlayer;
            currentPointsValues = new HashMap<>(this.pointsValues);
            currentIsLastTurn = this.isLastTurn;
            currentChat = new ArrayList<>(this.chat);
            currentCommonGoals = new HashMap<>(this.commonGoals);
            currentAchievedCommonGoals = new HashMap<>(this.achievedCommonGoals);
            currentPersonalGoals = this.personalGoalsDescription;
            currentGameStatus = this.status;
        }

        /* Then update graphics on retrieved values */
        synchronized (this.renderLock) {

            Arrays.stream(this.canvas).forEach(a -> Arrays.fill(a, ' '));
            Arrays.stream(this.canvasColors).forEach(a -> Arrays.fill(a, DEFAULT));

            if (this.scene == Scene.GAME) {
                if (currentGameStatus == GameStatus.MATCHMAKING || currentGameStatus == GameStatus.STARTED || currentGameStatus == GameStatus.SUSPENDED) {
                    drawChat(this.playerId, currentChat, this.canvas, this.canvasColors);
                    drawShelves(currentPlayerShelves, currentFirstPlayer, currentPlayerId, currentPlayerOnTurn, currentPointsValues, this.canvas, this.canvasColors);
                }
                if (currentGameStatus == GameStatus.MATCHMAKING || currentGameStatus == GameStatus.SUSPENDED) {
                    drawCenteredString("WAITING FOR OTHER PLAYERS TO JOIN", 0, 32, 80, GREEN, this.canvas, this.canvasColors);
                }
                if (currentGameStatus == GameStatus.STARTED) {
                    drawLastTurn(currentIsLastTurn, this.canvas, this.canvasColors);
                    drawLivingRoom(currentBoardState, this.canvas, this.canvasColors);
                    drawCommonGoals(currentCommonGoals, currentAchievedCommonGoals, this.canvas, this.canvasColors);
                    drawPersonalGoal(currentPersonalGoals, this.canvas, this.canvasColors);
                }
            } else if (this.scene == Scene.LEADERBOARD) {
                drawGameEnd(currentPointsValues, this.canvas, this.canvasColors);
            }

            drawBox(0, 0, renderHeight, renderWidth, DEFAULT, this.canvas, this.canvasColors);
            drawCommandLine(currentCursor, currentOldCmd, this.canvas, this.canvasColors);

            ClearScreen(s -> this.printCommandLine(s, RED));

            for (int i = 0; i < this.canvas.length; i++) {
                for (int j = 0; j < this.canvas[0].length; j++) {
                    this.out.print(this.renderPixel(i, j));
                }
                this.out.println();
            }

            this.out.print("\033[" + (cursorY) + ";" + (cursorX + this.cursor.length() + 2) + "H");
        }
    }


    /*------------------- OTHER GRAPHICS ----------------------*/

    private String renderPixel(int x, int y) {
        return "\u001B[" + this.canvasColors[x][y] + "m" + this.canvas[x][y] + "\u001B[0m";
    }

    private void printCommandLine(String toPrint) {
        this.printCommandLine(toPrint, DEFAULT);
    }

    private void printCommandLine(String toPrint, int colour) {
        String[] lines = toPrint.split("\n");

        synchronized (this.updateLock) {
            for (String s : lines)
                this.oldCmds.add(new Pair(s, colour));

            while (this.oldCmds.size() > 8)
                this.oldCmds.remove(0);
        }
    }

    @Override
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public GameGraphics getGameGraphics() {
        return this;
    }

    @Override
    public void showConnection(String error) {
        synchronized (this.renderLock) {
            this.scene = Scene.CONNECTION;
            this.cursor = "Connect with RMI (r) or SOCKET (s)?, empty to exit:";

            if (!error.isBlank())
                this.printCommandLine(error, RED);
        }
        this.render();
    }

    @Override
    public void showServerInteraction(String message) {
        synchronized (this.renderLock) {
            this.scene = Scene.INTERACTION;
            this.cursor = "(h for commands)->";

            if (!message.isBlank())
                this.printCommandLine(message);
        }
        this.render();
    }

    @Override
    public void showJoin(String error) {
        synchronized (this.renderLock) {
            this.scene = Scene.JOIN;
            this.cursor = "Write playerId and gameId (empty to exit):";

            if (!error.isBlank())
                this.printCommandLine(error, RED);
        }
        this.render();
    }

    @Override
    public void showCreate(String error) {
        synchronized (this.renderLock) {
            this.scene = Scene.CREATE;
            this.cursor = "Write gameId and playerNumber (empty to exit):";

            if (!error.isBlank())
                this.printCommandLine(error, RED);
        }
        this.render();
    }

    @Override
    public void showGame(String message) {
        synchronized (this.updateLock) {
            this.scene = Scene.GAME;
            this.cursor = "(h for commands)->";
        }

        if (!message.isBlank())
            this.printCommandLine(message);

        this.render();
    }

    @Override
    public void exit() {
        this.inputThread.interrupt();
        System.exit(0);
    }

    @Override
    public void resetGameGraphics(String playerId) {
        synchronized (this.updateLock) {
            this.playerId = playerId;
            this.playerOnTurn = "";
            this.boardState = new Tile[9][9];
            for (Tile[] row : this.boardState)
                Arrays.fill(row, Tile.EMPTY);
            this.personalGoalsDescription = new Tile[6][5];
            for (Tile[] row : this.personalGoalsDescription)
                Arrays.fill(row, Tile.EMPTY);
            this.playerShelves = new HashMap<>();
            this.pointsValues = new HashMap<>();
            this.isLastTurn = false;
            this.chat = new ArrayList<>();
            this.commonGoals = new HashMap<>();
            this.achievedCommonGoals = new HashMap<>();
            this.personalGoals = new HashMap<>();
            this.status = GameStatus.MATCHMAKING;
        }
    }

    @Override
    public void updateBoardGraphics(Tile[][] board) {
        synchronized (this.updateLock) {
            this.boardState = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
        }
        this.render();
    }

    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        synchronized (this.updateLock) {
            this.playerShelves.put(playerId, Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new));
        }
        this.render();
    }

    @Override
    public void updateGameInfoGraphics(GameStatus status, String firstTurnPlayer, String playerOnTurn, boolean isLastTurn, Map<String, Integer> pointsValues) {
        synchronized (this.updateLock) {
            this.status = status;
            this.firstTurnPlayer = firstTurnPlayer;
            this.playerOnTurn = playerOnTurn;
            this.pointsValues = new HashMap<>(pointsValues);
            this.isLastTurn = isLastTurn;

            if (status == GameStatus.ENDED) {
                this.cursor = "Press enter to go back to the home page";
                this.scene = Scene.LEADERBOARD;
            }
        }
        this.render();
    }

    @Override
    public void updatePlayerChatGraphics(List<Message> chat) {
        synchronized (this.updateLock) {
            this.chat = chat;
        }
        this.render();
    }

    @Override
    public void updateCommonGoalGraphics(String id, String description, Token tokenState) {
        synchronized (this.updateLock) {
            this.commonGoals.put(id, tokenState);
        }
        this.render();
    }

    @Override
    public void updateAchievedCommonGoals(Map<String, Token> achievedCommonGoals) {
        synchronized (this.updateLock) {
            this.achievedCommonGoals = achievedCommonGoals;
        }
        this.render();
    }

    @Override
    public void updateErrorState(String reportedError) {
        synchronized (this.updateLock) {
            if (!reportedError.isBlank())
                this.printCommandLine(reportedError, RED);
        }
        this.render();
    }

    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description) {
        synchronized (this.updateLock) {
            if (!this.personalGoals.containsKey(id)) {
                Tile[][] temp = new Tile[6][5];
                for (int i = 0; i < description.length; i++)
                    for (int j = 0; j < description[i].length; j++) {
                        if (description[i][j] != Tile.EMPTY) {
                            temp[i][j] = description[i][j];
                        } else {
                            temp[i][j] = this.personalGoalsDescription[i][j];
                        }
                    }
                this.personalGoalsDescription = temp;
            }

            this.personalGoals.put(id, hasBeenAchieved);
        }
        this.render();
    }

    private enum Scene {
        CONNECTION,
        INTERACTION,
        JOIN,
        CREATE,
        GAME,
        LEADERBOARD,
    }

    record Pair(String string, int colour) {
    }
}
