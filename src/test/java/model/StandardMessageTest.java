package model;

import model.abstractModel.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StandardMessageTest {

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link StandardMessage#StandardMessage(String, String, String)}
     *   <li>{@link StandardMessage#toString()}
     *   <li>{@link StandardMessage#getReceiver()}
     *   <li>{@link StandardMessage#getSender()}
     *   <li>{@link StandardMessage#getText()}
     *   <li>{@link StandardMessage#getTimestamp()}
     * </ul>
     */
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