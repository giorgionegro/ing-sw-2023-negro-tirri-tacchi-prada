package view;

import model.Tile;
import model.abstractModel.Message;
import modelView.LivingRoomInfo;
import modelView.PlayerChatInfo;
import modelView.ShelfInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLI {

    private String[] buffer = new String[20];

    private LivingRoomInfo currentLivingRoom;
private PlayerChatInfo currentPlayerChat;

private String thisPlayerId;

private Map<String,Integer> playersInShelf;
private List<ShelfInfo> currentShelfs;


    private String[] livingRoomBuffer;
    private Map<String,String[]> playerShelfBuffer;

    private String[] playerChatBuffer;

    private String[] playerPersonalGoalBuffer;

    private String[] commonGoalBuffer;


    pixel[][] cliView = new pixel[50][50];

    public CLI(){
        this.livingRoomBuffer = new String[10];

        this.playerShelfBuffer = new HashMap<>();

        this.playerChatBuffer = new String[10];
        this.playerPersonalGoalBuffer = new String[18];
        this.commonGoalBuffer = new String[4];



        currentShelfs = new ArrayList<>();
        playersInShelf = new HashMap<>();
        initBox(this);

    }



    public void setThisPlayerId(String playerId){
        this.thisPlayerId = playerId;
    }
    public void updateLivingRoom(LivingRoomInfo lR){
        //set current living room
        this.currentLivingRoom = lR;
        drawBoard(this);

        render(cliView);
    }

    public void updateShelf(ShelfInfo sV){
        //set current shelf
        //search if shelf with same id is already in the list
        AtomicBoolean found = new AtomicBoolean(false);
        currentShelfs.stream().filter(shelfInfo -> shelfInfo.getPlayerId().equals(sV.getPlayerId())).forEach(shelfInfo -> {
            //if found update the shelf
            shelfInfo = sV;
            found.set(true);
        });
        //if not found add the shelf
        if(!found.get()){
            currentShelfs.add(sV);
            //add the player to the map
            playersInShelf.put(sV.getPlayerId(),currentShelfs.size());
        }
        drawShelfs(this);

        render(cliView);
    }

    public void updatePlayerChat(PlayerChatInfo pC){//TODO: fix this
        //set current player chat
        this.currentPlayerChat = pC;
        drawChat(this);

        render(cliView);
    }

    public void updateCLI(){
        render(cliView);
    }

    public static int WHITE = 37;
    public static int GREEEN = 32;
    public static int YELLOW = 33;
    public static int BLUE = 34;
    public static int MAGENTA = 35;
    public static int CYAN = 36;

    public static int DEFAULT = 39;

    public static String coloredText(int colorInt, String text){
        return (char) 27 + "["+colorInt+"m" + text + (char) 27 +"[39m";
    }





    private static void initBox(CLI cli)
    {
        //draw border
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if(i == 0 || i == 49 ){
                    cli.cliView[i][j] = new pixel((" "+j+" ").substring(0,3).toCharArray(), "0");
                }
                else if(j == 0 || j == 49 ){
                    cli.cliView[i][j] = new pixel((" "+i+" ").substring(0,3).toCharArray(), "0");
                }
                else{
                    cli.cliView[i][j] = new pixel("   ".toCharArray(),"0");
                }
            }
        }
    }
    private static void render(pixel[][] cliView) {
        //flush the console
        System.out.print("\033[H\033[2J");
        for (int i = 0; i < 50; i++) {
        for (int j = 0; j < 50; j++) {
            System.out.print(cliView[i][j].toString());
        }
        System.out.println();
    }
}

    private static void drawChat(CLI cli) {
        List<Message> messages = cli.currentPlayerChat.getMessages();
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
            //size of the message
            int size = toDraw.length();
            //if subject and sender is not idPlayer color it in red
            String color = "0";
            if (!messagesToDraw.get(i).getSubject().equals(cli.thisPlayerId) && !messagesToDraw.get(i).getSender().equals(cli.thisPlayerId) ) { color = "31";
            }
            //draw the message
            drawStringFromEnd(cli, toDraw, 48 -i, 48, color);
        }
    }

    private static void drawBoard(CLI cli) {
        for (int i = 0; i < cli.currentLivingRoom.getBoard().length; i++) {
            for (int j = 0; j < cli.currentLivingRoom.getBoard()[0].length; j++) {
                String color = "0";
                if(cli.currentLivingRoom.getBoard()[i][j]==null)
                    cli.currentLivingRoom.getBoard()[i][j] = Tile.EMPTY;
                String tile = cli.currentLivingRoom.getBoard()[i][j].getColor();
                switch (tile) {
                    case "Green" -> color = "32";
                    case "White" -> color = "37";
                    case "Yellow" -> color = "33";
                    case "Blue" -> color = "34";
                    case "LightBlue" -> color = "36";
                    case "Magenta" -> color = "35";
                    case "Empty" -> color = "0";
                }            //draw block of color  if not empty
                if (!tile.equals("Empty")) {
                    cli.cliView[i + 1][j + 1] = new pixel("|▀|".toCharArray(), color);
                }
            }
        }

    }

    private static void drawShelfs(CLI cli) {
        int start = 44 - 6 * (cli.currentShelfs.size() - 1);
        for (int i = 0; i < cli.currentShelfs.size(); i++) {
            for (int j = 0; j < cli.currentShelfs.get(i).getShelf().length; j++) {
                for (int k = 0; k < cli.currentShelfs.get(i).getShelf()[0].length; k++) {
                    String color = "0";
                    if(cli.currentShelfs.get(i).getShelf()[j][k]==null)
                        cli.currentShelfs.get(i).getShelf()[j][k] = Tile.EMPTY;
                    String tile = cli.currentShelfs.get(i).getShelf()[j][k].getColor();
                    switch (tile) {
                        case "Green" -> color = "32";
                        case "White" -> color = "37";
                        case "Yellow" -> color = "33";
                        case "Blue" -> color = "34";
                        case "LightBlue" -> color = "36";
                        case "Magenta" -> color = "35";
                        case "Empty" -> color = "0";
                    }
                    //draw block of color  if not empty
                    if (!tile.equals("Empty")) {
                        cli.cliView[j + 1][start + k + i * 6] = new pixel("|▀|".toCharArray(), color);
                    }
                    else
                    {
                        cli.cliView[j + 1][start + k + i * 6] = new pixel("| |".toCharArray(), color);
                    }
                }
            }
        }
        //draw under each shelf you if you are in that shelf or the number of player in the shelf
        for (int i = 0; i < cli.currentShelfs.size(); i++) {
            if (cli.currentShelfs.get(i).getPlayerId().equals(cli.thisPlayerId)) {
                cli.cliView[7][start + 2 + i * 6] = new pixel("YOU".toCharArray(), new String[]{"33","33", "33"});
            } else {
                int nbPlayer = cli.playersInShelf.get(cli.currentShelfs.get(i).getPlayerId());
                cli.cliView[7][start + 2 + i * 6] = new pixel((" "+(char) (nbPlayer + 48)+ " ").toCharArray(), "0");
            }
        }
    }

    //draw String from start coordinate
    private static void drawString(CLI cli, String toDraw, int startRow, int startCol, String color) {
        int size = toDraw.length();
        List<String> toDrawSplit = new ArrayList<>();
        //split the message in 3 characters
        for (int j = 0; j < size; j += 3) {
            if (j + 3 > size) {
                toDrawSplit.add(toDraw.substring(j, size) + " ".repeat(Math.max(0, 3 - (size - j))));

            } else {
                toDrawSplit.add(toDraw.substring(j, j + 3));
            }
        }
        //draw the message
        for (int j = 0; j < toDrawSplit.size(); j++) {
            cli.cliView[startRow][startCol + j] = new pixel(toDrawSplit.get(toDrawSplit.size()-1-j).toCharArray(), new String[]{color,color,color});
        }
    }

    //draw String from end coordinate
    private static void drawStringFromEnd(CLI cli, String toDraw, int endRow, int endCol, String color) {
        int size = toDraw.length();
        List<String> toDrawSplit = new ArrayList<>();
        //split the message in 3 characters
        for (int j = 0; j < size; j += 3) {
            if (j + 3 > size) {
                toDrawSplit.add(toDraw.substring(j, size) + " ".repeat(Math.max(0, 3 - (size - j))));

            } else {
                toDrawSplit.add(toDraw.substring(j, j + 3));
            }
        }
        //reverse the message
        Collections.reverse(toDrawSplit);
        //draw the message
        for (int j = 0; j < toDrawSplit.size(); j++) {
            cli.cliView[endRow][endCol - j] = new pixel(toDrawSplit.get(j).toCharArray(), new String[]{color,color,color});
        }

    }


}
class pixel{
    public char[] c;
    public String[] colors;
    public pixel(char[] c, String[] color) {
        this.c = c;
        //if color is null, set it to default
        if (color == null) {
            this.colors = new String[]{"0", "0", "0"};
            //if color is length 1, set rest to default
        } else if (color.length == 1) {
            this.colors = new String[]{"0", color[0], "0"};
            this.colors = color;
        }
        else if (color.length == 2) {
            this.colors = new String[]{"0", color[0], color[1]};
            this.colors = color;
        }
        else {
            //trim the array to 3
            this.colors = new String[]{color[0], color[1], color[2]};
        }

    }

    public pixel(char[] c, String color) {
        this.c = c;
        //if color is null, set it to default
        if (color == null) {
            this.colors = new String[]{"0", "0", "0"};
            //if color is length 1, set rest to default
        } else  {
            this.colors = new String[]{"0", color, "0"};
        }
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for (int i = 0, cLength = c.length; i < cLength; i++) {
            char value = c[i];
            temp.append("\u001B[").append(colors[i]).append("m").append(value).append("\u001B[0m");
        }

        return temp.toString();
    }
}