package model;

import model.abstractModel.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardMessageTest {

    @Test
    void creationTest(){
        Message m = new StandardMessage("ProvaSender", "ProvaSubject", "ProvaText");
        assertEquals("ProvaSender", m.getSender());
        assertEquals("ProvaSubject", m.getSubject());
        assertEquals("ProvaText", m.getText());
        assertNotNull(m.getTimestamp());
        assertNotNull(m.toString());
    }

}