package view.TUI;

import model.Tile;
import model.Token;
import model.abstractModel.Game.GameStatus;
import model.abstractModel.Message;
import modelView.PickedTile;
import view.ViewLogic;
import view.graphicInterfaces.AppGraphics;
import view.graphicInterfaces.GameGraphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.*;

import static view.TUI.TUIdraw.*;
import static view.TUI.TUIutils.*;


/**
 * This class is the Text User Interface of the game.
 */
public class TUI implements AppGraphics, GameGraphics {

    /**
     * Render height of the TUI
     */
    static final int renderHeight = 53;
    /**
     * Render width of the TUI
     */
    static final int renderWidth = 140;
    /**
     * X Position of the cursor on the TUI
     */
    static final int cursorX = 3;
    /**
     * Y Position of the cursor on the TUI
     */
    static final int cursorY = 51;
    /**
     * Stream used to print on the TUI
     */
    private final PrintStream out = System.out;
    /**
     * Scanner used to read from the TUI
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     * History of the commands executed and printed on the TUI
     */
    private final List<Pair> oldCmds = new ArrayList<>();
    /**
     * Canvas of the TUI, tile matrix of chars
     */
    private final char[][] canvas = new char[renderHeight][renderWidth];
    /**
     * color canvas of the TUI, tile matrix of int
     */
    private final int[][] canvasColors = new int[renderHeight][renderWidth];
    /**
     * Lock used to synchronize the render of the TUI
     */
    private final Object renderLock = new Object();
    /**
     * Lock used to synchronize the updates of the fields of the TUI
     */
    private final Object updateLock = new Object();
    /**
     * Current cursor of the TUI
     */
    private String cursor = "";
    /**
     * Current scene of the TUI
     */
    private Scene scene;
    /**
     * Action listener of the TUI for handling events
     */
    private ActionListener actionListener;
    /**
     * current player id of the game
     */
    private String playerId;
    /**
     * current board state of the game
     */
    private Tile[][] boardState;
    /**
     * current players shelf of the game
     */
    private Map<String, Tile[][]> playerShelves;
    /**
     * current player on turn
     */
    private String playerOnTurn;
    /**
     * player that had the first turn
     */
    private String firstTurnPlayer;
    /**
     * players points
     */
    private Map<String, Integer> pointsValues;

    /**
     * is the last turn of the game
     */
    private boolean isLastTurn;
    /**
     * current game status
     */
    private GameStatus status;
    /**
     * current chat of the game
     */
    private List<? extends Message> chat;
    /**
     * current common goals of the game
     */
    private Map<String, Token> commonGoals;

    /**
     *  player achieved common goals
     */

    private Map<String, Token> achievedCommonGoals;

    /**
     * personal goal description
     */
    private Tile[][] personalGoalsDescription;

    /**
     * Thread used to read the input from the TUI
     */
    private final Thread inputThread = new Thread(() -> {
        while (true) {
            String cmd = this.scanner.nextLine();
            //we print this command on the command line to preserve it in the history
            this.printCommandLine(this.cursor + " " + cmd);
            //we dispatch the command to react to it
            this.dispatchInput(cmd);
        }
    });
    /**
     * personal goals
     */
    private Map<Integer, Boolean> personalGoals;

    /**
     * Constructor of the TUI
     */
    public TUI() {
        super();
        this.inputThread.start();
        this.resetGameGraphics("");
    }

    /**
     * execute read command
     * @param cmd command read from the TUI
     */
    private void dispatchInput(String cmd) {
        switch (this.scene) {
            case CONNECTION -> {
                if (cmd.isBlank())
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.EXIT, ""));
                else {
                    //we read if the command is a valid connection command, then we notify the view logic of the network choice
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
                    //we read if the command is a valid interaction command, then we notify the view logic of the interaction choice via an action event

                    //but if help command is read, we show the hints without notifying the view logic
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

                if (cmd.isBlank()) { // if the command is blank, we ask the view logic to go back to the home scene
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
                } else {
                    //we read if the command is a valid creation command, then we send the relevant parameters to the view logic

                    if (cmd.split(" ").length != 2) {
                        this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_CREATE, "Wrong number of parameter (2 required)"));
                    } else {
                        this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.CREATE, "STANDARD " + cmd));
                    }
                }
            }
            case JOIN -> {
                if (cmd.isBlank()) { // if the command is blank, we ask the view logic to go back to the home scene
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
                } else {
                    //we read if the command is a valid join command, then we send the relevant parameters to the view logic
                    this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.JOIN, cmd));
                }
            }
            case GAME -> {
                switch (cmd) {
                    case "h" -> { // if the command is help, we show the hints without notifying the view logic
                        this.showHints();
                        this.render();
                    }
                    //if the command is pick, we handle the pick routine, then we notify the view logic
                    case "p" -> this.doMoveRoutine();
                    //same for the place command
                    case "s" -> this.sendMessageRoutine();
                    //for the leave command, we notify the view logic to leave the game
                    case "e" -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.LEAVE_GAME, ""));
                    //else we just render the game scene
                    default -> this.render();
                }
            }
            //if the scene is the leaderboard, we just return to the home scene
            case LEADERBOARD -> this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.ROUTE_HOME, ""));
        }
    }

    /**
     * start routine to send a message
     */
    private void sendMessageRoutine() {
        String receiver;
        String content;
        synchronized (this.renderLock) {
            this.cursor = "To (empty for everyone): ";
            this.render();
            receiver = this.scanner.nextLine();
            //we print this command on the command line to preserve it in the history
            this.printCommandLine(this.cursor + " " + receiver);
            this.cursor = "Message content (empty to abort): ";
            this.render();
            content = this.scanner.nextLine();
            //we reset the cursor to the default one
            this.cursor = "(h for commands) ->";
            this.render();
        }
        if (!content.isBlank())//if the content is not blank, we notify the view logic of the message to send
            this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MESSAGE,
                    this.playerId + "\n" + receiver + "\n" + content
            ));
    }

    /**
     * start routine to do a move
     */
    private void doMoveRoutine() {
        String pickedTiles;
        String sCol = "";
        boolean choosing = true;
        boolean exit = false;
        List<PickedTile> tTiles = new ArrayList<>();

        synchronized (this.renderLock) {
            do {
                tTiles.clear();
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
                        var x = Integer.parseInt(split1[0]);
                        var y = Integer.parseInt(split1[1]);
                        //we add the picked tile to the list of picked tiles for the checks
                        tTiles.add(new PickedTile(x, y));
                    }
                    if (this.pickable(tTiles, this.boardState)) {
                        //if the tiles are pickable, we move on
                        choosing = false;
                    }

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
                        this.printCommandLine("Write the column of the shelf (empty to abort)");
                        this.render();
                        sCol = this.scanner.nextLine();

                        if (sCol.isBlank()) {
                            exit = true;
                            break;
                        }

                        var sC = Integer.parseInt(sCol);
                        //we check if there is enough space in the shelf
                        int emptyTiles = Arrays.stream(this.playerShelves.get(this.playerId)).mapToInt(row -> row[sC] == Tile.EMPTY ? 1 : 0).sum();
                        if (emptyTiles < tTiles.size()) {
                            this.printCommandLine("Not enough space in the shelf", RED);
                            continue;
                        }
                        //if there is enough space, we move on
                        choosing = false;
                    } catch (NumberFormatException e) {
                        this.printCommandLine("Shelf chosen column is not a number", RED);
                    }
                } while (choosing);
            }
        }
        //if the user did not abort, we notify the view logic of the move
        if (!exit) {
            this.actionListener.actionPerformed(new ActionEvent(this, ViewLogic.SEND_MOVE, pickedTiles + "\n" + sCol));
        }

        synchronized (this.renderLock) {
            //we reset the cursor to the default one
            this.cursor = "(h for commands)->";
            this.render();
        }
    }

    /**
     * Shows hints for the current scene
     */
    private void showHints() {
        synchronized (this.updateLock) {
            switch (this.scene) {
                case INTERACTION -> this.printCommandLine("c: Create a game\nj: Join a game\ne: Exit\nu: Update view");
                case GAME -> this.printCommandLine("p: Pick tiles\ns: Send message\ne: Leave game\nu: Update view");
            }
        }
    }

    /**
     * Update and render the canvas
     */
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

            //clear the canvases
            Arrays.stream(this.canvas).forEach(a -> Arrays.fill(a, ' '));
            Arrays.stream(this.canvasColors).forEach(a -> Arrays.fill(a, DEFAULT));
            //we draw to the canvases the relevant information for the current scene
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
            //we clear the screen and print the canvases
            ClearScreen(s -> this.printCommandLine(s, RED));
            //we print the canvases
            this.renderCanvases();

            //we move the cursor to the correct position
            this.out.print("\033[" + (cursorY) + ";" + (cursorX + this.cursor.length() + 2) + "H");
        }
    }

    /**
     * Render the canvases
     */
    private void renderCanvases() {
        for (int i = 0; i < this.canvas.length; i++) {
            for (int j = 0; j < this.canvas[0].length; j++) {
                this.out.print(this.renderPixel(i, j));
            }
            this.out.println();
        }
    }


    /*------------------- OTHER GRAPHICS ----------------------*/

    /**
     * render single pixel
     * @param x     x coordinate of the pixel
     * @param y    y coordinate of the pixel
     * @return String representing the pixel at the given coordinates
     */
    private String renderPixel(int x, int y) {
        return "\u001B[" + this.canvasColors[x][y] + "m" + this.canvas[x][y] + "\u001B[0m";
    }

    /**
     * Print a string in the command line
     * @param toPrint  String to print
     */
    private void printCommandLine(String toPrint) {
        this.printCommandLine(toPrint, DEFAULT);
    }

    /**
     * Print a string in the command line with a given colour
     * @param toPrint  String to print
     * @param colour  Colour of the string
     */
    private void printCommandLine(String toPrint, int colour) {
        String[] lines = toPrint.split("\n");

        synchronized (this.updateLock) {
            //we add the new commands to the old ones
            for (String s : lines)
                this.oldCmds.add(new Pair(s, colour));

        }
    }

    /**
     * {@inheritDoc}
     * @param actionListener {@inheritDoc}
     */
    @Override
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public GameGraphics getGameGraphics() {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param error {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     *
     * @param message {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     * @param error {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     * @param error eventual error message to be printed
     */
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

    /**
     * {@inheritDoc}
     * @param message eventual message to be printed
     */
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

    /**
     * Exit the game
     */
    @Override
    public void exit() {
        this.inputThread.interrupt();
        System.exit(0);
    }

    /**
     * reset the game graphics for a new game
     * @param playerId {@inheritDoc}
     */
    @Override
    public void resetGameGraphics(String playerId) {
        //we reset the game graphics for a new game9
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

    /**
     * {@inheritDoc}
     * @param board board of the living room
     */
    @Override
    public void updateBoardGraphics(Tile[][] board) {
        synchronized (this.updateLock) {
            this.boardState = Arrays.stream(board).map(Tile[]::clone).toArray(Tile[][]::new);
        }
        this.render();
    }

    /**
     * {@inheritDoc}
     * @param playerId id of the player that owns the shelf
     * @param shelf    shelf representation of the player
     */
    @Override
    public void updatePlayerShelfGraphics(String playerId, Tile[][] shelf) {
        synchronized (this.updateLock) {
            this.playerShelves.put(playerId, Arrays.stream(shelf).map(Tile[]::clone).toArray(Tile[][]::new));
        }
        this.render();
    }

    /**
     * {@inheritDoc}
     * @param status          status of the game
     * @param firstTurnPlayer id of the player that has the first turn
     * @param playerOnTurn    id of the player on turn
     * @param isLastTurn      true if the game is in the last round of turns
     * @param pointsValues    points amount of each player
     */
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

    /**
     * {@inheritDoc}
     * @param chat list of messages sent to a player
     */
    @Override
    public void updatePlayerChatGraphics(List<? extends Message> chat) {
        synchronized (this.updateLock) {
            this.chat = chat;
        }
        this.render();
    }

    /**
     * {@inheritDoc}
     * @param id          the unique id of the common goal
     * @param description the description of common goal specs
     * @param tokenState  the current token value of the common goal
     */
    @Override
    public void updateCommonGoalGraphics(String id, String description, Token tokenState) {
        synchronized (this.updateLock) {
            this.commonGoals.put(id, tokenState);
        }
        this.render();
    }

    /**
     * {@inheritDoc}
     * @param achievedCommonGoals map of achieved common goals with earned token
     */
    @Override
    public void updateAchievedCommonGoals(Map<String, Token> achievedCommonGoals) {
        synchronized (this.updateLock) {
            this.achievedCommonGoals = achievedCommonGoals;
        }
        this.render();
    }

    /**
     * {@inheritDoc}
     * @param reportedError message of en error encountered during gameplay
     */
    @Override
    public void updateErrorState(String reportedError) {
        synchronized (this.updateLock) {
            if (!reportedError.isBlank())
                this.printCommandLine(reportedError, RED);
        }
        this.render();
    }

    /**
     * {@inheritDoc}
     * @param id              the id of this goal, unique among others player personal goal
     * @param hasBeenAchieved true if the goal is achieved
     * @param description     matrix representation of the goal
     */
    @Override
    public void updatePersonalGoalGraphics(int id, boolean hasBeenAchieved, Tile[][] description) {
        //we receive the personal goal description Tile by Tile, so we have to update the description matrix to sum up the received objectives
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

    /**
     * Method that determines if a list of tiles are pickable
     * @param pickedTiles list of tiles picked by the player
     * @param board board of the living room
     * @return true if the tile is pickable
     */
    public boolean pickable(List<PickedTile> pickedTiles, Tile[][] board) {
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

    /**
     * Method that determines if tiles are aligned
     * @param pickedTiles list of tiles picked by the player
     * @return true if the tiles are aligned
     */
    private boolean areTilesAligned(List<PickedTile> pickedTiles) {

        boolean rowAligned = true;
        boolean colAligned = true;
        //first check if tiles are aligned and to which axis
        for (int i = 1; i < pickedTiles.size(); i++) {
            rowAligned = rowAligned && (pickedTiles.get(i - 1).row() == pickedTiles.get(i).row());
            colAligned = colAligned && (pickedTiles.get(i - 1).col() == pickedTiles.get(i).col());
        }


        if (colAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::row));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).row() + 1 != pickedTiles.get(i + 1).row())
                    return false;
        }

        //for each axis, check if tiles are adjacent
        if (rowAligned) {
            pickedTiles.sort(Comparator.comparingInt(PickedTile::col));
            for (int i = 0; i < pickedTiles.size() - 1; i++)
                if (pickedTiles.get(i).col() + 1 != pickedTiles.get(i + 1).col())
                    return false;
        }



        //if we pass the adjacent check, we return true if at least one axis is aligned
        return rowAligned || colAligned;
    }

    /**
     * Method that determines if tiles are different
     * @param pickedTiles list of picked tiles
     * @return true if tiles are different, false otherwise
     */
    private boolean areTilesDifferent(List<PickedTile> pickedTiles) {
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
     * Method that determines if a tile is in a pickable position
     * @param row    row of the tile
     * @param column column of the tile
     * @param board  board to check
     * @return true if tile is pickable, false otherwise
     */
    private boolean isTilePickable(int row, int column, Tile[][] board) {
        //if the tile is empty or null, it is not pickable
        if (row < 0 || column < 0 || row > board.length - 1 || column > board[row].length - 1 || board[row][column] == Tile.EMPTY || board[row][column] == null)
            return false;

        //if is to the edge of the board it is pickable
        if (row == 0 || column == 0 || row == board.length - 1 || column == board[0].length - 1)
            return true;

        //if at least one of the adjacent tiles is empty, it is pickable
        return board[row - 1][column] == Tile.EMPTY
                || board[row + 1][column] == Tile.EMPTY
                || board[row][column - 1] == Tile.EMPTY
                || board[row][column + 1] == Tile.EMPTY;
    }


    /**
     * Enum that represents the scenes of the game
     */
    private enum Scene {
        /**
         * Scene of the connection
         */
        CONNECTION,
        /**
         * Scene of the interaction
         */
        INTERACTION,
        /**
         * Scene of the joining
         */
        JOIN,
        /**
         * Scene of the creation
         */
        CREATE,
        /**
         * Scene of the main game menu
         */
        GAME,
        /**
         * Scene of the end game
         */
        LEADERBOARD,
    }


    /**
     * Class that represents a pair of string and colour
     * @param string string to be stored
     * @param colour int that represents the colour
     */
    record Pair(String string, int colour) {
    }


}
