package model;

import model.abstractModel.Message;
import model.abstractModel.PlayerChat;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StandardPlayerChatTest {
    @Test
    void chatMessagesInsertionAndGet(){
        PlayerChat chat = new StandardPlayerChat();
        if(chat.getMessages().size()!=0)
            fail("Chat message list is not empty on construction");

        List<Message> messagesTestList = new ArrayList<>();
        List<String> testPlayerIdList = new ArrayList<>();
        int messageNumber = 200;
        int othersPlayerNumber = 3;
        int playerIdLength = 5;

        Random random = new Random();

        //Add broadcast id
        testPlayerIdList.add("");

        //Add random generated ids
        for(int i = 0; i<othersPlayerNumber; i++){
            StringBuilder tempId = new StringBuilder();
            for(int j = 0; j<playerIdLength; j++)
                tempId.append((char) random.nextInt(97, 123));

            testPlayerIdList.add(tempId.toString());
        }

        for(int i = 0; i<messageNumber; i++){
            String sender;
            do{
                sender = testPlayerIdList.get(random.nextInt(testPlayerIdList.size()));
            }while(sender.equals(""));

            String subject = random.nextInt(2)==0 ? "" : "THISPLAYER";

            Message m = new StandardMessage(sender,subject,"TESTO PROVA");

            messagesTestList.add(m);
            chat.addMessage(m);
        }

        List<Message> messagesFromChat = chat.getMessages();
        if(messagesFromChat.size()!=messageNumber)
            fail("Chat does not save all sent messages");

        for(int i = 0; i<messageNumber; i++){
            if(!messagesTestList.get(i).equals(messagesFromChat.get(i)))
                fail("Chat saved an message wrong");
        }
    }
}