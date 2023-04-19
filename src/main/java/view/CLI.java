package view;

import model.Tile;
import model.abstractModel.Message;
import modelView.LivingRoomInfo;
import modelView.PlayerChatInfo;
import modelView.ShelfInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLI {

    private String[] buffer = new String[20];
    private String[] livingRoomBuffer;
    private Map<String,String[]> playerShelfBuffer;

    private String[] playerChatBuffer;

    private String[] playerPersonalGoalBuffer;

    private String[] commonGoalBuffer;

    public CLI(){
        this.livingRoomBuffer = new String[10];

        this.playerShelfBuffer = new HashMap<>();

        this.playerChatBuffer = new String[10];
        this.playerPersonalGoalBuffer = new String[18];
        this.commonGoalBuffer = new String[4];
    }



    public void updateLivingRoom(LivingRoomInfo lR){
        Tile[][] board = lR.getBoard();
        int r = 0;
        String nubs = "  0 1 2 3 4 5 6 7 8 ";
        livingRoomBuffer[r]=nubs;
        r++;
        for(int i=0; i<board.length; i++){
            Tile[] row  = board[i];
            String temp = ""+i+" ";
            for(Tile tile : row){
                if(tile==null){
                    temp += " ";
                }else{
                    int color;
                    switch (tile.getColor()){
                        case "Green" -> color = GREEEN;
                        case "White" -> color = WHITE;
                        case "Yellow" -> color = YELLOW;
                        case "Blue" -> color = BLUE;
                        case "LightBlue" -> color = CYAN;
                        case "Magenta" -> color = MAGENTA;
                        default -> color = DEFAULT;
                    }
                    if(color==DEFAULT)
                        temp += " ";
                    else
                        temp += coloredText(color,"█");
                }
                temp +=" ";
            }
            livingRoomBuffer[r] = temp;
            r++;
        }
    }

    public void updateShelf(ShelfInfo sV){
        String[] tempB = new String[7];
        Tile[][] shelf = sV.getShelf();
        int r = 0;
        for(Tile[] row : shelf){
            String temp = "│";
            for(Tile tile : row){
                if(tile==null){
                    temp += " ";
                }else{
                    int color;
                    switch (tile.getColor()){
                        case "Green" -> color = GREEEN;
                        case "White" -> color = WHITE;
                        case "Yellow" -> color = YELLOW;
                        case "Blue" -> color = BLUE;
                        case "LightBlue" -> color = CYAN;
                        case "Magenta" -> color = MAGENTA;
                        default -> color = DEFAULT;
                    }
                    temp += coloredText(color,"█");
                }
                temp += "│";
            }
            tempB[r] = temp;
            r++;
        }
        tempB[r]="───────────";
        this.playerShelfBuffer.put(sV.getPlayerId(), tempB);
    }

    public void updatePlayerChat(PlayerChatInfo pC){
        for(int o = 0; o<playerChatBuffer.length; o++)
            playerChatBuffer[o]="";
        List<Message> messages = pC.getMessages();
        if(messages.size()>10)
            messages.subList(pC.getMessages().size()-10,pC.getMessages().size());
        int i=0;
        for(Message m : messages){
            String temp = "";
            String bufferId = " ";
            for(int k=0; k<8; k++){
                try {
                    bufferId+=m.getSender().charAt(k);
                }catch (IndexOutOfBoundsException e){
                    bufferId+=" ";
                }
            }
            temp +=m.getTimestamp()+" : "+bufferId +" : "+m.getText();
            playerChatBuffer[i]=temp;
            i++;
        }
    }

    public void updateCLI(){
        for(int f = 0; f<buffer.length;f++)
            buffer[f]="";

        int i = 0;
        for(String s : livingRoomBuffer) {
            buffer[i] += s + "  ";
            i++;
        }

        playerShelfBuffer.forEach((s, strings) -> {
            int j=0;
            String bufferId = " ";
            for(int k=0; k<10; k++){
                try {
                    bufferId+=s.charAt(k);
                }catch (IndexOutOfBoundsException e){
                    bufferId+=" ";
                }
            }
            buffer[j]+=bufferId+"  ";
            j++;
            for(String row : strings){
                buffer[j]+=row+"  ";
                j++;
            }
        });

        i = 10;
        for(String s : playerChatBuffer){
          buffer[i]+=s;
          i++;
        }

        for(String s : buffer){
            System.out.println(s);
        }
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
}
