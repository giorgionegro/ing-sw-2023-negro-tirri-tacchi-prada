package model;

import model.abstractModel.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StandardMessageTest {

    @Test
    void creationTest() {
        Message m = new StandardMessage("ProvaSender", "ProvaSubject", "ProvaText");
        assertEquals("ProvaSender", m.getSender());
        assertEquals("ProvaSubject", m.getReceiver());
        assertEquals("ProvaText", m.getText());
        assertNotNull(m.getTimestamp());
        assertNotNull(m.toString());
    }

}