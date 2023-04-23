package view;

import model.Tile;
import model.abstractModel.Message;
import modelView.*;

import java.io.PrintStream;
import java.util.*;

public class CLI {

    static final PrintStream out = System.out;
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;
    private String thisPlayerId;
    private final Map<String,ShelfInfo> currentShelfs;
    static final int renderHeight = 50;
    static final int renderWidth = 150;
    final char[][] cliPixel = new char[renderHeight][renderWidth];
    final int[][] cliPixelColor = new int[renderHeight][renderWidth];
    private CommonGoalInfo commonGoal;
    private GameInfo currentGameState;

    public CLI() {
        currentShelfs = new HashMap<>();
        initBox();
    }


    public void setThisPlayerId(String playerId) {
        this.thisPlayerId = playerId;
    }

    public void updateLivingRoom(LivingRoomInfo lR) {
        //set current living room
        this.currentLivingRoom = lR;
        drawBoard();

        render();
    }

    public void updateShelf(ShelfInfo sV) {
        //set current shelf

        currentShelfs.put(sV.getPlayerId(),sV);

        drawShelfs();

        render();
    }

    public void updatePlayerChat(PlayerChatInfo pC) {//TODO: fix this
        //set current player chat
        this.currentPlayerChat = pC;
        drawChat();

        render();
    }

    public static final int WHITE = 37;
    public static final int GREEEN = 32;
    public static final int YELLOW = 33;
    public static final int BLUE = 34;
    public static final int MAGENTA = 35;
    public static final int CYAN = 36;

    public static final int RED = 31;

    public static final int DEFAULT = 39;

    private void initBox() {
        //draw border
//        for (int i = 0; i < renderHeight; i++) {
//            for (int j = 0; j < renderWidth; j++) {
//                if (i == 0 || i == renderHeight-1) {
//                    cliPixel[i][j] = '─';
//                    cliPixelColor[i][j] = DEFAULT;
//                } else if (j == 0 || j == renderWidth-1) {
//                    cliPixel[i][j] = '│';
//                    cliPixelColor[i][j] = DEFAULT;
//                } else {
//                    cliPixel[i][j] = ' ';
//                    cliPixelColor[i][j] = DEFAULT;
//                }
//            }
//        }
//
//        cliPixel[0][0] = '┌';
//        cliPixel[0][renderWidth-1] = '┐';
//        cliPixel[renderHeight-1][0] = '└';
//        cliPixel[renderHeight-1][renderWidth-1]='┘';
        drawBox(0,0, renderHeight, renderWidth, DEFAULT);
    }



    private String renderPixel(int x, int y){
        return "\u001B["+cliPixelColor[x][y]+"m"+cliPixel[x][y]+"\u001B[0m";
    }
    public static void ClearScreen(){

        try{
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void drawChat() {
        List<Message> messages = currentPlayerChat.getMessages();
        int startMessage = messages.size() - 5;
        if (startMessage < 0) {
            startMessage = 0;
        }

        List<Message> messagesToDraw = messages.subList(startMessage, messages.size());
        //reverse the list
        Collections.reverse(messagesToDraw);

        for (int i = 0; i < messagesToDraw.size(); i++) {
            String idSender = messagesToDraw.get(i).getSender();
            String message = messagesToDraw.get(i).getText();
            //crop
            if (idSender.length() > 10) {
                idSender = idSender.substring(0, 10);
            }
            if (message.length() > 20) {
                message = message.substring(0, 20);
            }
            //concatenate
            String toDraw = idSender + ": " + message;
            //if subject and sender is not idPlayer color it in red
            int color = DEFAULT;
            if (!messagesToDraw.get(i).getSubject().equals(thisPlayerId) && !messagesToDraw.get(i).getSender().equals(thisPlayerId)) {
                color = RED;
            }
            //draw the message
            drawString(toDraw, renderHeight - 2 - i, renderWidth - 2 - 20, color,20);
        }
    }


    private void drawBoard() {
        final String topLeft = "┌───";
        final String topCenter = "┬───";
        final String topRight = "┬───┐";
        final String centerLeft ="├───";

        final String centerCenter ="┼───";

        final String centerRight ="┼───┤";

        Tile[][] board = currentLivingRoom.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int color;
                if (board[i][j] == null)
                    board[i][j] = Tile.EMPTY;
                String tile = board[i][j].getColor();
                switch (tile) {
                    case "Green" -> color = GREEEN;
                    case "White" -> color = WHITE;
                    case "Yellow" -> color = YELLOW;
                    case "Blue" -> color = BLUE;
                    case "LightBlue" -> color = CYAN;
                    case "Magenta" -> color = MAGENTA;
                    default-> color = DEFAULT;//Empty

                }            //draw block of color  if not empty
                String first="";//first part of the block
                if(i==0 && j==0){//tor right
                    first = topLeft;
                } else if (i==0&&j!=(board[0].length-1)) {//top center
                    first=topCenter;
                } else if (i==0&&j==(board[0].length-1)) {
                    first=topRight;
                } else if (i!=0&&j==0) {
                    first=centerLeft;
                } else if (i!=0&&j!=(board[0].length-1)) {
                    first=centerCenter;
                } else if (i!=0&&j==(board[0].length-1)) {
                    first=centerRight;
                }
                String second = "│   ";
                if (!tile.equals("Empty"))
                    second="│███";

                if(j==board[0].length-1)//if last column add right border
                    second+="│";

                for (int c =0; c<first.length();c++) {
                    cliPixel[1+i*2][1+j*4+c]=first.charAt(c);
                    cliPixelColor[1+i*2][1+j*4+c]=DEFAULT;
                }
                for(int c=0; c<second.length();c++)
                    {
                        cliPixel[2+i*2][1+j*4+c]=second.charAt(c);
                        cliPixelColor[2+i*2][1+j*4+c]=color;
                    }
                cliPixelColor[2+i*2][1+j*4]=DEFAULT;
                cliPixel[board.length*2+1][j*4+1]='┴';
                cliPixel[board.length*2+1][j*4+2]='─';
                cliPixel[board.length*2+1][j*4+3]='─';
                cliPixel[board.length*2+1][j*4+4]='─';
            }
        }
        cliPixel[board.length*2+1][1]='└';
        cliPixel[board.length*2+1][board[0].length*4+1]='┘';
    }

    private void drawShelfs() {
        final String tops = "    ";
        final String centerLeft ="├───";
        final String centerCenter ="┼───";
        final String centerRight ="┼───┤";

        int margin = 1 + 4*currentLivingRoom.getBoard()[0].length + 1 + 1;

        for (int s = 0; s < currentShelfs.size(); s++) {
            Tile[][] shelf = currentShelfs.values().stream().toList().get(s).getShelf();
            int start = margin + s * shelf[0].length*4 + s;
            for (int i = 0; i < shelf.length; i++) {
                for (int j = 0; j < shelf[0].length; j++) {
                    int colour;

                    if (shelf[i][j] == null) //TODO da sistemare quando risolto
                        shelf[i][j] = Tile.EMPTY;

                    switch (shelf[i][j].getColor()) {
                        case "Green" -> colour = GREEEN;
                        case "White" -> colour = WHITE;
                        case "Yellow" -> colour = YELLOW;
                        case "Blue" -> colour = BLUE;
                        case "LightBlue" -> colour = CYAN;
                        case "Magenta" -> colour = MAGENTA;
                        default -> colour = DEFAULT;
                    }
                    String first="";
                    if(i==0){//tor right
                        first = tops;
                    } else if (j==0) {
                        first=centerLeft;
                    } else if (j!=(shelf[0].length-1)) {
                        first=centerCenter;
                    } else if (j==(shelf[0].length-1)) {
                        first=centerRight;
                    }
                    String second = "│   ";
                    if (colour!=DEFAULT)
                        second="│███";

                    if(j==shelf[0].length-1)
                        second+="│";

                    for (int c =0; c<first.length();c++) {
                        cliPixel[1+i*2][start+1+j*4+c]=first.charAt(c);
                        cliPixelColor[1+i*2][start+1+j*4+c]=DEFAULT;
                    }
                    for(int c=0; c<second.length();c++)
                    {
                        cliPixel[2+i*2][start+1+j*4+c]=second.charAt(c);
                        cliPixelColor[2+i*2][start+1+j*4+c]=colour;
                    }
                    cliPixelColor[2+i*2][start+1+j*4]=DEFAULT;
                    cliPixel[shelf.length*2+1][start+j*4+1]='┴';
                    cliPixel[shelf.length*2+1][start+j*4+2]='─';
                    cliPixel[shelf.length*2+1][start+j*4+3]='─';
                    cliPixel[shelf.length*2+1][start+j*4+4]='─';
                }
            }
            cliPixel[shelf.length*2+1][start+1]='└';
            cliPixel[shelf.length*2+1][start+shelf[0].length*4+1]='┘';
        }
        //draw under each shelf you if you are in that shelf or the number of player in the shelf
        for (int i = 0; i < currentShelfs.size(); i++) {
            ShelfInfo shelf = currentShelfs.values().stream().toList().get(i);
            Tile[][] shelfTile = shelf.getShelf();

        String toDraw = "";
        if (shelf.getPlayerId().equals(thisPlayerId)) {
            toDraw = "YOU";
        } else {
            toDraw = shelf.getPlayerId();

        }
        if (currentGameState!=null && currentGameState.getPlayerOnTurn().equals(shelf.getPlayerId())) {
            toDraw = ">"+toDraw+"<";
        }
        else {
            toDraw = " "+toDraw+" ";
        }
        drawString(toDraw, 1+shelfTile.length*2 +1 , margin + i * (4*shelfTile[0].length+1) +1, DEFAULT, shelfTile[0].length*4+1-2);


    }
    }

    //draw String from start coordinate
    private void drawString(String toDraw, int Row, int startCol, int colour, int size) {
        if(toDraw.length()>size)
            toDraw = toDraw.substring(0,size);

        for(int i=0; i<toDraw.length(); i++) {
            cliPixel[Row][startCol+i] = toDraw.charAt(i);
            cliPixelColor[Row][startCol+i] = colour;
        }
    }
    //move cursor to arbitrary position
    public static void moveCursor(int x, int y) {
        System.out.print("\033[" + x + ";" + y + "H");
    }

    private void drawBox(int x, int y, int height, int width, int colour) {//TODO implements colour
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) cliPixel[x + i][y + j] = '┌';
                else if (i == 0 && j == width - 1) cliPixel[x + i][y + j] = '┐';
                else if (i == height - 1 && j == 0) {
                    cliPixel[x + i][y + j] = '└';
                } else if (i == height - 1 && j == width - 1) {
                    cliPixel[x + i][y + j] = '┘';
                } else if (i == 0) {
                    cliPixel[x + i][y + j] = '─';
                } else if (i == height - 1) {
                    cliPixel[x + i][y + j] = '─';
                } else if (j == 0) {
                    cliPixel[x + i][y + j] = '│';
                } else if (j == width - 1) {
                    cliPixel[x + i][y + j] = '│';
                }
                else {
                    cliPixel[x + i][y + j] = ' ';
                }
            }
        }
    }


    //draw command line box 10*50 in the bottom left corner

    private void drawCommandLine() {
        drawBox(renderHeight-11, 2, 10, 50, DEFAULT);
        drawString("Command Line", renderHeight -10, 3, DEFAULT, 50 - 2);
        drawString(">", renderHeight - 3, 3, DEFAULT, 50 - 2);
        drawOldCmds();
    }



    //old cmds to be shifted up
    List<Pair> oldCmds = new ArrayList<>();
    private void drawOldCmds() {
        while (oldCmds.size()>5)
            oldCmds.remove(0);
        for (int i = 0; i < oldCmds.size(); i++) {
            drawString(oldCmds.get(i).getString(), renderHeight - 9 + i, 3, oldCmds.get(i).getColour(), 50 - 2);
        }
    }
    public String readCommandLine(String message) {
        moveCursor(renderHeight - 2, 4);
        System.out.print(message+" ");
        Scanner scanner = new Scanner(System.in);
        String cmd= scanner.nextLine();
        oldCmds.add(new Pair(message+" "+cmd, DEFAULT));
        //trim old cmds to 8
        while (oldCmds.size()>8)
            oldCmds.remove(0);
        drawCommandLine();
        render();
        //move cursor back to command line
        moveCursor(renderHeight - 2, 5);
        return cmd;
    }

    public void printCommandLine(String toPrint) {
     oldCmds.add(new Pair(toPrint, DEFAULT));
        while (oldCmds.size()>8)
            oldCmds.remove(0);
        drawCommandLine();
        render();
        moveCursor(renderHeight - 2, 5);
    }
    public void printCommandLine(String toPrint, int colour) {
        oldCmds.add(new Pair(toPrint, colour));
        while (oldCmds.size()>8)
            oldCmds.remove(0);
        drawCommandLine();
        render();
        moveCursor(renderHeight - 2, 5);
    }
    private void render() {

        ClearScreen();
        if (currentGameState != null) {
            drawGameState();
        }
        for (int i = 0; i < cliPixel.length; i++) {
            for (int j = 0; j < cliPixel[0].length; j++) {
                out.print(renderPixel(i,j));
            }
            out.println();
        }
        moveCursor(renderHeight - 2, 5);

    }


    public void updateCommonGoal(CommonGoalInfo o) {
        commonGoal = o;
        drawCommonGoal();
        render();
    }

    //TODO test if it works properly, spoiler: it will not with more than 1 common goal
    private void drawCommonGoal() {
        String description = commonGoal.getDescription();
        //y 52 x renderHeight-10
        drawString(description, renderHeight-10, 52, DEFAULT, 50 - 2);
        
    }

    public void updateGameState(GameInfo o) {
        currentGameState = o;
        drawGameState();
        render();
    }

    private void drawGameState() {
        //redraw shelfs with new current player
        if (currentLivingRoom!= null && currentShelfs!=null &&currentLivingRoom.getBoard()!=null && currentLivingRoom.getBoard().length>0 && currentShelfs.size()>0&& currentLivingRoom.getBoard()[0].length>0)
            drawShelfs();
        //draw is last turn if it is
        if(currentGameState.isLastTurn())
            drawString("Last Turn", renderHeight-10, 30, DEFAULT, 50 - 2);
    }

    private static class Pair {
        String string;
        int colour = DEFAULT;

        public Pair(String string, int colour) {
            this.string = string;
            this.colour = colour;
        }

        //getter and setter
        public String getString() {
            return string;
        }
        public int getColour() {
            return colour;
        }

    }
    }