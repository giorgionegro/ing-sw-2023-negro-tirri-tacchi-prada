package view;

import model.Tile;
import model.abstractModel.Message;
import modelView.LivingRoomInfo;
import modelView.PlayerChatInfo;
import modelView.ShelfInfo;

import java.io.PrintStream;
import java.util.*;

public class CLI {

    static PrintStream out = System.out;
    private LivingRoomInfo currentLivingRoom;
    private PlayerChatInfo currentPlayerChat;
    private String thisPlayerId;
    private final Map<String,ShelfInfo> currentShelfs;
    static final int renderHeight = 50;
    static final int renderWidth = 150;
    char[][] cliPixel = new char[renderHeight][renderWidth];
    int[][] cliPixelColor = new int[renderHeight][renderWidth];

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

    public static int WHITE = 37;
    public static int GREEEN = 32;
    public static int YELLOW = 33;
    public static int BLUE = 34;
    public static int MAGENTA = 35;
    public static int CYAN = 36;

    public static int RED = 31;

    public static int DEFAULT = 39;

    private void initBox() {
        //draw border
        for (int i = 0; i < renderHeight; i++) {
            for (int j = 0; j < renderWidth; j++) {
                if (i == 0 || i == renderHeight-1) {
                    cliPixel[i][j] = '─';
                    cliPixelColor[i][j] = DEFAULT;
                } else if (j == 0 || j == renderWidth-1) {
                    cliPixel[i][j] = '│';
                    cliPixelColor[i][j] = DEFAULT;
                } else {
                    cliPixel[i][j] = ' ';
                    cliPixelColor[i][j] = DEFAULT;
                }
            }
        }

        cliPixel[0][0] = '┌';
        cliPixel[0][renderWidth-1] = '┐';
        cliPixel[renderHeight-1][0] = '└';
        cliPixel[renderHeight-1][renderWidth-1]='┘';
    }

    private void render() {
        //flush the console
        ClearConsole();
        for (int i = 0; i < cliPixel.length; i++) {
            for (int j = 0; j < cliPixel[0].length; j++) {
                out.print(renderPixel(i,j));
            }
            out.println();
        }
    }

    private String renderPixel(int x, int y){
        return "\u001B["+cliPixelColor[x][y]+"m"+cliPixel[x][y]+"\u001B[0m";
    }
    public static void ClearConsole(){
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
                String top="";
                if(i==0 && j==0){//tor right
                    top = topLeft;
                } else if (i==0&&j!=(board[0].length-1)) {//top center
                    top=topCenter;
                } else if (i==0&&j==(board[0].length-1)) {
                    top=topRight;
                } else if (i!=0&&j==0) {
                    top=centerLeft;
                } else if (i!=0&&j!=(board[0].length-1)) {
                    top=centerCenter;
                } else if (i!=0&&j==(board[0].length-1)) {
                    top=centerRight;
                }
                String middle = "│   ";
                if (!tile.equals("Empty"))
                    middle="│███";

                if(j==board[0].length-1)
                    middle+="│";

                for (int c =0; c<top.length();c++) {
                    cliPixel[1+i*2][1+j*4+c]=top.charAt(c);
                    cliPixelColor[1+i*2][1+j*4+c]=DEFAULT;
                }
                for(int c=0; c<middle.length();c++)
                    {
                        cliPixel[2+i*2][1+j*4+c]=middle.charAt(c);
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
                    String top="";
                    if(i==0){//tor right
                        top = tops;
                    } else if (j==0) {
                        top=centerLeft;
                    } else if (j!=(shelf[0].length-1)) {
                        top=centerCenter;
                    } else if (j==(shelf[0].length-1)) {
                        top=centerRight;
                    }
                    String middle = "│   ";
                    if (colour!=DEFAULT)
                        middle="│███";

                    if(j==shelf[0].length-1)
                        middle+="│";

                    for (int c =0; c<top.length();c++) {
                        cliPixel[1+i*2][start+1+j*4+c]=top.charAt(c);
                        cliPixelColor[1+i*2][start+1+j*4+c]=DEFAULT;
                    }
                    for(int c=0; c<middle.length();c++)
                    {
                        cliPixel[2+i*2][start+1+j*4+c]=middle.charAt(c);
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
            if (shelf.getPlayerId().equals(thisPlayerId)) {
                drawString("YOU", 1+shelfTile.length*2 +1, margin + i * (4*shelfTile[0].length+1) + 1, DEFAULT, shelfTile[0].length*4+1-2);
            } else {
                drawString(shelf.getPlayerId(), 1+shelfTile.length*2 +1 , margin + i * (4*shelfTile[0].length+1) +1, DEFAULT, shelfTile[0].length*4+1-2);
            }
        }
    }

    //draw String from start coordinate
    private void drawString(String toDraw, int startRow, int startCol, int color, int size) {
        if(toDraw.length()>size)
            toDraw = toDraw.substring(0,size);

        for(int i=0; i<toDraw.length(); i++) {
            cliPixel[startRow][startCol+i] = toDraw.charAt(i);
            cliPixelColor[startRow][startCol+1] = color;
        }
    }
}